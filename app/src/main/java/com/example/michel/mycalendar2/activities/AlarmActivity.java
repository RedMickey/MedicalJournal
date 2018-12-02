package com.example.michel.mycalendar2.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.additional_views.swipe_button.OnStateChangeListener;
import com.example.michel.mycalendar2.additional_views.swipe_button.SwipeButton;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;

import java.text.SimpleDateFormat;

public class AlarmActivity extends AppCompatActivity {
private int reminderEntryID;
private int reminderType;
private SwipeButton swipeButton;
private Ringtone ringtone;
private TextView alarmResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Bundle bundle = getIntent().getExtras();
        PillReminderEntry pre = (PillReminderEntry) bundle.getParcelable("pre");
        MeasurementReminderEntry mre = (MeasurementReminderEntry) bundle.getParcelable("mre");
        if (pre == null)
        {
            reminderEntryID = mre.getId();
            reminderType = 2;
        }
        else
        {
            reminderEntryID = pre.getId();
            reminderType = 1;
        }

        swipeButton = (SwipeButton) findViewById(R.id.swipe_btn);
        alarmResult = (TextView) findViewById(R.id.alarm_result);

        swipeButton.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(int state) {
                int colorFrom = getResources().getColor(R.color.alarm_act_bg);
                if (state!=0)
                    ringtone.stop();
                if (state==2){
                    alarmResult.setText("Принято");

                    if (reminderType == 1)
                    {
                        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                        databaseAdapter.open();
                        databaseAdapter.updateIsDonePillReminderEntry( 1, reminderEntryID, "");
                        databaseAdapter.close();
                    }

                    int colorTo = getResources().getColor(R.color.alarm_act_bg_acc);
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                    colorAnimation.setDuration(1050); // milliseconds
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            ((RelativeLayout) findViewById(R.id.layout_alarm)).
                                    setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });
                    colorAnimation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            if (reminderType == 2){
                                Intent i = new Intent(getApplicationContext(), MainActivity.class); // Your list's Intent
                                //i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // Adds the FLAG_ACTIVITY_NO_HISTORY flag
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            }
                            finishAndRemoveTask();
                            /*else {
                                finish();
                            }*/

                        }
                    });
                    colorAnimation.start();
                }
                if (state==1){
                    alarmResult.setText("Отменено");
                    int colorTo = getResources().getColor(R.color.alarm_act_bg_rej);
                    ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), colorFrom, colorTo);
                    colorAnimation.setDuration(1050); // milliseconds
                    colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                        @Override
                        public void onAnimationUpdate(ValueAnimator animator) {
                            ((RelativeLayout) findViewById(R.id.layout_alarm)).
                                    setBackgroundColor((int) animator.getAnimatedValue());
                        }

                    });
                    colorAnimation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            finish();
                        }
                    });
                    colorAnimation.start();
                }
            }
        });

        SimpleDateFormat simpleDate = new SimpleDateFormat("HH:mm");
        if (reminderType == 1)
        {
            ((TextView) findViewById(R.id.reminder_time_alarm)).setText(
                    simpleDate.format(pre.getDate())
            );
            ((TextView) findViewById(R.id.reminder_name_alarm)).setText(pre.getPillName());
            ((TextView) findViewById(R.id.pill_count_and_type_alarm)).setText(
                    String.valueOf(pre.getPillCount())+" "+
                            pre.getPillCountType()
            );
        }
        else {
            ((TextView) findViewById(R.id.reminder_time_alarm)).setText(
                    simpleDate.format(mre.getDate())
            );
            ((TextView) findViewById(R.id.reminder_name_alarm)).setText(mre.getMeasurementTypeName());
            /*((TextView) findViewById(R.id.pill_count_and_type_alarm)).setText(
                    String.valueOf(pre.getPillCount())+" "+
                            pre.getPillCountType()
            );*/
        }


        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();
    }

    @Override
    public void finishActivity(int requestCode) {
        super.finishActivity(requestCode);
    }
}
