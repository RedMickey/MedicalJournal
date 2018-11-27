package com.example.michel.mycalendar2.models.measurement;

import com.example.michel.mycalendar2.models.ItemReminderModel;
import com.example.michel.mycalendar2.models.ReminderModel;

public class MeasurementReminder extends ItemReminderModel {
    private int idMeasurementType;
    private int idMeasurementValueType;

    public MeasurementReminder(int id, int idMeasurementType, int havingMealsType,
                               int isActive, int numberOfDoingAction, String startDate,
                               String endDate, int numberOfDoingActionLeft, int idMeasurementValueType)
    {
        super(id, havingMealsType, isActive, numberOfDoingAction, startDate, endDate, numberOfDoingActionLeft);
        this.idMeasurementType=idMeasurementType;
        this.idMeasurementValueType = idMeasurementValueType;
    }

    public int getIdMeasurementType() {
        return idMeasurementType;
    }

    public void setIdMeasurementType(int idMeasurementType) {
        this.idMeasurementType = idMeasurementType;
    }

    public int getIdMeasurementValueType() {
        return idMeasurementValueType;
    }

    public void setIdMeasurementValueType(int idMeasurementValueType) {
        this.idMeasurementValueType = idMeasurementValueType;
    }
}
