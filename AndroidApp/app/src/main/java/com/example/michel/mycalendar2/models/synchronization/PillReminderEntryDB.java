package com.example.michel.mycalendar2.models.synchronization;

import java.sql.Date;
import java.sql.Time;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PillReminderEntryDB extends SynchModel {
    private UUID idPillReminderEntry;
    private int isDone;
    private Date reminderDate;
    private UUID idPillReminder;
    private Time reminderTime;
    //private Integer isOneTime;
}
