package com.example.michel.mycalendar2.view.custom_views.calendarview.listeners;

import android.view.View;

import com.example.michel.mycalendar2.view.custom_views.calendarview.data.DateData;

/**
 * Created by bob.sun on 15/8/28.
 */
public abstract class OnDateClickListener {
    public static OnDateClickListener instance;

    public abstract void onDateClick(View view,DateData date);
}
