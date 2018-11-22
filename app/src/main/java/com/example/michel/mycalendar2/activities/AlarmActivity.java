package com.example.michel.mycalendar2.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
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
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;

import java.text.SimpleDateFormat;

public class AlarmActivity extends AppCompatActivity {
private int pillReminderEntryID;
private SwipeButton swipeButton;
private Ringtone ringtone;
private TextView alarmResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Bundle bundle = getIntent().getExtras();
        PillReminderEntry pre = (PillReminderEntry) bundle.getParcelable("pre");
        pillReminderEntryID = pre.getId();

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
                    DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                    databaseAdapter.open();
                    databaseAdapter.updateIsDonePillReminderEntry( 1, pillReminderEntryID, "");
                    databaseAdapter.close();

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
                            finish();
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
        ((TextView) findViewById(R.id.reminder_time_alarm)).setText(
                simpleDate.format(pre.getDate())
        );
        ((TextView) findViewById(R.id.pill_name_alarm)).setText(pre.getPillName());
        ((TextView) findViewById(R.id.pill_count_and_type_alarm)).setText(
                String.valueOf(pre.getPillCount())+" "+
                        pre.getPillCountType()
        );

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();
    }
}
