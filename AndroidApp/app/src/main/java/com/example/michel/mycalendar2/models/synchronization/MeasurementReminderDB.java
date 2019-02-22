package com.example.michel.mycalendar2.models.synchronization;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MeasurementReminderDB extends SynchModel {
    private UUID idMeasurementReminder;
    private int idMeasurementType;
    private Date startDate;
    private UUID idCycle;
    private Integer idHavingMealsType;
    private Time havingMealsTime;
    private String annotation;
    private int isActive;
    private int timesADay;
    private Integer isOneTime;
    private int userId;
}
