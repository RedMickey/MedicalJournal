package com.example.michel.mycalendar2.models;


import android.support.annotation.Nullable;

import java.util.UUID;

public class CycleDBInsertEntry {
    private Integer period;
    private Integer periodDMType;
    private Integer once_aPeriod;
    private Integer once_aPeriodDMType;
    private Integer idCyclingType;
    private int[] weekSchedule;
    private int dayCount;
    private int dayInterval;
    private UUID idCycle;
    private UUID idWeekSchedule;

    public CycleDBInsertEntry(Integer period, Integer periodDMType, @Nullable Integer once_aPeriod,
                              @Nullable Integer once_aPeriodDMType, Integer idCyclingType,
                              @Nullable int[] weekSchedule, int dayCount)
    {
        this.period=period;
        this.periodDMType=periodDMType;
        this.once_aPeriod=once_aPeriod;
        this.once_aPeriodDMType=once_aPeriodDMType;
        this.idCyclingType=idCyclingType;
        this.weekSchedule=weekSchedule;
        this.dayCount=dayCount;
        this.dayInterval=0;
        this.idCycle=null;
        this.idWeekSchedule=null;
    }

    public CycleDBInsertEntry()
    {
        this.period=null;
        this.periodDMType=null;
        this.once_aPeriod=null;
        this.once_aPeriodDMType=null;
        this.idCyclingType=null;
        this.weekSchedule=null;
        this.dayCount=0;
        this.dayInterval=0;
        this.idWeekSchedule=null;
        this.idCycle=null;
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

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public int getDayInterval() {
        return dayInterval;
    }

    public void setDayInterval(int dayInterval) {
        this.dayInterval = dayInterval;
    }

    public UUID getIdCycle() {
        return idCycle;
    }

    public void setIdCycle(UUID idCycle) {
        this.idCycle = idCycle;
    }

    public UUID getIdWeekSchedule() {
        return idWeekSchedule;
    }

    public void setIdWeekSchedule(UUID idWeekSchedule) {
        this.idWeekSchedule = idWeekSchedule;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==this) return true;
        if (obj==null || obj.getClass()!=this.getClass()) return false;
        return (this.period==((CycleDBInsertEntry) obj).getPeriod()&&
                this.periodDMType==((CycleDBInsertEntry) obj).getPeriodDMType()&&
                this.once_aPeriod==((CycleDBInsertEntry) obj).getOnce_aPeriod()&&
                this.once_aPeriodDMType==((CycleDBInsertEntry) obj).getOnce_aPeriodDMType()&&
                this.idCyclingType==((CycleDBInsertEntry) obj).getIdCyclingType()&&
                this.dayCount==((CycleDBInsertEntry) obj).getDayCount()&&
                this.dayInterval==((CycleDBInsertEntry) obj).getDayInterval()&&
                this.idCycle.compareTo(((CycleDBInsertEntry) obj).getIdCycle())==0&&
                this.idWeekSchedule.compareTo(((CycleDBInsertEntry) obj).getIdWeekSchedule())==0);
    }
}
