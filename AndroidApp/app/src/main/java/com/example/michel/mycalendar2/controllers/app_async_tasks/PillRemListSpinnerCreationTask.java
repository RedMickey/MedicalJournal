package com.example.michel.mycalendar2.controllers.app_async_tasks;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.michel.mycalendar2.activities.AddOneTimeTreatmentActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.controllers.adapters.PillReminderListAdapter;
import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.models.pill.PillReminder;

import java.util.List;

public class PillRemListSpinnerCreationTask extends AsyncTask<Void, Void, List<PillReminder>> {
    private View view;
    private AddOneTimeTreatmentActivity activity;
    private AlertDialog dialog;

    public PillRemListSpinnerCreationTask(View view, AddOneTimeTreatmentActivity activity, AlertDialog dialog){
        super();
        this.view = view;
        this.activity = activity;
        this.dialog = dialog;
    }

    @Override
    protected List<PillReminder> doInBackground(Void... voids) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        PillReminderDao pillReminderDao = new PillReminderDao(databaseAdapter.open().getDatabase());
        List<PillReminder> pillReminders = pillReminderDao.getAllPillReminders();

        databaseAdapter.close();

        return pillReminders;
    }

    @Override
    protected void onPostExecute(List<PillReminder> pillReminders) {
        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        ((Button) view.findViewById(R.id.no_prescription_button)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.getPrescriptionTv().setText("Без рецепта");
                EditText editTextMedicineName = activity.findViewById(R.id.edit_text_medicine_name);
                editTextMedicineName.setText("");
                editTextMedicineName.setEnabled(true);
                dialog.dismiss();
            }
        });

        if(pillReminders.size()>0){
            ListView listView = (ListView) view.findViewById(R.id.reminder_list_view);
            PillReminderListAdapter reminderListAdapter = new PillReminderListAdapter(view.getContext(), R.layout.reminder_medicine_item, pillReminders);
            AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    PillReminder pr = (PillReminder)adapterView.getItemAtPosition(i);
                    OneTimeTreatmentParamsChangingTask ottpct = new OneTimeTreatmentParamsChangingTask(activity);
                    ottpct.execute(pr.getId());
                    dialog.dismiss();
                }
            };

            listView.setAdapter(reminderListAdapter);
            listView.setOnItemClickListener(itemClickListener);
        }
    }
}
