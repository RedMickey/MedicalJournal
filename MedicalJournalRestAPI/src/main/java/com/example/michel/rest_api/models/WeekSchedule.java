package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Data
@Table(name = "week_schedule", schema = "medical_journal", catalog = "")
public class WeekSchedule {
    @Id
    @Column(name = "_id_week_schedule")
    private String id;
    private Integer mon;
    private Integer tue;
    private Integer wed;
    private Integer thu;
    private Integer fri;
    private Integer sat;
    private Integer sun;
}
