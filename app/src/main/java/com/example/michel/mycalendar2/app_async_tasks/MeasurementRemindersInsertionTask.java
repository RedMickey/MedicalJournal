package com.example.michel.mycalendar2.app_async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.michel.mycalendar2.models.CycleAndMeasurementComby;

public class MeasurementRemindersInsertionTask extends AsyncTask<CycleAndMeasurementComby, Void, Void> {
    private Context appContext;

    public MeasurementRemindersInsertionTask(Context context){
        appContext = context;
    }

    @Override
    protected Void doInBackground(CycleAndMeasurementComby... cycleAndMeasurementCombies) {


        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {

    }
}
