package com.example.michel.mycalendar2.calendarview.utils;

import java.util.Calendar;
import java.util.List;

import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.data.DayDifference;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;

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

    public static String getRusMonthName(int month, int type){
        String monthName = "";
        switch (month){
            case 1:
                switch (type){
                    case 0:
                        monthName = "янв.";
                        break;
                    case 1:
                        monthName = "января";
                        break;
                }
                break;
            case 2:
                switch (type){
                    case 0:
                        monthName = "Февр.";
                        break;
                    case 1:
                        monthName = "февраля";
                        break;
                }
                break;
            case 3:
                monthName = "марта";
                break;
            case 4:
                switch (type){
                    case 0:
                        monthName = "апр.";
                        break;
                    case 1:
                        monthName = "апреля";
                        break;
                }
                break;
            case 5:
                monthName = "мая";
                break;
            case 6:
                monthName = "июня";
                break;
            case 7:
                monthName = "июля";
                break;
            case 8:
                switch (type){
                    case 0:
                        monthName = "авг.";
                        break;
                    case 1:
                        monthName = "августа";
                        break;
                }
                break;
            case 9:
                switch (type){
                    case 0:
                        monthName = "сент.";
                        break;
                    case 1:
                        monthName = "сентября";
                        break;
                }
                break;
            case 10:
                switch (type){
                    case 0:
                        monthName = "окт.";
                        break;
                    case 1:
                        monthName = "октября";
                        break;
                }
                break;
            case 11:
                switch (type){
                    case 0:
                        monthName = "нояб.";
                        break;
                    case 1:
                        monthName = "ноября";
                        break;
                }
                break;
            case 12:
                switch (type){
                    case 0:
                        monthName = "дек.";
                        break;
                    case 1:
                        monthName = "декабря";
                        break;
                }
                break;
        }
        return monthName;
    }

    public static String getDayOfWeekRusName(DateData dateData){
        Calendar cal = Calendar.getInstance();
        cal.set(dateData.getYear(), dateData.getMonth()-1, dateData.getDay());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        String dayName="";
        switch (dayOfWeek){
            case 2:
                dayName = "понедельник";
                break;
            case 3:
                dayName = "вторник";
                break;
            case 4:
                dayName = "среда";
                break;
            case 5:
                dayName = "четверг";
                break;
            case 6:
                dayName = "пятница";
                break;
            case 7:
                dayName = "суббота";
                break;
            case 1:
                dayName = "воскресение";
                break;
        }
        return dayName;
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
    public static DayDifference calculateDaysBetweenDates(DateData curDate, DateData date2)
    {
        Calendar firstDate = Calendar.getInstance();
        firstDate.set(Calendar.DAY_OF_MONTH, curDate.getDay());
        firstDate.set(Calendar.MONTH, curDate.getMonth()-1); // 0-11 so 1 less
        firstDate.set(Calendar.YEAR, curDate.getYear());

        Calendar secondDate = Calendar.getInstance();
        secondDate.set(Calendar.DAY_OF_MONTH,date2.getDay());
        secondDate.set(Calendar.MONTH,date2.getMonth()-1); // 0-11 so 1 less
        secondDate.set(Calendar.YEAR, date2.getYear());

        int difference = (int)((firstDate.getTimeInMillis() - secondDate.getTimeInMillis())/(24 * 60 * 60 * 1000));
        return new DayDifference(Math.abs(difference), difference<0?true:false);
    }

    //method for testing
    public static int[] calculateRemainingReminders(List<PillReminderEntry> pillReminderEntries, int fullCount){
        int[] countOfRemainingReminders = new int[]{0,0,0,0};
        //countOfRemainingReminders[0] - count of morning taking
        //countOfRemainingReminders[1] - count of day taking
        //countOfRemainingReminders[2] - count of evening taking
        //countOfRemainingReminders[3] - count of Remaining Reminders
        Calendar calendar = Calendar.getInstance();
        for (PillReminderEntry pre:pillReminderEntries) {
            calendar.setTime(pre.getDate());
            int curHour = calendar.get(Calendar.HOUR_OF_DAY);

            if(curHour>=4&&curHour<12){
                countOfRemainingReminders[0]++;
            }
            if(curHour>=12&&curHour<18){
                countOfRemainingReminders[1]++;
            }
            if((curHour>=18&&curHour<=23)||(curHour>=0&&curHour<4)){
                countOfRemainingReminders[2]++;
            }
            countOfRemainingReminders[3]++;
        }
        countOfRemainingReminders[3]=fullCount-countOfRemainingReminders[3];
        return countOfRemainingReminders;
    }
}
