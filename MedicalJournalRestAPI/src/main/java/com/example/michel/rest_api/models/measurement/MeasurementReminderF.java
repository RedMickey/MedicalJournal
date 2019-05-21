package com.example.michel.rest_api.models.measurement;

import lombok.Data;

import java.util.UUID;

@Data
public class MeasurementReminderF {
    protected String measurementTypeName;
    protected int idMeasurementType;
    protected UUID id;
    protected int havingMealsType;
    protected int isActive;
    protected int numberOfDoingAction;
    protected String startDate;
    protected String endDate;
    protected int numberOfDoingActionLeft;
}
