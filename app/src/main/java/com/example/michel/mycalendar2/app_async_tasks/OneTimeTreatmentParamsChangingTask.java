package com.example.michel.mycalendar2.app_async_tasks;

import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.michel.mycalendar2.activities.AddOneTimeTreatmentActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.models.pill.PillReminder;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.util.List;

public class OneTimeTreatmentParamsChangingTask extends AsyncTask<Integer, Void, PillReminderDBInsertEntry> {
    private AddOneTimeTreatmentActivity activity;

    public OneTimeTreatmentParamsChangingTask(AddOneTimeTreatmentActivity activity){
        super();
        this.activity = activity;
    }

    @Override
    protected PillReminderDBInsertEntry doInBackground(Integer... integers) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        PillReminderDBInsertEntry pillReminderDBInsertEntry = databaseAdapter.getPillReminderDBInsertEntryByID(integers[0]);

        databaseAdapter.close();
        return pillReminderDBInsertEntry;
    }

    @Override
    protected void onPostExecute(PillReminderDBInsertEntry pillReminderDBInsertEntry) {
        activity.getPrescriptionTv().setText(pillReminderDBInsertEntry.getPillName());
        EditText editTextMedicineName = activity.findViewById(R.id.edit_text_medicine_name);
        editTextMedicineName.setText(pillReminderDBInsertEntry.getPillName());
        editTextMedicineName.setEnabled(false);
        String mapDoseTypesValue = DBStaticEntries.getKeyByValue2(DBStaticEntries.doseTypes, pillReminderDBInsertEntry.getIdPillCountType());
        Spinner spinnerDoseType = (Spinner) activity.findViewById(R.id.dose_type);
        int spinnerPosition = ((ArrayAdapter) spinnerDoseType.getAdapter()).getPosition(mapDoseTypesValue);
        spinnerDoseType.setSelection(spinnerPosition);
    }
}
