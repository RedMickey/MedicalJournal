package com.example.michel.mycalendar2.models.synchronization;


import java.sql.Time;
import java.util.Date;
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
    private Integer havingMealsTime;
    private String annotation;
    private int isActive;
    private int timesADay;
    //private Integer isOneTime;
    private int userId;

    public MeasurementReminderDB(Date synchTime, int changeType, UUID idMeasurementReminder,
                                 int idMeasurementType, Date startDate, UUID idCycle,
                                 Integer idHavingMealsType, Integer havingMealsTime, String annotation,
                                 int isActive, int timesADay, int userId){
        super(synchTime,changeType);
        this.idMeasurementReminder = idMeasurementReminder;
        this.idMeasurementType = idMeasurementType;
        this.startDate = startDate;
        this.idCycle = idCycle;
        this.idHavingMealsType = idHavingMealsType;
        this.havingMealsTime = havingMealsTime;
        this.annotation = annotation;
        this.isActive = isActive;
        this.timesADay = timesADay;
        //this.isOneTime = isOneTime;
        this.userId = userId;
    }
}
