package com.example.michel.mycalendar2.models.synchronization;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MeasurementReminderReqModule {
    private List<CycleDB> cycleDBList;
    private List<MeasurementReminderDB> measurementReminderDBList;
    private List<MeasurementReminderEntryDB> measurementReminderEntryDBList;
    private List<WeekScheduleDB> weekScheduleDBList;
    private List<ReminderTimeDB> reminderTimeDBList;
    private int type;
}
