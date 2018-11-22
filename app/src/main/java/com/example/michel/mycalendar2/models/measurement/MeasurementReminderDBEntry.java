package com.example.michel.mycalendar2.models.measurement;

import android.support.annotation.Nullable;

import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.models.ReminderDBEntry;
import com.example.michel.mycalendar2.models.ReminderTime;

public class MeasurementReminderDBEntry extends ReminderDBEntry {
    private int idMeasurementType;
    private Integer idMeasurementReminder;

    public MeasurementReminderDBEntry(int idMeasurementType, Integer idMeasurementReminder,
                                    DateData startDate, Integer idCycle, @Nullable Integer idHavingMealsType,
                                    @Nullable Integer havingMealsTime, String annotation, Integer isActive,
                                    ReminderTime[] reminderTimes)
    {
        super(startDate, idCycle, idHavingMealsType, havingMealsTime, annotation,
                isActive, reminderTimes);
        this.idMeasurementType = idMeasurementType;
        this.idMeasurementReminder = idMeasurementReminder;
    }

    public MeasurementReminderDBEntry()
    {
        this.idMeasurementType=0;
        this.idMeasurementReminder = null;
    }

    public int getIdMeasurementType() {
        return idMeasurementType;
    }

    public void setIdMeasurementType(int idMeasurementType) {
        this.idMeasurementType = idMeasurementType;
    }

    public Integer getIdMeasurementReminder() {
        return idMeasurementReminder;
    }

    public void setIdMeasurementReminder(Integer idMeasurementReminder) {
        this.idMeasurementReminder = idMeasurementReminder;
    }
}
