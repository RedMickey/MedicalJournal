package com.example.michel.mycalendar2.models.synchronization;

import java.sql.Time;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class ReminderTimeDB extends SynchModel {
    private UUID idReminderTime;
    private Time reminderTime;
    private UUID idPillReminder;
    private UUID idMeasurementReminder;
}
