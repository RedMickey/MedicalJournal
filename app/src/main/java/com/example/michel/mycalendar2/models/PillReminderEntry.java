package com.example.michel.mycalendar2.models;

import java.util.Calendar;
import java.util.Date;

public class PillReminderEntry {
    private int id;
    private String pillName;
    private int isDone;
    private Date date;
    private int pillCount;
    private String pillCountType;
    private int havingMealsType;
    private Date havingMealsTime;
    private boolean isLate;

    public PillReminderEntry(int id, String pillName, int pillCount, String pillCountType,
                             Date date, int havingMealsType, Date havingMealsTime, int isDone, boolean isLate)
    {
        this.id = id;
        this.pillName = pillName;
        this.pillCount = pillCount;
        this.pillCountType = pillCountType;
        this.date = date;
        this.havingMealsType = havingMealsType;
        this.havingMealsTime = havingMealsTime;
        this.isDone = isDone;
        this.isLate=isLate;
    }

    public boolean isLateCheck(){
        Calendar calendar = Calendar.getInstance();
        isLate =  calendar.getTime().compareTo(date)>0?true:false;
        return isLate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPillName() {
        return pillName;
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

    public int getPillCount() {
        return pillCount;
    }

    public String getPillCountType() {
        return pillCountType;
    }

    public int getHavingMealsType() {
        return havingMealsType;
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