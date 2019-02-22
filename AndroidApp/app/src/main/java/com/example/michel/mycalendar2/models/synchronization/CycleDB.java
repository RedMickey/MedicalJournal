package com.example.michel.mycalendar2.models.synchronization;

import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class CycleDB extends SynchModel {
    private UUID idCycle;
    private int period;
    private int periodDmType;
    private Integer onceAPeriod;
    private Integer onceAPeriodDmType;
    private UUID idWeekSchedule;
    private int idCyclingType;
}
