package com.example.michel.mycalendar2.calendarview.listeners;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;

/**
 * Created by bob.sun on 15/8/28.
 */
public abstract class OnMonthChangeListener implements ViewPager.OnPageChangeListener {
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//        Log.wtf("OnPageScrolled:", ""+position);
    }

    @Override
    public void onPageSelected(int position) {
//        Log.wtf("OnPageSelected:", ""+position);
        onMonthChange(CalendarUtil.position2Year(position), CalendarUtil.position2Month(position));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public abstract void onMonthChange(int year, int month);
}
