package com.example.michel.rest_api.models.auxiliary_models.synchronization;

import com.example.michel.rest_api.models.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ReminderSynchronizationReqModule {
    private List<Cycle> cycleDBList;
    private List<MeasurementReminder> measurementReminderDBList;
    private List<MeasurementReminderEntry> measurementReminderEntryDBList;
    private List<WeekSchedule> weekScheduleDBList;
    private List<ReminderTime> reminderTimeDBList;
    private List<Pill> pillDBList;
    private List<PillReminder> pillReminderDBList;
    private List<PillReminderEntry> pillReminderEntryDBList;
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
