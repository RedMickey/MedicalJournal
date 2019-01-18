package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.util.Objects;

@Entity
@Data
@Table(name = "reminder_time", schema = "medical_journal", catalog = "")
public class ReminderTime {
    @Id
    @Column(name = "_id_reminder_time")
    private String id;
    @Column(name = "reminder_time")
    private Time reminderTime;
    @Column(name = "_id_pill_reminder")
    private String idPillReminder;
    @Column(name = "_id_measurement_reminder")
    private String idMeasurementReminder;
}
