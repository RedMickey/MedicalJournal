package com.example.michel.mycalendar2.calendarview.appAsyncTasks;

import android.os.AsyncTask;
import android.util.Log;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.views.DayFragment;
import com.example.michel.mycalendar2.models.TakingMedicine;

import java.util.ArrayList;
import java.util.List;

public class TasksViewCreationTask extends AsyncTask<DateData, Void, List<TakingMedicine>>{
    @Override
    protected List<TakingMedicine> doInBackground(DateData... dateData) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        List<TakingMedicine> takingMedicines = databaseAdapter.getTakingMedicine();
        databaseAdapter.close();

        return takingMedicines;
    }

    @Override
    protected void onPostExecute(List<TakingMedicine> takingMedicines) {
        for (TakingMedicine tm: takingMedicines) {
            Log.i("tm", tm.getName());
        }
    }
}
