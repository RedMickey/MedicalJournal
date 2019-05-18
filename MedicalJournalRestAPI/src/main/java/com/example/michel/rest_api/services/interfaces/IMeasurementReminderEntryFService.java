package com.example.michel.rest_api.services.interfaces;

import com.example.michel.rest_api.models.auxiliary_models.request_bodies.UpdateMeasurementReminderBody;
import com.example.michel.rest_api.models.measurement.MeasurementReminderEntryF;

import java.util.Date;
import java.util.List;

public interface IMeasurementReminderEntryFService {

    public List<MeasurementReminderEntryF> getMeasurementReminderEntriesByDate(Date date, int userId);

    public int updateIsDoneMeasurementReminderEntry(UpdateMeasurementReminderBody updateMeasurementReminderBody);
}
