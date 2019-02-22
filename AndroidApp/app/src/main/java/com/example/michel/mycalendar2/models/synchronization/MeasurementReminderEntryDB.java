package com.example.michel.mycalendar2.models.synchronization;

import java.sql.Date;
import java.sql.Time;
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
    private Time reminderTime;
    private Date reminderDate;
    //private Integer isOneTime;
}
