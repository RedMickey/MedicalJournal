package com.example.michel.mycalendar2.app_async_tasks;

import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.adapters.StatisticListAdapter;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.models.measurement.MeasurementStatEntry;

import java.util.ArrayList;
import java.util.List;

public class StatisticListItemsCreationTask extends AsyncTask<Integer, Void, List<MeasurementStatEntry>> {
    private View mView;

    public StatisticListItemsCreationTask(View view){
        super();
        this.mView = view;
    }

    @Override
    protected List<MeasurementStatEntry> doInBackground(Integer... integers) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.open().getDatabase());
        List<MeasurementStatEntry> measurementStatEntries = measurementReminderDao.getAllMeasurementStatEntries(integers[0]);
        databaseAdapter.close();

        return measurementStatEntries;
    }

    @Override
    protected void onPostExecute(List<MeasurementStatEntry> measurementStatEntries) {
        if(measurementStatEntries.size()>0){
            RecyclerView recyclerView = (RecyclerView)mView.findViewById(R.id.statistic_recycle_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(mView.getContext()));
            StatisticListAdapter mAdapter = new StatisticListAdapter(mView.getContext(), measurementStatEntries);
            recyclerView.setAdapter(mAdapter);
        }
    }
}
