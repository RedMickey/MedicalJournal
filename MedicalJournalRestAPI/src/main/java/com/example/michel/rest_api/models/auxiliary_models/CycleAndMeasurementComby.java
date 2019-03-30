package com.example.michel.rest_api.models.auxiliary_models;

import com.example.michel.rest_api.models.measurement.MeasurementReminderCourse;
import lombok.Data;

@Data
public class CycleAndMeasurementComby {
    private CycleDBInsertEntry cycleDBInsertEntry;
    private MeasurementReminderCourse measurementReminderCourse;
}
