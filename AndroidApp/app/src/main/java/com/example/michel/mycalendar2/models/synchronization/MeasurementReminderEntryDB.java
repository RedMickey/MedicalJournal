package com.example.michel.mycalendar2.models.synchronization;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class MeasurementReminderEntryDB extends SynchModel {
    private UUID idMeasurRemindEntry;
    private Double value1;
    private Double value2;
    private UUID idMeasurementReminder;
    private int isDone;
    private Date reminderTime;
    private Date reminderDate;
    //private Integer isOneTime;

    public MeasurementReminderEntryDB(Date synchTime, int changeType, UUID idMeasurRemindEntry,
                                      Double value1, Double value2, UUID idMeasurementReminder,
                                      int isDone, Date reminderTime, Date reminderDate)
    {
        super(synchTime,changeType);
        this.idMeasurRemindEntry = idMeasurRemindEntry;
        this.value1 = value1;
        this.value2 = value2;
        this.idMeasurementReminder = idMeasurementReminder;
        this.isDone = isDone;
        this.reminderTime = reminderTime;
        this.reminderDate = reminderDate;
    }
}
