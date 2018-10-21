package com.example.michel.mycalendar2.models;

/*public void insertPillReminder(String pillName, int pillCount, int idPillCountType,
        String startDate, int idCycle, @Nullable int idHavingMealsType,
@Nullable String havingMealsTime, String annotation, int isActive){*/

import android.support.annotation.Nullable;

import com.example.michel.mycalendar2.calendarview.data.DateData;

public class PillReminderDBInsertEntry {
    private String pillName;
    private Integer pillCount;
    private Integer idPillCountType;
    private DateData startDate;
    private Integer idCycle;
    private Integer idHavingMealsType;
    private String havingMealsTime;
    private String annotation;
    private Integer isActive;
    private String[] reminderTimes;

    public PillReminderDBInsertEntry(String pillName, Integer pillCount, Integer idPillCountType,
                                     DateData startDate, Integer idCycle, @Nullable Integer idHavingMealsType,
                                     @Nullable String havingMealsTime, String annotation, Integer isActive)
    {
        this.pillName=pillName;
        this.pillCount=pillCount;
        this.idPillCountType=idPillCountType;
        this.startDate=startDate;
        this.idCycle=idCycle;
        this.idHavingMealsType=idHavingMealsType;
        this.havingMealsTime=havingMealsTime;
        this.annotation=annotation;
        this.isActive=isActive;
        this.reminderTimes = null;
    }

    public PillReminderDBInsertEntry()
    {
        this.pillName=null;
        this.pillCount=null;
        this.idPillCountType=null;
        this.startDate=null;
        this.idCycle=null;
        this.idHavingMealsType=null;
        this.havingMealsTime=null;
        this.annotation=null;
        this.isActive=null;
        this.reminderTimes=null;
    }

    public String getPillName() {
        return pillName;
    }

    public Integer getPillCount() {
        return pillCount;
    }

    public Integer getIdPillCountType() {
        return idPillCountType;
    }

    public DateData getStartDate() {
        return startDate;
    }

    public Integer getIdCycle() {
        return idCycle;
    }

    public Integer getIdHavingMealsType() {
        return idHavingMealsType;
    }

    public String getHavingMealsTime() {
        return havingMealsTime;
    }

    public String getAnnotation() {
        return annotation;
    }

    public Integer getIsActive() {
        return isActive;
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public void setHavingMealsTime(String havingMealsTime) {
        this.havingMealsTime = havingMealsTime;
    }

    public void setIdCycle(Integer idCycle) {
        this.idCycle = idCycle;
    }

    public void setIdHavingMealsType(Integer idHavingMealsType) {
        this.idHavingMealsType = idHavingMealsType;
    }

    public void setIdPillCountType(Integer idPillCountType) {
        this.idPillCountType = idPillCountType;
    }

    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
    }

    public void setPillCount(Integer pillCount) {
        this.pillCount = pillCount;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public void setStartDate(DateData startDate) {
        this.startDate = startDate;
    }

    public String[] getReminderTimes() {
        return reminderTimes;
    }

    public void setReminderTimes(String[] reminderTimes) {
        this.reminderTimes = reminderTimes;
    }
}

