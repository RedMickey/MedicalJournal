package com.example.michel.rest_api.models.measurement;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class MeasurementReminderEntryF {
    private double value1;
    private double value2;
    private int idMeasurementType;
    private String measurementTypeName;
    private String measurementValueTypeName;
    protected int isDone;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Europe/Moscow")
    protected Date date;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Europe/Moscow")
    protected Date havingMealsTime;
    protected UUID id;
    protected int havingMealsType;
}
