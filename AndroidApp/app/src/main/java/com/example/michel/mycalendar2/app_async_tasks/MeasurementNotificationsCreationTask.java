package com.example.michel.mycalendar2.app_async_tasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;
import com.example.michel.mycalendar2.utils.AlarmReceiver;

import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

public class MeasurementNotificationsCreationTask extends AsyncTask<Context, Void, Void> {
    private int type;

    public MeasurementNotificationsCreationTask(int type){
        super();
        this.type = type;
    }

    public MeasurementNotificationsCreationTask(){
        super();
        this.type = 0;
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AlarmManager alarmManager = (AlarmManager) contexts[0].getSystemService(ALARM_SERVICE);
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        Calendar cal = Calendar.getInstance();

        List<MeasurementReminderEntry> todayMeasurementReminderEntries = databaseAdapter.getMeasurementReminderEntriesByDate(new DateData(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
        ));


        cal.add(Calendar.DAY_OF_MONTH, -1);
        List<MeasurementReminderEntry> yesterdayMeasurementReminderEntries = databaseAdapter.getMeasurementReminderEntriesByDate(new DateData(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
        ));


        cal.add(Calendar.DAY_OF_MONTH,2);
        List<MeasurementReminderEntry> tomorrowMeasurementReminderEntries = databaseAdapter.getMeasurementReminderEntriesByDate(new DateData(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
        ));


        switch (type){
            case 0:
                cancelAlarms(contexts[0], todayMeasurementReminderEntries, alarmManager);
                setupAlarms(contexts[0], todayMeasurementReminderEntries, alarmManager);

                cancelAlarms(contexts[0], yesterdayMeasurementReminderEntries, alarmManager);

                cancelAlarms(contexts[0], tomorrowMeasurementReminderEntries, alarmManager);
                setupAlarms(contexts[0], tomorrowMeasurementReminderEntries, alarmManager);
                break;
            case 1:
                cancelAlarms(contexts[0], todayMeasurementReminderEntries, alarmManager);
                cancelAlarms(contexts[0], yesterdayMeasurementReminderEntries, alarmManager);
                cancelAlarms(contexts[0], tomorrowMeasurementReminderEntries, alarmManager);
                break;
            case 2:
                setupAlarms(contexts[0], todayMeasurementReminderEntries, alarmManager);
                setupAlarms(contexts[0], tomorrowMeasurementReminderEntries, alarmManager);
                break;
        }

        databaseAdapter.close();
        return null;
    }

    private void setupAlarms(Context context, List<MeasurementReminderEntry> measurementReminderEntry, AlarmManager alarmManager){
        for (MeasurementReminderEntry mre: measurementReminderEntry) {
            if (mre.getIsDone()==0&&!mre.isLate()){
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                myIntent.putExtra("mre", mre);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, mre.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                // set alarm time
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        mre.getDate().getTime(), pendingIntent);
            }
        }
    }

    private void cancelAlarms(Context context, List<MeasurementReminderEntry> measurementReminderEntry, AlarmManager alarmManager){
        for (MeasurementReminderEntry mre: measurementReminderEntry) {
            if (mre.getIsDone()==0&&!mre.isLate()){
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, mre.getId(), myIntent, 0);
                // set alarm time
                try {
                    alarmManager.cancel(pendingIntent);
                } catch (Exception e) {
                    Log.e(TAG, "AlarmManager update was not canceled. " + e.toString());
                }
            }
        }
    }
}
