package com.example.michel.mycalendar2.models.synchronization;

import java.util.Date;
import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class WeekScheduleDB extends SynchModel {
    private UUID idWeekSchedule;
    private int mon;
    private int tue;
    private int wed;
    private int thu;
    private int fri;
    private int sat;
    private int sun;

    public WeekScheduleDB(Date synchTime, int changeType, UUID idWeekSchedule,
                          int mon, int tue, int wed, int thu, int fri, int sat, int sun){
        super(synchTime,changeType);
        this.idWeekSchedule = idWeekSchedule;
        this.mon = mon;
        this.tue = tue;
        this.wed = wed;
        this.thu = thu;
        this.fri = fri;
        this.sat = sat;
        this.sun = sun;
    }
}
