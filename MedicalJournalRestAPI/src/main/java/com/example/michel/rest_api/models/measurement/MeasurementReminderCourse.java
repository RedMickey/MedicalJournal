package com.example.michel.rest_api.models.measurement;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class MeasurementReminderCourse {
    private UUID idMeasurementReminder;
    private int idMeasurementType;
    private Date startDate;
    private UUID idCycle;
    private Integer idHavingMealsType;
    private Integer havingMealsTime;
    private String annotation;
    private Integer isActive;
    private Date[] reminderTimes;

}
