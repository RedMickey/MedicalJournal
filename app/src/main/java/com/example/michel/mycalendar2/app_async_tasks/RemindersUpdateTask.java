package com.example.michel.mycalendar2.app_async_tasks;

import android.os.AsyncTask;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.PillReminderDBInsertEntry;

public class RemindersUpdateTask extends AsyncTask<CycleAndPillComby, Void, Void> {
    @Override
    protected Void doInBackground(CycleAndPillComby... cycleAndPillCombies) {
        PillReminderDBInsertEntry pillReminderDBInsertEntry = cycleAndPillCombies[0].pillReminderDBInsertEntry;
        CycleDBInsertEntry cycleDBInsertEntry = cycleAndPillCombies[0].cycleDBInsertEntry;
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        dbAdapter.open();

        /*dbAdapter.updateCycle(cycleDBInsertEntry.getIdCycle(), ,
                cycleDBInsertEntry.getPeriod(), cycleDBInsertEntry.getPeriodDMType(),
                cycleDBInsertEntry.getOnce_aPeriod(), cycleDBInsertEntry.getOnce_aPeriodDMType(),
                cycleDBInsertEntry.getIdCyclingType(), cycleDBInsertEntry.getWeekSchedule()
        );*/

        Integer pillReminderId = dbAdapter.insertPillReminder(
                pillReminderDBInsertEntry.getPillName(), pillReminderDBInsertEntry.getPillCount(),
                pillReminderDBInsertEntry.getIdPillCountType(), pillReminderDBInsertEntry.getStartDate().getDateString(),
                pillReminderDBInsertEntry.getIdCycle(), pillReminderDBInsertEntry.getIdHavingMealsType(),
                pillReminderDBInsertEntry.getHavingMealsTime(), pillReminderDBInsertEntry.getAnnotation(),
                pillReminderDBInsertEntry.getIsActive(), pillReminderDBInsertEntry.getReminderTimes().length
        );

        dbAdapter.close();
        return null;
    }
}
