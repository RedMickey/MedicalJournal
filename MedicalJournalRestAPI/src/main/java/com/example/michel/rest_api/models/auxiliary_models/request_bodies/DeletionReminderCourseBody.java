package com.example.michel.rest_api.models.auxiliary_models.request_bodies;

import lombok.Data;

import java.util.UUID;

@Data
public class DeletionReminderCourseBody {
    private UUID idReminder;
    private UUID idCycle;
    private UUID idWeekSchedule;
    private int courseType;
    private Integer userId;
}
