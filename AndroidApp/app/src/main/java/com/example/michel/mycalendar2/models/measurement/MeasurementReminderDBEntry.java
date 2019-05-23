package com.example.michel.mycalendar2.models.measurement;

import android.support.annotation.Nullable;

import com.example.michel.mycalendar2.view.custom_views.calendarview.data.DateData;
import com.example.michel.mycalendar2.models.ReminderDBEntry;
import com.example.michel.mycalendar2.models.ReminderTime;

import java.util.UUID;

public class MeasurementReminderDBEntry extends ReminderDBEntry {
    private int idMeasurementType;
    private UUID idMeasurementReminder;
    private int idMeasurementValueType;
    private int isGfitListening;

    public MeasurementReminderDBEntry(int idMeasurementType, UUID idMeasurementReminder,
                                    DateData startDate, UUID idCycle, @Nullable Integer idHavingMealsType,
                                    @Nullable Integer havingMealsTime, String annotation, Integer isActive,
                                    ReminderTime[] reminderTimes, int idMeasurementValueType, int isGfitListening)
    {
        super(startDate, idCycle, idHavingMealsType, havingMealsTime, annotation,
                isActive, reminderTimes);
        this.idMeasurementType = idMeasurementType;
        this.idMeasurementReminder = idMeasurementReminder;
        this.idMeasurementValueType = idMeasurementValueType;
        this.isGfitListening = isGfitListening;
    }

    public MeasurementReminderDBEntry()
    {
        this.idMeasurementType=0;
        this.idMeasurementReminder = null;
        this.idMeasurementValueType = 0;
        this.isGfitListening = 0;
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

    public int getIsGfitListening() {
        return isGfitListening;
    }

    public void setIsGfitListening(int isGfitListening) {
        this.isGfitListening = isGfitListening;
    }
}
