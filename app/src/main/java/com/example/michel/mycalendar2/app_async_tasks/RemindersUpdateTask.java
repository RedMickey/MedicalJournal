package com.example.michel.mycalendar2.app_async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.PillReminderDBInsertEntry;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class RemindersUpdateTask extends AsyncTask<CycleAndPillComby, Void, Void> {
    private boolean needUpdatePill;
    private Context appContext;

    public RemindersUpdateTask(boolean needUpdatePill, Context context){
        super();
        this.needUpdatePill = needUpdatePill;
        appContext = context;
    }

    @Override
    protected Void doInBackground(CycleAndPillComby... cycleAndPillCombies) {
        PillReminderDBInsertEntry pillReminderDBInsertEntry = cycleAndPillCombies[0].pillReminderDBInsertEntry;
        CycleDBInsertEntry cycleDBInsertEntry = cycleAndPillCombies[0].cycleDBInsertEntry;
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        dbAdapter.open();

        dbAdapter.updateCycle(cycleDBInsertEntry.getIdCycle(), cycleDBInsertEntry.getIdWeekSchedule(),
                cycleDBInsertEntry.getPeriod(), cycleDBInsertEntry.getPeriodDMType(),
                cycleDBInsertEntry.getOnce_aPeriod(), cycleDBInsertEntry.getOnce_aPeriodDMType(),
                cycleDBInsertEntry.getIdCyclingType(), cycleDBInsertEntry.getWeekSchedule()
        );

        dbAdapter.updatePillReminder( pillReminderDBInsertEntry.getIdPillReminder(),
                pillReminderDBInsertEntry.getPillName(), pillReminderDBInsertEntry.getPillCount(),
                pillReminderDBInsertEntry.getIdPillCountType(), pillReminderDBInsertEntry.getStartDate().getDateString(),
                pillReminderDBInsertEntry.getIdCycle(), pillReminderDBInsertEntry.getIdHavingMealsType(),
                pillReminderDBInsertEntry.getHavingMealsTime(), pillReminderDBInsertEntry.getAnnotation(),
                pillReminderDBInsertEntry.getIsActive(), pillReminderDBInsertEntry.getReminderTimes().length,
                needUpdatePill
        );

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String curDate = sdf.format(cal.getTime());
        dbAdapter.deletePillReminderEntriesAfterDate(pillReminderDBInsertEntry.getIdPillReminder(), curDate);
        // need check for change start date

        dbAdapter.deleteReminderTimeByPillReminderId(pillReminderDBInsertEntry.getIdPillReminder());

        for (int i=0; i<pillReminderDBInsertEntry.getReminderTimes().length; i++){
            dbAdapter.insertReminderTime(pillReminderDBInsertEntry.getReminderTimes()[i].getReminderTimeStr(), pillReminderDBInsertEntry.getIdPillReminder(), 0);
        }
        //dbAdapter.deleteIrrelevantReminderTime(pillReminderDBInsertEntry.getIdPillReminder(), pillReminderDBInsertEntry.getStartDate().getDateString());
        /*int[] reminderTimeIds = new int[pillReminderDBInsertEntry.getReminderTimes().length];
        for (int i=0; i<reminderTimeIds.length; i++){
            if (pillReminderDBInsertEntry.getReminderTimes()[i].getIdReminderTime()==0||!pillReminderDBInsertEntry.getReminderTimes()[i].isUsed())
                reminderTimeIds[i] = dbAdapter.insertReminderTime(pillReminderDBInsertEntry.getReminderTimes()[i].getReminderTimeStr()+":00", pillReminderDBInsertEntry.getIdPillReminder(), 0);
        }*/

        if (pillReminderDBInsertEntry.getIsActive()==1) {
            switch (cycleDBInsertEntry.getIdCyclingType()) {
                case 1:
                    for (int i = 0; i < cycleDBInsertEntry.getDayCount(); i++) {
                        for (int j = 0; j < pillReminderDBInsertEntry.getReminderTimes().length; j++) {
                            dbAdapter.insertPillReminderEntries(
                                    sdf.format(new Date(cal.getTimeInMillis())),
                                    pillReminderDBInsertEntry.getIdPillReminder(),
                                    pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr()
                            );
                        }
                        cal.add(Calendar.DATE, 1);
                    }
                    break;
                case 2:
                    for (int i = 0; i < cycleDBInsertEntry.getDayCount(); i++) {
                        if (cycleDBInsertEntry.getWeekSchedule()[cal.get(Calendar.DAY_OF_WEEK) - 1] == 1) {
                            for (int j = 0; j < pillReminderDBInsertEntry.getReminderTimes().length; j++) {
                                dbAdapter.insertPillReminderEntries(
                                        sdf.format(new Date(cal.getTimeInMillis())),
                                        pillReminderDBInsertEntry.getIdPillReminder(),
                                        pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr()
                                );
                            }
                        }
                        cal.add(Calendar.DATE, 1);
                    }
                    break;
                case 3:
                    for (int i = 0; i < cycleDBInsertEntry.getDayCount(); i += cycleDBInsertEntry.getDayInterval()) {
                        for (int j = 0; j < pillReminderDBInsertEntry.getReminderTimes().length; j++) {
                            dbAdapter.insertPillReminderEntries(
                                    sdf.format(new Date(cal.getTimeInMillis())),
                                    pillReminderDBInsertEntry.getIdPillReminder(),
                                    pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr()
                            );
                        }
                        cal.add(Calendar.DATE, cycleDBInsertEntry.getDayInterval());
                    }
                    break;
            }
        }

        dbAdapter.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        NotificationsCreationTask nct = new NotificationsCreationTask();
        nct.execute(appContext);
    }
}
