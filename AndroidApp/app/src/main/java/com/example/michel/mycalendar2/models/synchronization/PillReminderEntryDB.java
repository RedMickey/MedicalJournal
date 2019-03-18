package com.example.michel.mycalendar2.models.synchronization;

import java.sql.Time;
import java.util.Date;
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
    private Date reminderTime;
    private Integer isOneTime;

    public PillReminderEntryDB(Date synchTime, int changeType, UUID idPillReminderEntry,
                               int isDone, Date reminderDate, UUID idPillReminder,
                               Date reminderTime)
    {
        super(synchTime,changeType);
        this.idPillReminderEntry = idPillReminderEntry;
        this.isDone = isDone;
        this.reminderDate = reminderDate;
        this.idPillReminder = idPillReminder;
        this.reminderTime = reminderTime;
    }
}
