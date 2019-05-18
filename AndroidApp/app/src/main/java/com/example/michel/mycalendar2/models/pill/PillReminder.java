package com.example.michel.mycalendar2.models.pill;

import com.example.michel.mycalendar2.models.ItemReminderModel;

import java.util.UUID;

public class PillReminder extends ItemReminderModel {
    private String pillName;
    private int pillCount;
    private String pillCountType;

    public PillReminder(UUID id, String pillName, int pillCount, String pillCountType,
                        int havingMealsType, int isActive, int numberOfDoingAction,
                        String startDate, String endDate, int numberOfDoingActionLeft)
    {
        super(id, havingMealsType, isActive, numberOfDoingAction, startDate, endDate, numberOfDoingActionLeft);
        this.pillName = pillName;
        this.pillCount = pillCount;
        this.pillCountType = pillCountType;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public int getPillCount() {
        return pillCount;
    }

    public void setPillCount(int pillCount) {
        this.pillCount = pillCount;
    }

    public String getPillCountType() {
        return pillCountType;
    }

    public void setPillCountType(String pillCountType) {
        this.pillCountType = pillCountType;
    }
}
