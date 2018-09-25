package com.example.michel.mycalendar2.calendarview.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;

import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.views.DayFragment;

public class DayAdapter extends FragmentStatePagerAdapter{
    public DayAdapter(FragmentManager fm) {
        super(fm);
    }

    private DateData currentDate;
    private DateData preDate;
    private int i=0;

    public void setCurrentDate(DateData currentDate){
        this.currentDate = currentDate;
    }

    public void setPreDate(DateData preDate) {
        this.preDate = preDate;
    }

    public void setInitCurrentDate(DateData currentDate){
        this.currentDate = new DateData(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay());
        //this.currentDate.setDay(this.currentDate.getDay()-1);
        Log.i("DateN", this.currentDate.getDayString());
    }

    @Override
    public Fragment getItem(int position) {
        int dayDelta = currentDate.getDay() -preDate.getDay();
        switch (i){
            case 0:
                currentDate = new DateData(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay());
                i++;
                break;
            case 1:
                currentDate = new DateData(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay()-1);
                i++;
                break;
            case 2:
                currentDate = new DateData(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay()+2);
                i++;
                break;

        }
        DayFragment fragment = DayFragment.newInstance(currentDate, dayDelta);
        preDate = currentDate;
        return fragment;
    }

    @Override
    public int getCount() {
        return 7000;
    }

}


