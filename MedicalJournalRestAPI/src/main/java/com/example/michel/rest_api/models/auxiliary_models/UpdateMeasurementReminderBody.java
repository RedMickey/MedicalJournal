package com.example.michel.rest_api.models.auxiliary_models;

import lombok.Data;

@Data
public class UpdateMeasurementReminderBody extends UpdatePillReminderBody {
    private double value1;
    private double value2;
}
