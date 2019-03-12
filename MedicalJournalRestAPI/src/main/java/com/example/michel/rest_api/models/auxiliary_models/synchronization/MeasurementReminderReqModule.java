package com.example.michel.rest_api.models.auxiliary_models.synchronization;

import com.example.michel.rest_api.models.*;
import lombok.Data;

import java.util.List;

@Data
public class MeasurementReminderReqModule {
    private List<Cycle> cycleDBList;
    private List<MeasurementReminder> measurementReminderDBList;
    private List<MeasurementReminderEntry> measurementReminderEntryDBList;
    private List<WeekSchedule> weekScheduleDBList;
    private List<ReminderTime> reminderTimeDBList;
}
