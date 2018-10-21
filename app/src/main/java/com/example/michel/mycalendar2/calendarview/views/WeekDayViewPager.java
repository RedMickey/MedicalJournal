package com.example.michel.mycalendar2.calendarview.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.example.michel.mycalendar2.calendarview.adapters.DayAdapter;

public class WeekDayViewPager extends ViewPager{

    private ExpCalendarView expCalendarView;
    private DayAdapter dayAdapter;
    public int LastPage;

    public WeekDayViewPager(@NonNull Context context) {
        super(context);
     /*   if (context instanceof FragmentActivity) {
            init((FragmentActivity) context);
        }*/
    }

    public WeekDayViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
      /*  if (context instanceof FragmentActivity) {
            init((FragmentActivity) context);
        }*/
    }

    public void init(ExpCalendarView expCalendarView){

        //dayAdapter = new DayAdapter(((FragmentActivity)getContext()).getSupportFragmentManager());
        this.expCalendarView = expCalendarView;
        dayAdapter.setInitCurrentDate(this.expCalendarView.getMarkedDates().getAll().get(0));
        dayAdapter.setPreDate(this.expCalendarView.getMarkedDates().getAll().get(0));
        setAdapter(dayAdapter);

        this.setCurrentItem(3500);
        LastPage = 3500;
    }

    public void setDayAdapter(DayAdapter dayAdapter) {
        this.dayAdapter = dayAdapter;
    }

    public void setExpCalendarView(ExpCalendarView expCalendarView) {
        this.expCalendarView = expCalendarView;
    }
}
