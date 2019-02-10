package com.example.michel.mycalendar2.models.measurement;

import android.support.annotation.Nullable;

import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.models.ReminderDBEntry;
import com.example.michel.mycalendar2.models.ReminderTime;

import java.util.UUID;

public class MeasurementReminderDBEntry extends ReminderDBEntry {
    private int idMeasurementType;
    private UUID idMeasurementReminder;
    private int idMeasurementValueType;

    public MeasurementReminderDBEntry(int idMeasurementType, UUID idMeasurementReminder,
                                    DateData startDate, UUID idCycle, @Nullable Integer idHavingMealsType,
                                    @Nullable Integer havingMealsTime, String annotation, Integer isActive,
                                    ReminderTime[] reminderTimes, int idMeasurementValueType)
    {
        super(startDate, idCycle, idHavingMealsType, havingMealsTime, annotation,
                isActive, reminderTimes);
        this.idMeasurementType = idMeasurementType;
        this.idMeasurementReminder = idMeasurementReminder;
        this.idMeasurementValueType = idMeasurementValueType;
    }

    public MeasurementReminderDBEntry()
    {
        this.idMeasurementType=0;
        this.idMeasurementReminder = null;
        this.idMeasurementValueType = 0;
    }

    public int getIdMeasurementType() {
        return idMeasurementType;
    }

    public void setIdMeasurementType(int idMeasurementType) {
        this.idMeasurementType = idMeasurementType;
    }

    public UUID getIdMeasurementReminder() {
        return idMeasurementReminder;
    }

    public void setIdMeasurementReminder(UUID idMeasurementReminder) {
        this.idMeasurementReminder = idMeasurementReminder;
    }

    public int getIdMeasurementValueType() {
        return idMeasurementValueType;
    }

    public void setIdMeasurementValueType(int idMeasurementValueType) {
        this.idMeasurementValueType = idMeasurementValueType;
    }
}
