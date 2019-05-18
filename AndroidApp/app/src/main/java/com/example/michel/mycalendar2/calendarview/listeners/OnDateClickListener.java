package com.example.michel.mycalendar2.calendarview.listeners;

import android.view.View;

import com.example.michel.mycalendar2.calendarview.data.DateData;

/**
 * Created by bob.sun on 15/8/28.
 */
public abstract class OnDateClickListener {
    public static OnDateClickListener instance;

    public abstract void onDateClick(View view,DateData date);
}
