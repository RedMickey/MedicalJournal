package com.example.michel.mycalendar2.calendarview.utils;

import java.util.Calendar;

import com.example.michel.mycalendar2.calendarview.data.DateData;

public class CurrentCalendar {
    public static DateData getCurrentDateData(){
        Calendar calendar = Calendar.getInstance();
        return new DateData(calendar.get(calendar.YEAR), calendar.get(calendar.MONTH) + 1, calendar.get(calendar.DAY_OF_MONTH));
    }

}
