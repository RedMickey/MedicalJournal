package com.example.michel.mycalendar2.calendarview.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.View;

import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.calendarview.views.DayFragment;

import java.util.Calendar;

public class DayAdapter extends FragmentStatePagerAdapter{
    public DayAdapter(FragmentManager fm) {
        super(fm);
        calendar = Calendar.getInstance();
    }

    private DateData currentDate;
    private DateData preDate;
    private int i=0;
    private boolean isDayClicked = false;
    private DateData dayClickedDate;
    private Calendar calendar;

    public void setDayClicked(boolean dayClicked) {
        isDayClicked = dayClicked;
    }

    public void setDayClickedDate(DateData dayClickedDate) {
        this.dayClickedDate = dayClickedDate;
    }

    public boolean isDayClicked() {
        return isDayClicked;
    }

    public DateData getDayClickedDate() {
        return dayClickedDate;
    }

    public void setCurrentDate(DateData currentDate){
        this.currentDate = currentDate;
    }

    public DateData getCurrentDate() {
        /*int newYear, newMonth, newDay;

        if(currentDate.getDay()== CalendarUtil.getDaysInMonth(currentDate.getMonth()-1,currentDate.getYear()))
        {
            newDay = 1;
            if (dateBuff.getMonth()==12)
            {
                newMonth = 1;
                newYear = dateBuff.getYear()+1;
            }
            else {wMonth = dateBuff
                ne.getMonth() + 1;
                newYear = dateBuff.getYear();
            }
        */
        return currentDate;
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
        switch (i){
            case 0:
                currentDate = new DateData(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay());
                i++;
                break;
            case 1:
                calendar.set(currentDate.getYear(), currentDate.getMonth()-1, currentDate.getDay());
                calendar.add(Calendar.DAY_OF_MONTH, -1);
                currentDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
                i++;
                break;
            case 2:
                calendar.set(currentDate.getYear(), currentDate.getMonth()-1, currentDate.getDay());
                calendar.add(Calendar.DAY_OF_MONTH, 2);
                currentDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH));
                i++;
                break;

        }
        DayFragment fragment = DayFragment.newInstance(currentDate);
        preDate = currentDate;
        if (i==3){
            currentDate = new DateData(currentDate.getYear(), currentDate.getMonth(), currentDate.getDay()-1);
        }
        return fragment;
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //return super.getItemPosition(object);
        return POSITION_NONE;
    }

    public void cleanI(){
        i=0;
    }

    @Override
    public int getCount() {
        return 7000;
    }

}


