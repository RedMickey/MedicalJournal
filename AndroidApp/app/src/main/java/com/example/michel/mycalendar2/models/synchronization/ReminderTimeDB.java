package com.example.michel.mycalendar2.models.synchronization;

import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ReminderTimeDB extends SynchModel {
    private UUID idReminderTime;
    private Date reminderTime;
    private UUID idPillReminder;
    private UUID idMeasurementReminder;

    public ReminderTimeDB(Date synchTime, int changeType, UUID idReminderTime,
                          Date reminderTime, UUID idPillReminder, UUID idMeasurementReminder)
    {
        super(synchTime,changeType);
        this.idReminderTime = idReminderTime;
        this.reminderTime = reminderTime;
        this.idPillReminder = idPillReminder;
        this.idMeasurementReminder = idMeasurementReminder;
    }
}
