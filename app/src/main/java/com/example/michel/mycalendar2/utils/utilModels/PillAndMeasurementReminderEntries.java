package com.example.michel.mycalendar2.utils.utilModels;

import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;

import java.util.List;

public class PillAndMeasurementReminderEntries {
    public List<PillReminderEntry> pillReminderEntries;
    public List<MeasurementReminderEntry> measurementReminderEntries;

    public PillAndMeasurementReminderEntries(List<PillReminderEntry> pillReminderEntries,
                                             List<MeasurementReminderEntry> measurementReminderEntries)
    {
        this.pillReminderEntries = pillReminderEntries;
        this.measurementReminderEntries = measurementReminderEntries;
    }
}
