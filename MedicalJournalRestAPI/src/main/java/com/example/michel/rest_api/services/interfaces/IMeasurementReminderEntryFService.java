package com.example.michel.rest_api.services.interfaces;

import com.example.michel.rest_api.models.auxiliary_models.UpdateMeasurementReminderBody;
import com.example.michel.rest_api.models.measurement.MeasurementReminderEntryF;

import java.util.Date;
import java.util.List;

public interface IMeasurementReminderEntryFService {

    public List<MeasurementReminderEntryF> getMeasurementReminderEntriesByDate(Date date);

    public int updateIsDoneMeasurementReminderEntry(UpdateMeasurementReminderBody updateMeasurementReminderBody);
}
