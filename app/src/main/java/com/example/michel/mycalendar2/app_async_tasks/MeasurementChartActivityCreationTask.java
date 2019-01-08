package com.example.michel.mycalendar2.app_async_tasks;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.MeasurementChartActivity;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.models.measurement.MeasurementStatEntry;

import java.util.List;

public class MeasurementChartActivityCreationTask extends AsyncTask<Void, Void, String[]> {
    private MeasurementChartActivity view;
    private MeasurementStatEntry mse;

    public MeasurementChartActivityCreationTask(MeasurementChartActivity mca, MeasurementStatEntry mse){
        super();
        view = mca;
        this.mse = mse;
    }

    @Override
    protected String[] doInBackground(Void... voids) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        ReminderTime[] reminderEntriesTime = databaseAdapter.getReminderEntriesTime(mse.getId(), "", 1);
        databaseAdapter.close();
        String[] reminderTimeStrs = new String[reminderEntriesTime.length];
        for (int i=0; i<reminderEntriesTime.length; i++ ){
            reminderTimeStrs[i] = reminderEntriesTime[i].getReminderTimeStr();
        }
        return reminderTimeStrs;
    }

    @Override
    protected void onPostExecute(String[] reminderTimes) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view, android.R.layout.simple_spinner_item, reminderTimes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        view.getReminderTimeSpinner().setAdapter(adapter);

        if (reminderTimes.length>0){
            MeasurementChartCreationTask mcct = new MeasurementChartCreationTask(
                    view, mse.getIdMeasurementType(), mse.getMeasurementValueTypeStr(), reminderTimes[0]);
            mcct.execute(mse.getId(), view.getEndDate().getMonth(), view.getEndDate().getYear());
        }

    }
}
