package com.example.michel.mycalendar2.models.synchronization;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReminderEntriesReqModule {
    private int userId;
    private List<PillReminderEntryDB> pillReminderEntryDBList;
    private List<MeasurementReminderEntryDB> measurementReminderEntryDBList;

    public ReminderEntriesReqModule(){
        pillReminderEntryDBList = new ArrayList<>();
        measurementReminderEntryDBList = new ArrayList<>();
    }
}
