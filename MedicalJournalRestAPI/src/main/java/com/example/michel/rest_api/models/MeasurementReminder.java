package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@Table(name = "measurement_reminder", schema = "medical_journal", catalog = "")
public class MeasurementReminder {
    @Id
    @Column(name = "_id_measurement_reminder")
    private UUID id;
    @Column(name = "_id_measurement_type")
    private int idMeasurementType;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "_id_cycle")
    private UUID idCycle;
    @Column(name = "_id_having_meals_type")
    private Integer idHavingMealsType;
    @Column(name = "having_meals_time")
    private Time havingMealsTime;
    private String annotation;
    @Column(name = "is_active")
    private int isActive;
    @Column(name = "times_a_day")
    private int timesADay;
    @Column(name = "is_one_time")
    private Integer isOneTime;
    @Column(name = "user_id")
    private int userId;
    @Column(name = "synch_time")
    private Timestamp synchTime;
    @Column(name = "change_type")
    private int changeType;
}
