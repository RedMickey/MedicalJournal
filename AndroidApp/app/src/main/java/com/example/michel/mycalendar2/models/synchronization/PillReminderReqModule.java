package com.example.michel.mycalendar2.models.synchronization;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PillReminderReqModule {
    private List<PillDB> pillDBList;
    private List<CycleDB> cycleDBList;
    private List<PillReminderDB> pillReminderDBList;
    private List<PillReminderEntryDB> pillReminderEntryDBList;
    private List<WeekScheduleDB> weekScheduleDBList;
    private List<ReminderTimeDB> reminderTimeDBList;
    private int type;
}
