package com.example.michel.mycalendar2.app_async_tasks;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.adapters.ReminderListAdapter;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.models.PillReminder;
import com.example.michel.mycalendar2.models.PillReminderEntry;

import java.util.List;

public class ReminderMedicineItemsCreationTask extends AsyncTask<Void, Void, List<PillReminder>> {
    private View view;

    public ReminderMedicineItemsCreationTask(View view){
        super();
        this.view = view;
    }

    @Override
    protected List<PillReminder> doInBackground(Void... voids) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        List<PillReminder> pillReminders = databaseAdapter.getAllPillReminders();

        databaseAdapter.close();

        return pillReminders;
    }

    @Override
    protected void onPostExecute(List<PillReminder> pillReminders) {

        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        if(pillReminders.size()>0){
            ListView listView = (ListView) view.findViewById(R.id.reminder_list_view);
            ReminderListAdapter reminderListAdapter = new ReminderListAdapter(view.getContext(), R.layout.reminder_medicine_item, pillReminders);
            listView.setAdapter(reminderListAdapter);
        }
    }
}
