package com.example.michel.mycalendar2.app_async_tasks;

import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.michel.mycalendar2.activities.AddMeasurementActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.adapters.MeasurementReminderListAdapter;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminder;
import com.example.michel.mycalendar2.models.pill.PillReminder;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.util.List;

public class ReminderMeasurementItemsCreationTask extends AsyncTask<Void, Void, List<MeasurementReminder>> {
    private View view;

    public ReminderMeasurementItemsCreationTask(View view){
        super();
        this.view = view;
    }

    @Override
    protected List<MeasurementReminder> doInBackground(Void... voids) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        List<MeasurementReminder> measurementReminders = databaseAdapter.getAllMeasurementReminders();

        databaseAdapter.close();

        return measurementReminders;
    }

    @Override
    protected void onPostExecute(List<MeasurementReminder> measurementReminders) {
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        if(measurementReminders.size()>0){
            ListView listView = (ListView) view.findViewById(R.id.reminder_list_view);
            MeasurementReminderListAdapter reminderListAdapter = new MeasurementReminderListAdapter(view.getContext(), R.layout.reminder_measurement_item, measurementReminders);
            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    MeasurementReminder mr = (MeasurementReminder)adapterView.getItemAtPosition(i);
                    Intent intent = new Intent(view.getContext(), AddMeasurementActivity.class);
                    intent.putExtra("MeasurementReminderID", mr.getId());
                    intent.putExtra("MeasurementTypeID", mr.getIdMeasurementType());
                    intent.putExtra("MeasurementName", DBStaticEntries.getMeasurementTypeById(mr.getIdMeasurementType()).getName());
                    view.getContext().startActivity(intent);
                }
            };

            listView.setAdapter(reminderListAdapter);
            listView.setOnItemClickListener(itemClickListener);
        }
    }
}
