package com.example.michel.rest_api.models.pill;

import java.util.Date;
import java.util.UUID;

@lombok.Data
public class PillReminderCourse {
    private String pillName;
    private Integer pillCount;
    private Integer idPillCountType;
    private UUID idPillReminder;
    private Date startDate;
    private UUID idCycle;
    private Integer idHavingMealsType;
    private Integer havingMealsTime;
    private String annotation;
    private Integer isActive;
    private Date[] reminderTimes;
    private Integer userId;
}
