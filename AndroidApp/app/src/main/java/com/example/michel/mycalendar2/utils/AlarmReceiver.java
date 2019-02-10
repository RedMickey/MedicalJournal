package com.example.michel.mycalendar2.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;

import com.example.michel.mycalendar2.activities.AddTreatmentActivity;
import com.example.michel.mycalendar2.activities.AlarmActivity;
import com.example.michel.mycalendar2.activities.MainActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;
import com.example.michel.mycalendar2.services.AlarmService;

import java.text.SimpleDateFormat;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //AlarmActivity alarmActivity = new AlarmActivity();

        //Intent intent2 = new Intent(context,  AlarmActivity.class);

        //context.startActivity(intent2);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        //Bundle extras= intent.getExtras();

        /*Intent intAlarm = new Intent(context.getApplicationContext(), AlarmActivity.class);
        intAlarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intAlarm.putExtra("pre", intent.getParcelableExtra("pre"));
        intAlarm.putExtra("mre", intent.getParcelableExtra("mre"));
        context.startActivity(intAlarm);*/
        /*
        Intent intent2 = new Intent(new Intent(context, AlarmService.class));
        intent2.putExtra("isActual", 1);
        context.startService(intent2);
        */

        PillReminderEntry pre =  intent.getParcelableExtra("pre");
        MeasurementReminderEntry mre = intent.getParcelableExtra("mre");

        String pillReminderEntryID = pre==null?"":pre.getId().toString();
        String measurementReminderEntryID = mre==null?"":mre.getId().toString();

        Intent cancelIntent = new Intent(context, AlarmService.class);
        //notificationIntent2.putExtra("isActual", 1);
        cancelIntent.putExtra("notifId", 0);
        cancelIntent.putExtra("isCancel", 1);
        PendingIntent cancelPendingIntent = PendingIntent.getService(context,
                10000, cancelIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Intent acceptIntent = new Intent(context, AlarmService.class);
        //notificationIntent2.putExtra("isActual", 1);
        acceptIntent.putExtra("notifId", 0);
        acceptIntent.putExtra("isCancel", 0);
        acceptIntent.putExtra("pillReminderEntryID", pillReminderEntryID);
        acceptIntent.putExtra("measurementReminderEntryID", measurementReminderEntryID);
        PendingIntent acceptPendingIntent = PendingIntent.getService(context,
                10001, acceptIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.addFlags()
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder  builder = new Notification.Builder(context);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, d MMM");

        if (!pillReminderEntryID.equals("")){
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(sdf.format(pre.getDate()) + " " + pre.getPillName())
                    .setContentText("Не забудьте принять таблетки!")
                    .addAction(R.drawable.ic_confirm, "Принять", acceptPendingIntent)
                    .addAction(R.drawable.ic_clear, "Пропустить", cancelPendingIntent);
        }
        else {
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(sdf.format(mre.getDate()) + " " + mre.getMeasurementTypeName())
                    .setContentText("Не забудьте произвести измерение!")
                    .addAction(R.drawable.ic_confirm, "Принять", acceptPendingIntent)
                    .addAction(R.drawable.ic_clear, "Пропустить", cancelPendingIntent);
        }

        Notification nBuilder = builder.build();

        nBuilder.flags|= Notification.FLAG_AUTO_CANCEL;

        nBuilder.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, nBuilder);

        //setResultCode(Activity.RESULT_OK);
        wl.release();
    }
}
