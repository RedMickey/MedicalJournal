package com.example.michel.mycalendar2.calendarview.appAsyncTasks;

import android.os.AsyncTask;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.models.PillReminderEntry;

import java.lang.ref.WeakReference;
import java.util.List;

public class TasksViewCreationTask extends AsyncTask<DateData, Void, List<PillReminderEntry>>{

    private View view;

    public TasksViewCreationTask(View view){
        super();
        this.view = view;
    }

    @Override
    protected List<PillReminderEntry> doInBackground(DateData... dateData) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        List<PillReminderEntry> pillReminderEntries = databaseAdapter.getPillReminderEntriesByDate(dateData[0]);
        //List<TakingMedicine> takingMedicines = databaseAdapter.getTakingMedicine();
        databaseAdapter.close();

        return pillReminderEntries;
    }

    @Override
    protected void onPostExecute(List<PillReminderEntry> pillReminderEntries) {
        LinearLayout tasksLayout = (LinearLayout)view.findViewById(R.id.pill_reminder_entries_layout);

        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        if(pillReminderEntries.size()>0)
        {
            View pillReminderEntryView = inflater.inflate(R.layout.pill_reminder_entry, null, false);
            ((TextView) pillReminderEntryView.findViewById(R.id.pill_name_tv)).setText(pillReminderEntries.get(0).getName());
            tasksLayout.addView(pillReminderEntryView);
        }
        else
        {
            TextView taskNote = new TextView(view.getContext());
            LinearLayout.LayoutParams taskNoteParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            taskNote.setText("Empty");
            taskNote.setLayoutParams(taskNoteParams);
            taskNote.setGravity(Gravity.CENTER);
            tasksLayout.addView(taskNote);
        }

    }
}
