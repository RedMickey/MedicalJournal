package com.example.michel.mycalendar2.models.synchronization;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ReminderSynchronizationReqModule {
    private List<CycleDB> cycleDBList;
    private List<MeasurementReminderDB> measurementReminderDBList;
    private List<MeasurementReminderEntryDB> measurementReminderEntryDBList;
    private List<WeekScheduleDB> weekScheduleDBList;
    private List<ReminderTimeDB> reminderTimeDBList;
    private List<PillDB> pillDBList;
    private List<PillReminderDB> pillReminderDBList;
    private List<PillReminderEntryDB> pillReminderEntryDBList;
    private int type;
    private int userId;
    private Date synchronizationTimestamp;

    public ReminderSynchronizationReqModule(){
        cycleDBList = new ArrayList<>();
        measurementReminderDBList = new ArrayList<>();
        measurementReminderEntryDBList = new ArrayList<>();
        weekScheduleDBList = new ArrayList<>();
        reminderTimeDBList = new ArrayList<>();
        pillDBList = new ArrayList<>();
        pillReminderDBList = new ArrayList<>();
        pillReminderEntryDBList = new ArrayList<>();
    }
}
