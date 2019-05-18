package com.example.michel.mycalendar2.models;

import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;

public class CycleAndPillComby {
    public CycleDBInsertEntry cycleDBInsertEntry;
    public PillReminderDBInsertEntry pillReminderDBInsertEntry;

    public CycleAndPillComby(CycleDBInsertEntry cycleDBInsertEntry,
                             PillReminderDBInsertEntry pillReminderDBInsertEntry){
        this.cycleDBInsertEntry=cycleDBInsertEntry;
        this.pillReminderDBInsertEntry=pillReminderDBInsertEntry;
    }

    public CycleAndPillComby(){
        this.cycleDBInsertEntry=null;
        this.pillReminderDBInsertEntry=null;
    }
}
