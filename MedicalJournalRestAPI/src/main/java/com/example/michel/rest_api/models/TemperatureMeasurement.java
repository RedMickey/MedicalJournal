package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

@Entity
@Data
@Table(name = "temperature_measurement", schema = "medical_journal", catalog = "")
public class TemperatureMeasurement {
    @Id
    @Column(name = "_id_temp_measur")
    private String id;
    private Double temperature;
    @Column(name = "reminder_date")
    private Date reminderDate;
    @Column(name = "_id_measurement_reminder")
    private String idMeasurementReminder;
    @Column(name = "is_done")
    private int isDone;
    @Column(name = "reminder_time")
    private Time reminderTime;
}
