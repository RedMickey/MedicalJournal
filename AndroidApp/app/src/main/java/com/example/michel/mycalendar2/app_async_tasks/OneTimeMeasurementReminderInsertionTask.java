package com.example.michel.mycalendar2.app_async_tasks;

import android.os.AsyncTask;

import com.example.michel.mycalendar2.app_async_tasks.synchronization.SynchronizationMeasurementReminderTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderDBEntry;

import java.util.UUID;

public class OneTimeMeasurementReminderInsertionTask extends AsyncTask<MeasurementReminderDBEntry, Void, Void> {
    private double value1;
    private double value2;

    public OneTimeMeasurementReminderInsertionTask(double value1, double value2){
        super();
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    protected Void doInBackground(MeasurementReminderDBEntry... measurementReminderDBEntries) {
        MeasurementReminderDBEntry mrdbe = measurementReminderDBEntries[0];
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(dbAdapter.open().getDatabase());

        UUID measurementReminderId = measurementReminderDao.insertMeasurementReminder(
                mrdbe.getIdMeasurementType(),
                mrdbe.getStartDate().getDateString(),
                mrdbe.getIdCycle(), mrdbe.getIdHavingMealsType(),
                mrdbe.getHavingMealsTime(), mrdbe.getAnnotation(),
                mrdbe.getIsActive(), mrdbe.getReminderTimes().length,1,
                mrdbe.getIsGfitListening()
        );

        measurementReminderDao.insertMeasurementReminderEntry(
                mrdbe.getStartDate().getDateString(),
                measurementReminderId,
                mrdbe.getReminderTimes()[0].getReminderTimeStr(),
                value1, value2
        );

        dbAdapter.close();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (AccountGeneralUtils.curUser.getId()!=1){
            SynchronizationMeasurementReminderTask synchronizationMeasurementReminderTask = new
                    SynchronizationMeasurementReminderTask(DatabaseAdapter.AppContext);
            synchronizationMeasurementReminderTask.execute();
        }
    }
}
