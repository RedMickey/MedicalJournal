package com.example.michel.mycalendar2.models;

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
