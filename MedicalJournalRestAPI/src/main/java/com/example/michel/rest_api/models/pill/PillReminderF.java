package com.example.michel.rest_api.models.pill;

import lombok.Data;

import java.util.UUID;

@Data
public class PillReminderF {
    private String pillName;
    private int pillCount;
    private String pillCountType;
    protected UUID id;
    protected int havingMealsType;
    protected int isActive;
    protected int numberOfDoingAction;
    protected String startDate;
    protected String endDate;
    protected int numberOfDoingActionLeft;
}
