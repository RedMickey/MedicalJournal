package com.example.michel.mycalendar2.models.synchronization;

import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PillReminderDB extends SynchModel {
    private UUID idPillReminder;
    private UUID idPill;
    private int pillCount;
    private int idPillCountType;
    private Date startDate;
    private UUID idCycle;
    private Integer idHavingMealsType;
    private Integer havingMealsTime;
    private String annotation;
    private int isActive;
    private int timesADay;
    //private Integer isOneTime;
    private int userId;

    public PillReminderDB(Date synchTime, int changeType, UUID idPillReminder,
                          UUID idPill, int pillCount, int idPillCountType,
                          Date startDate, UUID idCycle, Integer idHavingMealsType,
                          Integer havingMealsTime, String annotation, int isActive,
                          int timesADay, int userId)
    {
        super(synchTime,changeType);
        this.idPillReminder = idPillReminder;
        this.idPill = idPill;
        this.pillCount = pillCount;
        this.idPillCountType = idPillCountType;
        this.startDate = startDate;
        this.idCycle = idCycle;
        this.idHavingMealsType = idHavingMealsType;
        this.havingMealsTime = havingMealsTime;
        this.annotation = annotation;
        this.isActive = isActive;
        this.timesADay = timesADay;
        this.userId = userId;
    }
}
