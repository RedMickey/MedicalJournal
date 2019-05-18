package com.example.michel.rest_api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "pill_reminder_entry", schema = "medical_journal", catalog = "")
public class PillReminderEntry {
    @Id
    @Column(name = "_id_pill_reminder_entry")
    private UUID idPillReminderEntry;
    @Column(name = "is_done")
    private int isDone;
    @Column(name = "reminder_date")
    private Date reminderDate;
    @Column(name = "_id_pill_reminder")
    private UUID idPillReminder;
    @Column(name = "reminder_time")
    private Time reminderTime;
    @Column(name = "is_one_time")
    private Integer isOneTime;
    @Column(name = "synch_time")
    private Timestamp synchTime;
    @Column(name = "change_type")
    private int changeType;
}
