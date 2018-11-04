package com.example.michel.mycalendar2.calendarview.async_tasks;

import android.os.AsyncTask;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.models.PillReminderEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        if(pillReminderEntries.size()>0)
        {
            calendar2.setTime(pillReminderEntries.get(0).getDate());
            boolean isToday = calendar2.get(Calendar.DAY_OF_YEAR)<=calendar1.get(Calendar.DAY_OF_YEAR);
            for (final PillReminderEntry pre:pillReminderEntries) {
                View pillReminderEntryView = inflater.inflate(R.layout.pill_reminder_entry, null, false);
                ((TextView) pillReminderEntryView.findViewById(R.id.pill_name_tv)).setText(pre.getPillName());
                final TextView reminderTimeTv = (TextView) pillReminderEntryView.findViewById(R.id.reminder_time_tv);
                reminderTimeTv.setText(new SimpleDateFormat("HH:mm").format(pre.getDate()));
                ((TextView) pillReminderEntryView.findViewById(R.id.pill_count_type_tv))
                        .setText(String.valueOf(pre.getPillCount())+" "+pre.getPillCountType());
                switch (pre.getHavingMealsType()){
                    case 1:
                        ((ImageView) pillReminderEntryView.findViewById(R.id.having_meals_iv)).setImageResource(R.drawable.icons8_50_21);
                        break;
                    case 2:
                        ((ImageView) pillReminderEntryView.findViewById(R.id.having_meals_iv)).setImageResource(R.drawable.icons8_50_61);
                        break;
                    case 3:
                        ((ImageView) pillReminderEntryView.findViewById(R.id.having_meals_iv)).setImageResource(R.drawable.icons8_50_4);
                        break;
                }
                final ImageView imageTimeExpired = ((ImageView) pillReminderEntryView.findViewById(R.id.late_indication));
                if(pre.isLate())
                    imageTimeExpired.setImageResource(R.drawable.ic_time_expired);
                CheckBox isDoneChb = (CheckBox) pillReminderEntryView.findViewById(R.id.is_done_chb);
                if (!isToday)
                    isDoneChb.setEnabled(false);
                if(pre.getIsDone()==1)
                    isDoneChb.setChecked(true);
                isDoneChb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                        databaseAdapter.open();
                        if (b){
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String curTime = sdf.format(cal.getTime());
                            reminderTimeTv.setText(curTime);
                            databaseAdapter.updateIsDonePillReminderEntry(1, pre.getId(), curTime+":00");
                            if (pre.isLate())
                                imageTimeExpired.setImageResource(android.R.color.transparent);
                        }
                        else {
                            databaseAdapter.updateIsDonePillReminderEntry( 0, pre.getId(), "");
                            if (pre.isLateCheck())
                                imageTimeExpired.setImageResource(R.drawable.ic_time_expired);
                        }
                        //databaseAdapter.updateIsDonePillReminderEntry(b ? 1 : 0, pre.getId());
                        databaseAdapter.close();
                    }
                });
                tasksLayout.addView(pillReminderEntryView);
            }
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
