package com.example.michel.mycalendar2.models;

/*public void insertPillReminder(String pillName, int pillCount, int idPillCountType,
        String startDate, int idCycle, @Nullable int idHavingMealsType,
@Nullable String havingMealsTime, String annotation, int isActive){*/

import android.support.annotation.Nullable;

public class PillReminderDBInsertEntry {
    private String pillName;
    private Integer pillCount;
    private Integer idPillCountType;
    private String startDate;
    private Integer idCycle;
    private Integer idHavingMealsType;
    private String havingMealsTime;
    private String annotation;
    private Integer isActive;

    public PillReminderDBInsertEntry(String pillName, Integer pillCount, Integer idPillCountType,
                                     String startDate, Integer idCycle, @Nullable Integer idHavingMealsType,
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

    public String getStartDate() {
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
}

