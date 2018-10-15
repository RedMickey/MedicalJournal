package com.example.michel.mycalendar2.calendarview.appAsyncTasks;

import android.os.AsyncTask;
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

        View pillReminderEntryView = inflater.inflate(R.layout.pill_reminder_entry, null, false);

        if(pillReminderEntries.size()>0)
            ((TextView) pillReminderEntryView.findViewById(R.id.pill_name_tv)).setText(pillReminderEntries.get(0).getName());
        else
            ((TextView) pillReminderEntryView.findViewById(R.id.pill_name_tv)).setText("Empty");
/*
        for (PillReminderEntry pre: pillReminderEntries) {
            //TextView taskNote = new TextView(((View)viewRef.get()).getContext());
            TextView taskNote = new TextView(view.getContext());
            LinearLayout.LayoutParams taskNoteParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            taskNote.setText(pre.getName() + " "+ pre.getTime());
            taskNote.setLayoutParams(taskNoteParams);

            tasksLayout.addView(taskNote);
        }
        if (pillReminderEntries.isEmpty()){
            //TextView taskNote = new TextView(((View)viewRef.get()).getContext());
            TextView taskNote = new TextView(view.getContext());
            LinearLayout.LayoutParams taskNoteParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            taskNote.setText("Empty");
            taskNote.setLayoutParams(taskNoteParams);

            tasksLayout.addView(taskNote);
        }
*/
        tasksLayout.addView(pillReminderEntryView);
    }
}
