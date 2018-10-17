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
}
