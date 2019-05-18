package com.example.michel.mycalendar2.app_async_tasks;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.adapters.ReminderTimeListAdapter;
import com.example.michel.mycalendar2.app_async_tasks.synchronization.SynchronizationReminderEntriesTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminder;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReminderTimeListCreationTask extends AsyncTask<Void, Void, List<MeasurementReminderEntry>> {
    private AlertDialog dialog;
    private View dialogView;
    private MeasurementReminder measurementReminder;
    private Date curDate;
    private double[] values;

    public ReminderTimeListCreationTask(AlertDialog dialog, MeasurementReminder measurementReminder, View dialogView, Date curDate,
                                        double[] values){
        super();
        this.dialog = dialog;
        this.measurementReminder = measurementReminder;
        this.dialogView = dialogView;
        this.curDate = curDate;
        this.values = values;
    }

    @Override
    protected List<MeasurementReminderEntry> doInBackground(Void... voids) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        Calendar calBuf = Calendar.getInstance();
        calBuf.setTime(curDate);
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.open().getDatabase());
        List<MeasurementReminderEntry> measurementReminderEntries = measurementReminderDao
                .getMeasurementReminderEntriesByDateAndMeasurementReminder(measurementReminder.getId(),
                        new DateData(calBuf.get(Calendar.YEAR), calBuf.get(Calendar.MONTH)+1, calBuf.get(Calendar.DAY_OF_MONTH)));

        databaseAdapter.close();

        return measurementReminderEntries;
    }

    @Override
    protected void onPostExecute(List<MeasurementReminderEntry> measurementReminderEntries) {
        ListView listView = (ListView) dialogView.findViewById(R.id.reminder_time_list_view);
        ReminderTimeListAdapter listAdapter = new ReminderTimeListAdapter(dialogView.getContext(), R.layout.reminder_time_dialog_item, measurementReminderEntries);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                MeasurementReminderEntry mre = (MeasurementReminderEntry)parent.getItemAtPosition(position);
                mre.setValue1(values[0]);
                if ((mre.getIdMeasurementType() == 2)){
                    mre.setValue2(values[1]);
                }

                DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.open().getDatabase());
                measurementReminderDao.updateIsDoneMeasurementReminderEntry(1, mre.getId(), sdf.format(curDate)+":00",
                        mre.getValue1(), mre.getValue2(), 0);
                databaseAdapter.close();
                if (AccountGeneralUtils.curUser.getId()!=1) {
                    SynchronizationReminderEntriesTask synchronizationReminderEntriesTask = new SynchronizationReminderEntriesTask(
                            dialogView.getContext(), mre.getId(), 2
                    );
                    synchronizationReminderEntriesTask.execute();
                }
                Toast.makeText(view.getContext(),"Запись добавлена", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
            }
        };

        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(itemClickListener);
    }
}
