package com.example.michel.mycalendar2.app_async_tasks;

import android.os.AsyncTask;

import com.example.michel.mycalendar2.app_async_tasks.synchronization.SynchronizationPillReminderTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;

import java.util.UUID;

public class OneTimePillReminderInsertionTask extends AsyncTask<PillReminderDBInsertEntry, Void, Void> {

    @Override
    protected Void doInBackground(PillReminderDBInsertEntry... pillReminderDBInsertEntries) {
        PillReminderDBInsertEntry pillReminderDBInsertEntry = pillReminderDBInsertEntries[0];

        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        PillReminderDao pillReminderDao = new PillReminderDao(dbAdapter.open().getDatabase());

        UUID pillReminderId = pillReminderDao.insertPillReminder(
                pillReminderDBInsertEntry.getPillName(), pillReminderDBInsertEntry.getPillCount(),
                pillReminderDBInsertEntry.getIdPillCountType(), pillReminderDBInsertEntry.getStartDate().getDateString(),
                pillReminderDBInsertEntry.getIdCycle(), pillReminderDBInsertEntry.getIdHavingMealsType(),
                pillReminderDBInsertEntry.getHavingMealsTime(), pillReminderDBInsertEntry.getAnnotation(),
                pillReminderDBInsertEntry.getIsActive(), pillReminderDBInsertEntry.getReminderTimes().length, 1
        );

        pillReminderDao.insertPillReminderEntry(
                pillReminderDBInsertEntry.getStartDate().getDateString(),
                pillReminderId,
                pillReminderDBInsertEntry.getReminderTimes()[0].getReminderTimeStr(),1
        );

        dbAdapter.close();
        return null;
    }


    @Override
    protected void onPostExecute(Void aVoid) {
        if (AccountGeneralUtils.curUser.getId()!=1) {
            SynchronizationPillReminderTask synchronizationPillReminderTask = new SynchronizationPillReminderTask(DatabaseAdapter.AppContext);
            synchronizationPillReminderTask.execute();
        }
    }
}
