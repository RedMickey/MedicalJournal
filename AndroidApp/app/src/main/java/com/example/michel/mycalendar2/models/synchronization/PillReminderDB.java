package com.example.michel.mycalendar2.models.synchronization;

import java.sql.Date;
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
}
