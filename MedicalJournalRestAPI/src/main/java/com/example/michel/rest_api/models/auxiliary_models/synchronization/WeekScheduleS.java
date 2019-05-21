package com.example.michel.rest_api.models.auxiliary_models.synchronization;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class WeekScheduleS {
    private UUID idWeekSchedule;
    private int mon;
    private int tue;
    private int wed;
    private int thu;
    private int fri;
    private int sat;
    private int sun;
    private Date synchTime;
    private int changeType;
}
