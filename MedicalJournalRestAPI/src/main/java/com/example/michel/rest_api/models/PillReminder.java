package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@Table(name = "pill_reminder", schema = "medical_journal", catalog = "")
public class PillReminder {
    @Id
    @Column(name = "_id_pill_reminder")
    private UUID id;
    @Column(name = "_id_pill")
    private UUID idPill;
    @Column(name = "pill_count")
    private int pillCount;
    @Column(name = "_id_pill_count_type")
    private int idPillCountType;
    @Column(name = "start_date")
    private Date startDate;
    @Column(name = "_id_cycle")
    private UUID idCycle;
    @Column(name = "_id_having_meals_type")
    private Integer idHavingMealsType;
    @Column(name = "having_meals_time")
    private Integer havingMealsTime;
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
