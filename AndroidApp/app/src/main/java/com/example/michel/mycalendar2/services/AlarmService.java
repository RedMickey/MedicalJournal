package com.example.michel.mycalendar2.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.AddTreatmentActivity;
import com.example.michel.mycalendar2.activities.MainActivity;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.utils.AlarmReceiver;

import java.util.UUID;

public class AlarmService extends Service {
    AlarmReceiver alarmReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int isCancel = intent.getIntExtra("isCancel",1);
        int notifId = intent.getIntExtra("notifId",0);

        String pillReminderEntryID = intent.getStringExtra("pillReminderEntryID");
        String measurementReminderEntryID =  intent.getStringExtra("measurementReminderEntryID");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.cancel(notifId);

        if (isCancel==0){

            //Toast.makeText(this,"isCancel==0", Toast.LENGTH_SHORT).show();

            if (!pillReminderEntryID.equals("")){
                DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                PillReminderDao pillReminderDao = new PillReminderDao(databaseAdapter.open().getDatabase());
                pillReminderDao.updateIsDonePillReminderEntry( 1, UUID.fromString(pillReminderEntryID), "");
                databaseAdapter.close();
            }

            if (!measurementReminderEntryID.equals("")){
                Intent i = new Intent(Intent.ACTION_MAIN);
                i.setComponent(new ComponentName(this, MainActivity.class));
                i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                        Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(i);
            }

        }
        return START_STICKY;
    }
}
