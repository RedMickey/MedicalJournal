package com.example.michel.rest_api.models.auxiliary_models;

import com.example.michel.rest_api.models.measurement.MeasurementReminderEntryF;
import com.example.michel.rest_api.models.pill.PillReminderEntryF;


import java.util.ArrayList;
import java.util.List;

public class PillAndMeasurementReminderEntriesF {
    public List<PillReminderEntryF> pillReminderEntries;
    public List<MeasurementReminderEntryF> measurementReminderEntries;

    public PillAndMeasurementReminderEntriesF(){
        this.pillReminderEntries = new ArrayList<PillReminderEntryF>();
        this.measurementReminderEntries = new ArrayList<MeasurementReminderEntryF>();
    }

    public PillAndMeasurementReminderEntriesF(List<PillReminderEntryF> pillReminderEntries,
                                             List<MeasurementReminderEntryF> measurementReminderEntries)
    {
        this.pillReminderEntries = pillReminderEntries;
        this.measurementReminderEntries = measurementReminderEntries;
    }
}
