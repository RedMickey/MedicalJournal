package com.example.michel.mycalendar2.models;

public class PillModel {
    protected int id;
    protected String pillName;
    protected int pillCount;
    protected String pillCountType;
    protected int havingMealsType;

    public PillModel(int id, String pillName, int pillCount, String pillCountType,
                        int havingMealsType){
        this.id = id;
        this.pillName = pillName;
        this.pillCount = pillCount;
        this.pillCountType = pillCountType;
        this.havingMealsType = havingMealsType;
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

    public int getHavingMealsType() {
        return havingMealsType;
    }

    public void setHavingMealsType(int havingMealsType) {
        this.havingMealsType = havingMealsType;
    }
}
