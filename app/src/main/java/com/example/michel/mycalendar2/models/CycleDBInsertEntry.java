package com.example.michel.mycalendar2.models;


import android.support.annotation.Nullable;

public class CycleDBInsertEntry {
    private Integer period;
    private Integer periodDMType;
    private Integer once_aPeriod;
    private Integer once_aPeriodDMType;
    private Integer idCyclingType;
    private int[] weekSchedule;

    public CycleDBInsertEntry(Integer period, Integer periodDMType, @Nullable Integer once_aPeriod,
                              @Nullable Integer once_aPeriodDMType, Integer idCyclingType,
                              @Nullable int[] weekSchedule)
    {
        this.period=period;
        this.periodDMType=periodDMType;
        this.once_aPeriod=once_aPeriod;
        this.once_aPeriodDMType=once_aPeriodDMType;
        this.idCyclingType=idCyclingType;
        this.weekSchedule=weekSchedule;
    }

    public CycleDBInsertEntry()
    {
        this.period=null;
        this.periodDMType=null;
        this.once_aPeriod=null;
        this.once_aPeriodDMType=null;
        this.idCyclingType=null;
        this.weekSchedule=null;
    }

    public Integer getPeriod() {
        return period;
    }

    public Integer getPeriodDMType() {
        return periodDMType;
    }

    public Integer getOnce_aPeriod() {
        return once_aPeriod;
    }

    public Integer getOnce_aPeriodDMType() {
        return once_aPeriodDMType;
    }

    public Integer getIdCyclingType() {
        return idCyclingType;
    }

    public int[] getWeekSchedule() {
        return weekSchedule;
    }

    public void setIdCyclingType(Integer idCyclingType) {
        this.idCyclingType = idCyclingType;
    }

    public void setPeriod(Integer period) {
        this.period = period;
    }

    public void setOnce_aPeriod(Integer once_aPeriod) {
        this.once_aPeriod = once_aPeriod;
    }

    public void setOnce_aPeriodDMType(Integer once_aPeriodDMType) {
        this.once_aPeriodDMType = once_aPeriodDMType;
    }

    public void setPeriodDMType(Integer periodDMType) {
        this.periodDMType = periodDMType;
    }

    public void setWeekSchedule(int[] weekSchedule) {
        this.weekSchedule = weekSchedule;
    }
}
