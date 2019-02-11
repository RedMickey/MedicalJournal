package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
public class Cycle {
    @Id
    @Column(name = "_id_cycle")
    private UUID id;
    private int period;
    @Column(name = "period_DM_type")
    private int periodDmType;
    @Column(name = "once_a_period")
    private Integer onceAPeriod;
    @Column(name = "once_a_period_DM_type")
    private Integer onceAPeriodDmType;
    @Column(name = "_id_week_schedule")
    private UUID idWeekSchedule;
    @Column(name = "_id_cycling_type")
    private int idCyclingType;
}
