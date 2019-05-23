package com.example.michel.mycalendar2.controllers.app_async_tasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.michel.mycalendar2.services.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.view.custom_views.calendarview.data.DateData;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.services.AlarmReceiver;

import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

public class MeasurementNotificationsCreationTask extends AsyncTask<Context, Void, Void> {
    private int type;
    private int userId;

    public MeasurementNotificationsCreationTask(int type){
        super();
        this.type = type;
        this.userId = AccountGeneralUtils.curUser.getId();
    }

    public MeasurementNotificationsCreationTask(int type, int userId){
        super();
        this.type = type;
        this.userId = userId;
    }

    public MeasurementNotificationsCreationTask(){
        super();
        this.type = 0;
        this.userId = AccountGeneralUtils.curUser.getId();
    }

    @Override
    protected Void doInBackground(Context... contexts) {
        AlarmManager alarmManager = (AlarmManager) contexts[0].getSystemService(ALARM_SERVICE);
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.open().getDatabase());
        Calendar cal = Calendar.getInstance();

        List<MeasurementReminderEntry> todayMeasurementReminderEntries = measurementReminderDao.getMeasurementReminderEntriesByDate(new DateData(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)), userId
        );


        cal.add(Calendar.DAY_OF_MONTH, -1);
        List<MeasurementReminderEntry> yesterdayMeasurementReminderEntries = measurementReminderDao.getMeasurementReminderEntriesByDate(new DateData(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)), userId
        );


        cal.add(Calendar.DAY_OF_MONTH,2);
        List<MeasurementReminderEntry> tomorrowMeasurementReminderEntries = measurementReminderDao.getMeasurementReminderEntriesByDate(new DateData(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)), userId
        );


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

    public static void setupAlarms(Context context, List<MeasurementReminderEntry> measurementReminderEntry, AlarmManager alarmManager){
        for (MeasurementReminderEntry mre: measurementReminderEntry) {
            if (mre.getIsDone()==0&&!mre.isLate()){
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                myIntent.putExtra("mre", mre);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, (int)mre.getId().getMostSignificantBits(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                // set alarm time
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        mre.getDate().getTime(), pendingIntent);
            }
        }
    }

    public static void cancelAlarms(Context context, List<MeasurementReminderEntry> measurementReminderEntry, AlarmManager alarmManager){
        for (MeasurementReminderEntry mre: measurementReminderEntry) {
            if (mre.getIsDone()==0&&!mre.isLate()){
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, (int) mre.getId().getMostSignificantBits(), myIntent, 0);
                // set alarm time
                try {
                    alarmManager.cancel(pendingIntent);
                } catch (Exception e) {
                    Log.e(TAG, "AlarmManager update was not canceled. " + e.toString());
                }
            }
        }
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
