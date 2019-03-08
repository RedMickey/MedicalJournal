package com.example.michel.rest_api.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "week_schedule", schema = "medical_journal", catalog = "")
public class WeekSchedule {
    @Id
    @Column(name = "_id_week_schedule")
    private UUID id;
    private Integer mon;
    private Integer tue;
    private Integer wed;
    private Integer thu;
    private Integer fri;
    private Integer sat;
    private Integer sun;
    @Column(name = "synch_time")
    private Timestamp synchTime;
    @Column(name = "change_type")
    private int changeType;
}
