package com.example.michel.mycalendar2.controllers.app_async_tasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.ChoosingMeasurementCourseActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.controllers.adapters.MeasurementReminderListAdapter;
import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminder;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChoosingMeasurCourseActCreationTask extends AsyncTask<Void, Void, List<MeasurementReminder>> {
    private int idMeasurementType;
    private Date curDate;
    private ChoosingMeasurementCourseActivity activity;

    public ChoosingMeasurCourseActCreationTask(ChoosingMeasurementCourseActivity activity, int idMeasurementType, Date curDate){
        super();
        this.idMeasurementType = idMeasurementType;
        this.curDate = curDate;
        this.activity = activity;
    }

    @Override
    protected List<MeasurementReminder> doInBackground(Void... voids) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.open().getDatabase());
        List<MeasurementReminder> measurementReminders = measurementReminderDao
                .getMeasurementRemindersByTypeAndDate(idMeasurementType, curDate);

        databaseAdapter.close();

        return measurementReminders;
    }

    @Override
    protected void onPostExecute(List<MeasurementReminder> measurementReminders) {
        LayoutInflater inflater = LayoutInflater.from(activity);

        if (measurementReminders.size()>0){
            ListView listView = (ListView) activity.findViewById(R.id.reminder_list_view);
            MeasurementReminderListAdapter reminderListAdapter = new MeasurementReminderListAdapter(
                    activity, R.layout.reminder_measurement_item, measurementReminders);
            AdapterView.OnItemClickListener clickListener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    LayoutInflater inflater = LayoutInflater.from(view.getContext());
                    SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM");
                    View dialogView = inflater.inflate(R.layout.choosing_reminder_time_dialog_layout, null, false);
                    ((TextView)dialogView.findViewById(R.id.text_current_date)).setText(
                            dateFormat.format(curDate)
                    );

                    builder.setView(dialogView)
                            .setTitle("Выберите время")
                            .setCancelable(true)
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog dialog = builder.create();

                    ReminderTimeListCreationTask creationTask = new ReminderTimeListCreationTask(dialog,
                            (MeasurementReminder) parent.getItemAtPosition(position), dialogView, curDate,
                            new double[]{activity.getValue1(), activity.getValue2()});
                    creationTask.execute();

                    dialog.show();
                }
            };

            listView.setAdapter(reminderListAdapter);
            listView.setOnItemClickListener(clickListener);
        }
        else
            ((TextView) activity.findViewById(R.id.error_tv)).setVisibility(View.VISIBLE);
    }
}
