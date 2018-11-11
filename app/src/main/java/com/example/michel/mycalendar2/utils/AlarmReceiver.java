package com.example.michel.mycalendar2.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;

import com.example.michel.mycalendar2.activities.AlarmActivity;
import com.example.michel.mycalendar2.models.PillReminder;
import com.example.michel.mycalendar2.models.PillReminderEntry;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //AlarmActivity alarmActivity = new AlarmActivity();

        //Intent intent2 = new Intent(context,  AlarmActivity.class);

        //context.startActivity(intent2);
        Bundle extras= intent.getExtras();

        Intent intAlarm = new Intent(context.getApplicationContext(), AlarmActivity.class);
        intAlarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intAlarm.putExtra("pre", intent.getParcelableExtra("pre"));
        context.startActivity(intAlarm);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        setResultCode(Activity.RESULT_OK);
        wl.release();
    }
}
