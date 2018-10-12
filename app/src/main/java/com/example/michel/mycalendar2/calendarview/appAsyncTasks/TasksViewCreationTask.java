package com.example.michel.mycalendar2.calendarview.appAsyncTasks;

import android.os.AsyncTask;
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

    private WeakReference viewRef;
    private View view;

    public TasksViewCreationTask(View view){
        super();
        viewRef = new WeakReference(view);
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
        //LinearLayout tasksLayout = (LinearLayout)((View)viewRef.get()).findViewById(R.id.tasks_layout);

        LinearLayout tasksLayout = (LinearLayout)view.findViewById(R.id.tasks_layout);
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

    }
}
