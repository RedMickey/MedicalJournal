package com.example.michel.mycalendar2.models.synchronization;

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
}
