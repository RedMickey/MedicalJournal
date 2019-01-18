package com.example.michel.mycalendar2.models;

import android.support.annotation.Nullable;

import com.example.michel.mycalendar2.calendarview.data.DateData;

public class ReminderDBEntry {
    protected DateData startDate;
    protected Integer idCycle;
    protected Integer idHavingMealsType;
    protected Integer havingMealsTime;
    protected String annotation;
    protected Integer isActive;
    protected ReminderTime[] reminderTimes;

    public ReminderDBEntry(DateData startDate, Integer idCycle, @Nullable Integer idHavingMealsType,
                                     @Nullable Integer havingMealsTime, String annotation, Integer isActive,
                                     ReminderTime[] reminderTimes)
    {
        this.startDate=startDate;
        this.idCycle=idCycle;
        this.idHavingMealsType=idHavingMealsType;
        this.havingMealsTime=havingMealsTime;
        this.annotation=annotation;
        this.isActive=isActive;
        this.reminderTimes = reminderTimes;
    }

    public ReminderDBEntry()
    {
        this.startDate=null;
        this.idCycle=null;
        this.idHavingMealsType=null;
        this.havingMealsTime=null;
        this.annotation=null;
        this.isActive=null;
        this.reminderTimes=null;
    }

    public DateData getStartDate() {
        return startDate;
    }

    public void setStartDate(DateData startDate) {
        this.startDate = startDate;
    }

    public Integer getIdCycle() {
        return idCycle;
    }

    public void setIdCycle(Integer idCycle) {
        this.idCycle = idCycle;
    }

    public Integer getIdHavingMealsType() {
        return idHavingMealsType;
    }

    public void setIdHavingMealsType(Integer idHavingMealsType) {
        this.idHavingMealsType = idHavingMealsType;
    }

    public Integer getHavingMealsTime() {
        return havingMealsTime;
    }

    public void setHavingMealsTime(Integer havingMealsTime) {
        this.havingMealsTime = havingMealsTime;
    }

    public String getAnnotation() {
        return annotation;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public ReminderTime[] getReminderTimes() {
        return reminderTimes;
    }

    public void setReminderTimes(ReminderTime[] reminderTimes) {
        this.reminderTimes = reminderTimes;
    }
}
