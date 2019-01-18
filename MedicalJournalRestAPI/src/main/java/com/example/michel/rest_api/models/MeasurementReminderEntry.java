package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Entity
@Data
@Table(name = "measurement_reminder_entry", schema = "medical_journal", catalog = "")
public class MeasurementReminderEntry {
    @Id
    @Column(name = "_id_measur_remind_entry")
    private String id;
    private Double value1;
    private Double value2;
    @Column(name = "_id_measurement_reminder")
    private String idMeasurementReminder;
    @Column(name = "is_done")
    private int isDone;
    @Column(name = "reminder_time")
    private Time reminderTime;
    @Column(name = "reminder_date")
    private Date reminderDate;
    @Column(name = "is_one_time")
    private Integer isOneTime;
}
