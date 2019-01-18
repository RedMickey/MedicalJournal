package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Entity
@Data
@Table(name = "pill_reminder_entry", schema = "medical_journal", catalog = "")
public class PillReminderEntry {
    @Id
    @Column(name = "_id_pill_reminder_entry")
    private String id;
    @Column(name = "is_done")
    private int isDone;
    @Column(name = "reminder_date")
    private Date reminderDate;
    @Column(name = "_id_pill_reminder")
    private String idPillReminder;
    @Column(name = "reminder_time")
    private Time reminderTime;
    @Column(name = "is_one_time")
    private Integer isOneTime;
}
