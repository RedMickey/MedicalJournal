package com.example.michel.mycalendar2.app_async_tasks;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;
import com.example.michel.mycalendar2.utils.AlarmReceiver;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;
import static android.content.Context.ALARM_SERVICE;

public class NotificationsCreationTask extends AsyncTask<Context, Void, Void> {
    private int type;

    public NotificationsCreationTask(int type){
        super();
        this.type = type;
    }

    public NotificationsCreationTask(){
        super();
        this.type = 0;
    }

    @Override
    protected Void doInBackground(Context... Contexts) {
        AlarmManager alarmManager = (AlarmManager) Contexts[0].getSystemService(ALARM_SERVICE);
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        Calendar cal = Calendar.getInstance();

        List<PillReminderEntry> todayPillReminderEntries = databaseAdapter.getPillReminderEntriesByDate(new DateData(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
        ));


        cal.add(Calendar.DAY_OF_MONTH, -1);
        List<PillReminderEntry> yesterdayPillReminderEntries = databaseAdapter.getPillReminderEntriesByDate(new DateData(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
        ));


        cal.add(Calendar.DAY_OF_MONTH,2);
        List<PillReminderEntry> tomorrowPillReminderEntries = databaseAdapter.getPillReminderEntriesByDate(new DateData(
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
        ));


        switch (type){
            case 0:
                cancelAlarms(Contexts[0], todayPillReminderEntries, alarmManager);
                setupAlarms(Contexts[0], todayPillReminderEntries, alarmManager);

                cancelAlarms(Contexts[0], yesterdayPillReminderEntries, alarmManager);

                cancelAlarms(Contexts[0], tomorrowPillReminderEntries, alarmManager);
                setupAlarms(Contexts[0], tomorrowPillReminderEntries, alarmManager);
                break;
            case 1:
                cancelAlarms(Contexts[0], todayPillReminderEntries, alarmManager);
                cancelAlarms(Contexts[0], yesterdayPillReminderEntries, alarmManager);
                cancelAlarms(Contexts[0], tomorrowPillReminderEntries, alarmManager);
                break;
            case 2:
                setupAlarms(Contexts[0], todayPillReminderEntries, alarmManager);
                setupAlarms(Contexts[0], tomorrowPillReminderEntries, alarmManager);
                break;
        }

        databaseAdapter.close();
        return null;
    }

    private void setupAlarms(Context context, List<PillReminderEntry> pillReminderEntries, AlarmManager alarmManager){
        for (PillReminderEntry pre: pillReminderEntries) {
            if (pre.getIsDone()==0&&!pre.isLate()){
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm");
                myIntent.putExtra("time", simpleDate.format(pre.getDate()));
                myIntent.putExtra("pre", pre);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, pre.getId(), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                // set alarm time
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        pre.getDate().getTime(), pendingIntent);
            }
        }
    }

    private void cancelAlarms(Context context, List<PillReminderEntry> pillReminderEntries, AlarmManager alarmManager){
        for (PillReminderEntry pre: pillReminderEntries) {
            if (pre.getIsDone()==0&&!pre.isLate()){
                Intent myIntent = new Intent(context, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context, pre.getId(), myIntent, 0);
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
