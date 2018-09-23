package com.example.michel.mycalendar2.calendarview.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.example.michel.mycalendar2.calendarview.adapters.DayAdapter;

public class WeekDayViewPager extends ViewPager{

    private ExpCalendarView expCalendarView;

    public WeekDayViewPager(@NonNull Context context) {
        super(context);
        if (context instanceof FragmentActivity) {
            init((FragmentActivity) context);
        }
    }

    public WeekDayViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof FragmentActivity) {
            init((FragmentActivity) context);
        }
    }

    public void init(FragmentActivity activity){
        setAdapter(new DayAdapter(activity.getSupportFragmentManager()));
        this.setCurrentItem(3500);
    }

    public void setExpCalendarView(ExpCalendarView expCalendarView) {
        this.expCalendarView = expCalendarView;
    }
}
