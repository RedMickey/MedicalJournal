package com.example.michel.mycalendar2.app_async_tasks;

import android.os.AsyncTask;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
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
        dbAdapter.open();

        UUID measurementReminderId = dbAdapter.insertMeasurementReminder(
                mrdbe.getIdMeasurementType(),
                mrdbe.getStartDate().getDateString(),
                mrdbe.getIdCycle(), mrdbe.getIdHavingMealsType(),
                mrdbe.getHavingMealsTime(), mrdbe.getAnnotation(),
                mrdbe.getIsActive(), mrdbe.getReminderTimes().length,1
        );

        dbAdapter.insertMeasurementReminderEntry(
                mrdbe.getStartDate().getDateString(),
                measurementReminderId,
                mrdbe.getReminderTimes()[0].getReminderTimeStr(),
                value1, value2
        );

        dbAdapter.close();

        return null;
    }
}
