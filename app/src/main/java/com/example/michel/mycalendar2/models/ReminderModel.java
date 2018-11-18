package com.example.michel.mycalendar2.models;

public class ReminderModel {
    protected int id;
    protected int havingMealsType;

    public ReminderModel(int id, int havingMealsType){
        this.id = id;
        this.havingMealsType = havingMealsType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHavingMealsType() {
        return havingMealsType;
    }

    public void setHavingMealsType(int havingMealsType) {
        this.havingMealsType = havingMealsType;
    }
}
