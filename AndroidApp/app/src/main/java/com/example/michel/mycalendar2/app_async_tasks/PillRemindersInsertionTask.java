package com.example.michel.mycalendar2.app_async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class PillRemindersInsertionTask extends AsyncTask<CycleAndPillComby, Void, Void> {
    private Context appContext;

    public PillRemindersInsertionTask(Context context){
        appContext = context;
    }

    @Override
    protected Void doInBackground(CycleAndPillComby... cycleAndPillCombies) {
        PillReminderDBInsertEntry pillReminderDBInsertEntry = cycleAndPillCombies[0].pillReminderDBInsertEntry;
        CycleDBInsertEntry cycleDBInsertEntry = cycleAndPillCombies[0].cycleDBInsertEntry;
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        PillReminderDao pillReminderDao = new PillReminderDao(dbAdapter.open().getDatabase());
        CycleDao cycleDao = new CycleDao(dbAdapter.getDatabase());
        ReminderTimeDao reminderTimeDao = new ReminderTimeDao(dbAdapter.getDatabase());

        pillReminderDBInsertEntry.setIdCycle(
                cycleDao.insertCycle(cycleDBInsertEntry.getPeriod(), cycleDBInsertEntry.getPeriodDMType(),
                        cycleDBInsertEntry.getOnce_aPeriod(), cycleDBInsertEntry.getOnce_aPeriodDMType(),
                        cycleDBInsertEntry.getIdCyclingType(), cycleDBInsertEntry.getWeekSchedule()
                )
        );

        UUID pillReminderId = pillReminderDao.insertPillReminder(
                pillReminderDBInsertEntry.getPillName(), pillReminderDBInsertEntry.getPillCount(),
                pillReminderDBInsertEntry.getIdPillCountType(), pillReminderDBInsertEntry.getStartDate().getDateString(),
                pillReminderDBInsertEntry.getIdCycle(), pillReminderDBInsertEntry.getIdHavingMealsType(),
                pillReminderDBInsertEntry.getHavingMealsTime(), pillReminderDBInsertEntry.getAnnotation(),
                pillReminderDBInsertEntry.getIsActive(), pillReminderDBInsertEntry.getReminderTimes().length, 0
        );

        for (int i=0; i<pillReminderDBInsertEntry.getReminderTimes().length; i++){
            reminderTimeDao.insertReminderTime(pillReminderDBInsertEntry.getReminderTimes()[i].getReminderTimeStr(), pillReminderId, 0);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(pillReminderDBInsertEntry.getStartDate().getYear(),
                pillReminderDBInsertEntry.getStartDate().getMonth()-1,
                pillReminderDBInsertEntry.getStartDate().getDay());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        switch (cycleDBInsertEntry.getIdCyclingType()){
            case 1:
                for(int i=0; i<cycleDBInsertEntry.getDayCount();i++){
                    for (int j=0; j<pillReminderDBInsertEntry.getReminderTimes().length; j++){
                        pillReminderDao.insertPillReminderEntry(
                                sdf.format(new Date(cal.getTimeInMillis())),
                                pillReminderId,
                                pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr(),0
                                );
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 2:
                for(int i=0; i<cycleDBInsertEntry.getDayCount();i++){
                    if(cycleDBInsertEntry.getWeekSchedule()[cal.get(Calendar.DAY_OF_WEEK)-1]==1){
                        for (int j=0; j<pillReminderDBInsertEntry.getReminderTimes().length; j++){
                            pillReminderDao.insertPillReminderEntry(
                                    sdf.format(cal.getTime()),
                                    pillReminderId,
                                    pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr(),0
                            );
                        }
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 3:
                for(int i=0; i<cycleDBInsertEntry.getDayCount();i+=cycleDBInsertEntry.getDayInterval()){
                    for (int j=0; j<pillReminderDBInsertEntry.getReminderTimes().length; j++){
                        pillReminderDao.insertPillReminderEntry(
                                sdf.format(new Date(cal.getTimeInMillis())),
                                pillReminderId,
                                pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr(),0
                        );
                    }
                    cal.add(Calendar.DATE, cycleDBInsertEntry.getDayInterval());
                }
                break;
        }

        dbAdapter.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        PillNotificationsCreationTask nct = new PillNotificationsCreationTask();
        nct.execute(appContext);
    }
}
