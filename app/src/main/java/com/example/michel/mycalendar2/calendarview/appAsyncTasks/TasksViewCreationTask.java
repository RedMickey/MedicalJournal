package com.example.michel.mycalendar2.calendarview.appAsyncTasks;

import android.os.AsyncTask;

import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.views.DayFragment;
import com.example.michel.mycalendar2.models.TakingMedicine;

import java.util.List;

public class TasksViewCreationTask extends AsyncTask<DateData, Void, List<TakingMedicine>>{
    @Override
    protected List<TakingMedicine> doInBackground(DateData... dateData) {


        return null;
    }

    @Override
    protected void onPostExecute(List<TakingMedicine> takingMedicines) {
        super.onPostExecute(takingMedicines);
    }
}
