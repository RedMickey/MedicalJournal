package com.example.michel.mycalendar2.calendarview.appAsyncTasks;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.views.DayFragment;
import com.example.michel.mycalendar2.models.TakingMedicine;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class TasksViewCreationTask extends AsyncTask<DateData, Void, List<TakingMedicine>>{

    private WeakReference viewRef;

    public TasksViewCreationTask(View view){
        super();
        viewRef = new WeakReference(view);
    }

    @Override
    protected List<TakingMedicine> doInBackground(DateData... dateData) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        List<TakingMedicine> takingMedicines = databaseAdapter.getTakingMedicineByDate(dateData[0]);
        //List<TakingMedicine> takingMedicines = databaseAdapter.getTakingMedicine();
        databaseAdapter.close();

        return takingMedicines;
    }

    @Override
    protected void onPostExecute(List<TakingMedicine> takingMedicines) {
        LinearLayout tasksLayout = (LinearLayout)((View)viewRef.get()).findViewById(R.id.tasks_layout);
        for (TakingMedicine tm: takingMedicines) {
            TextView taskNote = new TextView(((View)viewRef.get()).getContext());
            LinearLayout.LayoutParams taskNoteParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            taskNote.setText(tm.getName());
            taskNote.setLayoutParams(taskNoteParams);

            tasksLayout.addView(taskNote);
        }
        if (takingMedicines.isEmpty()){
            TextView taskNote = new TextView(((View)viewRef.get()).getContext());
            LinearLayout.LayoutParams taskNoteParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            taskNote.setText("Empty");
            taskNote.setLayoutParams(taskNoteParams);

            tasksLayout.addView(taskNote);
        }
    }
}
