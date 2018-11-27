package com.example.michel.mycalendar2.models;

import java.util.Calendar;
import java.util.Date;

public class ReminderEntryModel extends ReminderModel {
    protected int isDone;
    protected Date date;
    protected Date havingMealsTime;
    protected boolean isLate;

    public ReminderEntryModel(int id, int havingMealsType, Date date,
                              Date havingMealsTime, int isDone, boolean isLate)
    {
        super(id, havingMealsType);
        this.date = date;
        this.havingMealsTime = havingMealsTime;
        this.isDone = isDone;
        this.isLate=isLate;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getHavingMealsTime() {
        return havingMealsTime;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }

    public boolean isLateCheck(){
        Calendar calendar = Calendar.getInstance();
        isLate =  calendar.getTime().compareTo(date)>0?true:false;
        return isLate;
    }
}
