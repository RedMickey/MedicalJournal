package com.example.michel.mycalendar2.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

import com.example.michel.mycalendar2.calendarview.CellConfig;
import com.example.michel.mycalendar2.calendarview.listeners.OnDateClickListener;
import com.example.michel.mycalendar2.calendarview.listeners.OnExpDateClickListener;
import com.example.michel.mycalendar2.calendarview.listeners.OnMonthScrollListener;
import com.example.michel.mycalendar2.calendarview.views.ExpCalendarView;
import com.example.michel.mycalendar2.calendarview.data.DateData;

public class ExpMainActivity extends AppCompatActivity {

    private TextView YearMonthTv;
    private ExpCalendarView expCalendarView;
    private DateData selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exp_main_activity);

//      Get instance.
        expCalendarView = ((ExpCalendarView) findViewById(R.id.calendar_exp));
        YearMonthTv = (TextView) findViewById(R.id.main_YYMM_Tv);
        YearMonthTv.setText(getDateString(Calendar.getInstance().get(Calendar.YEAR),(Calendar.getInstance().get(Calendar.MONTH) + 1)));

//      Set up listeners.
        expCalendarView.setOnDateClickListener(new OnExpDateClickListener()).setOnMonthScrollListener(new OnMonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month) {
                YearMonthTv.setText(getDateString(year, month));
            }

            @Override
            public void onMonthScroll(float positionOffset) {
                Log.i("listener", "onMonthScroll:" + positionOffset);
            }
        });

        expCalendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                expCalendarView.getMarkedDates().removeAdd();
                expCalendarView.markDate(date);
                selectedDate = date;
            }
        });

        //imageInit();

        Calendar calendar = Calendar.getInstance();
        selectedDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        expCalendarView.markDate(selectedDate);
    }

    private String getDateString(int year, int month){
        String monthName = "";
        switch (month){
            case 1:
                monthName = "Январь";
                break;
            case 2:
                monthName = "Февраль";
                break;
            case 3:
                monthName = "Март";
                break;
            case 4:
                monthName = "Апрель";
                break;
            case 5:
                monthName = "Май";
                break;
            case 6:
                monthName = "Июнь";
                break;
            case 7:
                monthName = "Июль";
                break;
            case 8:
                monthName = "Август";
                break;
            case 9:
                monthName = "Сентябрь";
                break;
            case 10:
                monthName = "Октябрь";
                break;
            case 11:
                monthName = "Ноябрь";
                break;
            case 12:
                monthName = "Декабрь";
                break;
        }
        return monthName + " " + String.valueOf(year);
    }

    private boolean ifExpand = true;
/*
    private void imageInit() {
        final ImageView expandIV = (ImageView) findViewById(R.id.main_expandIV);
        expandIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifExpand) {
                    CellConfig.Month2WeekPos = CellConfig.middlePosition;
                    CellConfig.ifMonth = false;
                    expandIV.setImageResource(R.mipmap.icon_arrow_down);
                    CellConfig.weekAnchorPointDate = selectedDate;
                    expCalendarView.shrink();
                } else {
                    CellConfig.Week2MonthPos = CellConfig.middlePosition;
                    CellConfig.ifMonth = true;
                    expandIV.setImageResource(R.mipmap.icon_arrow_up);
                    expCalendarView.expand();
                }
                ifExpand = !ifExpand;
            }
        });
    }
*/
    /*public void TravelToClick(View v) {
        expCalendarView.travelTo(new DateData(1980, 11, 14));
    }*/
}
