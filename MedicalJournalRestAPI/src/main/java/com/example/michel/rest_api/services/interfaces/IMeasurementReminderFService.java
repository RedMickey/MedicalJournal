package com.example.michel.rest_api.services.interfaces;

import com.example.michel.rest_api.models.measurement.MeasurementReminderF;

import java.util.List;

public interface IMeasurementReminderFService {

    public List<MeasurementReminderF> getAllMeasurementRemindersF(int userId);
}
