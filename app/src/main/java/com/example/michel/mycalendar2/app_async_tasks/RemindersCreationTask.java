package com.example.michel.mycalendar2.app_async_tasks;

import android.os.AsyncTask;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.PillReminderDBInsertEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RemindersCreationTask extends AsyncTask<CycleAndPillComby, Void, Void> {
    @Override
    protected Void doInBackground(CycleAndPillComby... cycleAndPillCombies) {
        PillReminderDBInsertEntry pillReminderDBInsertEntry = cycleAndPillCombies[0].pillReminderDBInsertEntry;
        CycleDBInsertEntry cycleDBInsertEntry = cycleAndPillCombies[0].cycleDBInsertEntry;
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        dbAdapter.open();

        pillReminderDBInsertEntry.setIdCycle(
                dbAdapter.insertCycle(cycleDBInsertEntry.getPeriod(), cycleDBInsertEntry.getPeriodDMType(),
                        cycleDBInsertEntry.getOnce_aPeriod(), cycleDBInsertEntry.getOnce_aPeriodDMType(),
                        cycleDBInsertEntry.getIdCyclingType(), cycleDBInsertEntry.getWeekSchedule()
                )
        );


        Integer pillReminderId = dbAdapter.insertPillReminder(
                pillReminderDBInsertEntry.getPillName(), pillReminderDBInsertEntry.getPillCount(),
                pillReminderDBInsertEntry.getIdPillCountType(), pillReminderDBInsertEntry.getStartDate().getDateString(),
                pillReminderDBInsertEntry.getIdCycle(), pillReminderDBInsertEntry.getIdHavingMealsType(),
                pillReminderDBInsertEntry.getHavingMealsTime(), pillReminderDBInsertEntry.getAnnotation(),
                pillReminderDBInsertEntry.getIsActive(), pillReminderDBInsertEntry.getReminderTimes().length
        );

        for (int i=0; i<pillReminderDBInsertEntry.getReminderTimes().length; i++){
            dbAdapter.insertReminderTime(pillReminderDBInsertEntry.getReminderTimes()[i].getReminderTimeStr(), pillReminderId, 0);
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
                        dbAdapter.insertPillReminderEntries(
                                sdf.format(new Date(cal.getTimeInMillis())),
                                pillReminderId,
                                pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr()
                                );
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 2:
                for(int i=0; i<cycleDBInsertEntry.getDayCount();i++){
                    if(cycleDBInsertEntry.getWeekSchedule()[cal.get(Calendar.DAY_OF_WEEK)-1]==1){
                        for (int j=0; j<pillReminderDBInsertEntry.getReminderTimes().length; j++){
                            dbAdapter.insertPillReminderEntries(
                                    sdf.format(cal.getTime()),
                                    pillReminderId,
                                    pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr()
                            );
                        }
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 3:
                for(int i=0; i<cycleDBInsertEntry.getDayCount();i+=cycleDBInsertEntry.getDayInterval()){
                    for (int j=0; j<pillReminderDBInsertEntry.getReminderTimes().length; j++){
                        dbAdapter.insertPillReminderEntries(
                                sdf.format(new Date(cal.getTimeInMillis())),
                                pillReminderId,
                                pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr()
                        );
                    }
                    cal.add(Calendar.DATE, cycleDBInsertEntry.getDayInterval());
                }
                break;
        }

        dbAdapter.close();

        return null;
    }

}
