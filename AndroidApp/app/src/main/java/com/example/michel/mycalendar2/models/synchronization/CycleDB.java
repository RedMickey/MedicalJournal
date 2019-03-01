package com.example.michel.mycalendar2.models.synchronization;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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

    public CycleDB(Date synchTime, int changeType, UUID idCycle, int period, int periodDmType,
                   Integer onceAPeriod, Integer onceAPeriodDmType, UUID idWeekSchedule,
                   int idCyclingType){
        super(synchTime,changeType);
        this.idCycle = idCycle;
        this.period = period;
        this.periodDmType = periodDmType;
        this.onceAPeriod = onceAPeriod;
        this.onceAPeriodDmType = onceAPeriodDmType;
        this.idWeekSchedule = idWeekSchedule;
        this.idCyclingType = idCyclingType;
    }
}
