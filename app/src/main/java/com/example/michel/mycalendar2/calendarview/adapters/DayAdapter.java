package com.example.michel.mycalendar2.calendarview.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.views.DayFragment;

public class DayAdapter extends FragmentStatePagerAdapter{
    public DayAdapter(FragmentManager fm) {
        super(fm);
    }

    private DateData currentDate;

    public void setCurrentDate(DateData currentDate){
        this.currentDate = currentDate;
    }

    @Override
    public Fragment getItem(int position) {
        DayFragment fragment = new DayFragment();
        fragment.setData(currentDate);
        return fragment;
    }

    @Override
    public int getCount() {
        return 7000;
    }
}
