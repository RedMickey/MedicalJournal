package com.example.michel.mycalendar2.models;

public class PillModel extends ReminderModel {
    protected String pillName;
    protected int pillCount;
    protected String pillCountType;

    public PillModel(int id, String pillName, int pillCount, String pillCountType,
                        int havingMealsType){
        super(id, havingMealsType);
        this.id = id;
        this.pillName = pillName;
        this.pillCount = pillCount;
        this.pillCountType = pillCountType;
        this.havingMealsType = havingMealsType;
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
