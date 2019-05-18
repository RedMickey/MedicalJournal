package com.example.michel.rest_api.models.auxiliary_models.synchronization;

import com.example.michel.rest_api.models.MeasurementReminderEntry;
import com.example.michel.rest_api.models.PillReminderEntry;
import lombok.Data;

import java.util.List;

@Data
public class ReminderEntriesReqModule {
    private int userId;
    private List<PillReminderEntry> pillReminderEntryDBList;
    private List<MeasurementReminderEntry> measurementReminderEntryDBList;
}
