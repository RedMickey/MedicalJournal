package com.example.michel.mycalendar2.calendarview.utils;

import java.util.Calendar;

import com.example.michel.mycalendar2.calendarview.data.DateData;

public class CalendarUtil {
    public static DateData date = CurrentCalendar.getCurrentDateData();

    public static int position2Year(int pos){
        int tmpYear,tmpMonth;
        Calendar c = Calendar.getInstance();
//        tmpYear = c.get(Calendar.YEAR);
        // TODO: 15/12/10 Maybe using current year is a mistake.
        tmpYear = date.getYear();
        tmpMonth = CalendarUtil.position2Month(pos);
        int ret;
        if(pos == 500){
            return tmpYear;
        }
        if(pos > 500){

            ret = tmpYear + ((pos - 500) + c.get(Calendar.MONTH))/12;
//            ret = tmpYear + ((pos - 500) + date.getMonth() - 1)/12;//oong

        }else{
            ret =  tmpYear - ((500 - pos)+tmpMonth - 1)/12;
//            ret = tmpYear - ((500-pos)/12);   //oong
        }
        return ret;
    }

    public static int position2Month(int pos){
        int tmpMonth;
        Calendar c = Calendar.getInstance();
//        tmpMonth = c.get(Calendar.MONTH) + 1;
        tmpMonth = date.getMonth();
        int ret;
        if(pos == 500){
            return tmpMonth;
        }
        if(pos > 500){
            ret = (tmpMonth + (pos - 500)%12) % 12;
        }else{
            ret = (tmpMonth - (500 - pos)%12) % 12;
            ret = ret<0?12+ret:ret;
        }
        return ret==0?12:ret;
    }

    public static int getWeekCount(int position){
        Calendar calendar = Calendar.getInstance();
        calendar.set(position2Year(position), position2Month(position) - 1, 1) ;
        int start = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        int ret =  calendar.getActualMaximum(Calendar.DAY_OF_MONTH) + start;
        int more = ret % 7 == 0 ? 0 : 1;
        ret = ret / 7 + more;
        calendar = null;
        return ret;
    }

    public static int getDaysInMonth(int month, int year) {
        switch (month) {
            case Calendar.JANUARY:
            case Calendar.MARCH:
            case Calendar.MAY:
            case Calendar.JULY:
            case Calendar.AUGUST:
            case Calendar.OCTOBER:
            case Calendar.DECEMBER:
                return 31;
            case Calendar.APRIL:
            case Calendar.JUNE:
            case Calendar.SEPTEMBER:
            case Calendar.NOVEMBER:
                return 30;
            case Calendar.FEBRUARY:
                return (year % 4 == 0) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid Month");
        }
    }

    public static String getDateString(int year, int month){
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
}
