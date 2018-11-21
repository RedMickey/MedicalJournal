package com.example.michel.mycalendar2.models;

public class CycleAndMeasurementComby {
    public CycleDBInsertEntry cycleDBInsertEntry;
    public MeasurementReminderDBEntry measurementReminderDBEntry;

    public CycleAndMeasurementComby(CycleDBInsertEntry cycleDBInsertEntry,
                                    MeasurementReminderDBEntry measurementReminderDBEntry){
        this.cycleDBInsertEntry=cycleDBInsertEntry;
        this.measurementReminderDBEntry=measurementReminderDBEntry;
    }

    public CycleAndMeasurementComby(){
        this.cycleDBInsertEntry=null;
        this.measurementReminderDBEntry=null;
    }
}
