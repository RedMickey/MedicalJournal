package com.example.michel.mycalendar2.models;

import java.util.UUID;

public class ReminderModel {
    protected UUID id;
    protected int havingMealsType;

    public ReminderModel(UUID id, int havingMealsType){
        this.id = id;
        this.havingMealsType = havingMealsType;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getHavingMealsType() {
        return havingMealsType;
    }

    public void setHavingMealsType(int havingMealsType) {
        this.havingMealsType = havingMealsType;
    }
}
