package com.example.michel.mycalendar2.models;

import java.util.Calendar;
import java.util.Date;

public class PillReminderEntry extends PillModel {
    private int isDone;
    private Date date;
    private Date havingMealsTime;
    private boolean isLate;

    public PillReminderEntry(int id, String pillName, int pillCount, String pillCountType,
                             Date date, int havingMealsType, Date havingMealsTime, int isDone, boolean isLate)
    {
        super(id, pillName, pillCount, pillCountType, havingMealsType);
        this.date = date;
        this.havingMealsTime = havingMealsTime;
        this.isDone = isDone;
        this.isLate=isLate;
    }

    public boolean isLateCheck(){
        Calendar calendar = Calendar.getInstance();
        isLate =  calendar.getTime().compareTo(date)>0?true:false;
        return isLate;
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
}