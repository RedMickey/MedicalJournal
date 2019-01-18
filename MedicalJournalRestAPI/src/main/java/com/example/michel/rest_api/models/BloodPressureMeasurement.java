package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;

@Entity
@Data
@Table(name = "blood_pressure_measurement", schema = "medical_journal", catalog = "")
public class BloodPressureMeasurement {
    @Id
    @Column(name="_id_blood_press_measur")
    private String id;
    @Column(name="systolic_BP")
    private Integer systolicBP;
    @Column(name="diastolic_BP")
    private Integer diastolicBP;
    @Column(name="reminder_date")
    private java.sql.Date reminderDate;
    @Column(name = "_id_measurement_reminder")
    private String idMeasurementReminder;
    @Column(name="is_done")
    private Integer isDone;
    @Column(name="reminder_time")
    private Time reminderTime;

    @Column(name="insert_timestamp")
    private Timestamp insertTimestamp;
    @Column(name="update_timestamp")
    private Timestamp updateTimestamp;
}
