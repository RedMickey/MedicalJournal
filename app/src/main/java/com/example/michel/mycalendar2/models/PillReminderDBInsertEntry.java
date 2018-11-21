package com.example.michel.mycalendar2.models;

/*public void insertPillReminder(String pillName, int pillCount, int idPillCountType,
        String startDate, int idCycle, @Nullable int idHavingMealsType,
@Nullable String havingMealsTime, String annotation, int isActive){*/

import android.support.annotation.Nullable;

import com.example.michel.mycalendar2.calendarview.data.DateData;

public class PillReminderDBInsertEntry extends ReminderDBEntry {
    private String pillName;
    private Integer pillCount;
    private Integer idPillCountType;
    private Integer idPillReminder;

    public PillReminderDBInsertEntry(Integer idPillReminder, String pillName, Integer pillCount, Integer idPillCountType,
                                     DateData startDate, Integer idCycle, @Nullable Integer idHavingMealsType,
                                     @Nullable Integer havingMealsTime, String annotation, Integer isActive,
                                     ReminderTime[] reminderTimes)
    {
        super(startDate, idCycle, idHavingMealsType, havingMealsTime, annotation,
                isActive, reminderTimes);
        this.pillName=pillName;
        this.pillCount=pillCount;
        this.idPillCountType=idPillCountType;
        this.idPillReminder = idPillReminder;
    }

    public PillReminderDBInsertEntry()
    {
        super();
        this.pillName=null;
        this.pillCount=null;
        this.idPillCountType=null;
        this.idPillReminder=null;
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

    public void setIdPillCountType(Integer idPillCountType) {
        this.idPillCountType = idPillCountType;
    }

    public void setPillCount(Integer pillCount) {
        this.pillCount = pillCount;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public Integer getIdPillReminder() {
        return idPillReminder;
    }

    public void setIdPillReminder(Integer idPillReminder) {
        this.idPillReminder = idPillReminder;
    }
}

