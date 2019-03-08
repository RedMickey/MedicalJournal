package com.example.michel.rest_api.models.auxiliary_models.synchronization;

import com.example.michel.rest_api.models.*;
import lombok.Data;

import java.util.List;

@Data
public class PillReminderReqModule {
    private List<Pill> pillDBList;
    private List<Cycle> cycleDBList;
    private List<PillReminder> pillReminderDBList;
    private List<PillReminderEntry> pillReminderEntryDBList;
    private List<WeekSchedule> weekScheduleDBList;
    private List<ReminderTime> reminderTimeDBList;
}
