package com.example.michel.rest_api.dao.interfaces;

import com.example.michel.rest_api.models.auxiliary_models.CycleAndMeasurementComby;
import com.example.michel.rest_api.models.measurement.MeasurementReminderF;

import java.util.List;
import java.util.UUID;

public interface IMeasurementReminderFDao {
    public List<MeasurementReminderF> getAllMeasurementRemindersF(int userId);

    public CycleAndMeasurementComby getCycleAndMeasurementCombyById(UUID mrID, int userId);
}
