package com.example.michel.rest_api.models.auxiliary_models.request_bodies;

import com.example.michel.rest_api.models.measurement.MeasurementReminderCourse;
import lombok.Data;

@Data
public class OneTimeMeasEntryBody {
    private MeasurementReminderCourse measurementReminderCourse;
    private double value1;
    private double value2;
}
