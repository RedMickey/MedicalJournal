package com.example.michel.rest_api.models.auxiliary_models;

import lombok.Data;

import java.util.UUID;

@Data
public class CycleDBInsertEntry {
    private Integer period;
    private Integer periodDMType;
    private Integer onceAPeriod;
    private Integer onceAPeriodDMType;
    private Integer idCyclingType;
    private boolean[] weekSchedule;
    private UUID idCycle;
    private UUID idWeekSchedule;
}
