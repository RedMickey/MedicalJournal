package com.example.michel.mycalendar2.models;

public class MeasurementReminder extends ReminderModel {
    private int isActive;
    private int countOfTakingMeasurement;
    private String startDate;
    private String endDate;
    private int countOfTakingMeasurementLeft;
    private int idMeasurementType;

    public MeasurementReminder(int id, int idMeasurementType, int havingMealsType,
                               int isActive, int countOfTakingMeasurement, String startDate,
                               String endDate, int countOfTakingMeasurementLeft)
    {
        super(id, havingMealsType);
        this.isActive = isActive;
        this.countOfTakingMeasurement = countOfTakingMeasurement;
        this.startDate = startDate;
        this.endDate = endDate;
        this.countOfTakingMeasurementLeft = countOfTakingMeasurementLeft;
        this.idMeasurementType=idMeasurementType;
    }

    public int getIsActive() {
        return isActive;
    }

    public void setIsActive(int isActive) {
        this.isActive = isActive;
    }

    public int getCountOfTakingMeasurement() {
        return countOfTakingMeasurement;
    }

    public void setCountOfTakingMeasurement(int countOfTakingMeasurement) {
        this.countOfTakingMeasurement = countOfTakingMeasurement;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getCountOfTakingMeasurementLeft() {
        return countOfTakingMeasurementLeft;
    }

    public void setCountOfTakingMeasurementLeft(int countOfTakingMeasurementLeft) {
        this.countOfTakingMeasurementLeft = countOfTakingMeasurementLeft;
    }

    public int getIdMeasurementType() {
        return idMeasurementType;
    }

    public void setIdMeasurementType(int idMeasurementType) {
        this.idMeasurementType = idMeasurementType;
    }
}
