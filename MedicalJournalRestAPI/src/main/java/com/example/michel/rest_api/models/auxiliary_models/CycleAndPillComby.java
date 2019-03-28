package com.example.michel.rest_api.models.auxiliary_models;

import com.example.michel.rest_api.models.pill.PillReminderCourse;
import lombok.Data;

@Data
public class CycleAndPillComby {
    private CycleDBInsertEntry cycleDBInsertEntry;
    private PillReminderCourse pillReminderCourse;
}
