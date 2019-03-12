package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@Table(name = "reminder_time", schema = "medical_journal", catalog = "")
public class ReminderTime {
    @Id
    @Column(name = "_id_reminder_time")
    private UUID idReminderTime;
    @Column(name = "reminder_time")
    private Time reminderTime;
    @Column(name = "_id_pill_reminder")
    private UUID idPillReminder;
    @Column(name = "_id_measurement_reminder")
    private UUID idMeasurementReminder;
    @Column(name = "synch_time")
    private Timestamp synchTime;
    @Column(name = "change_type")
    private int changeType;
}
