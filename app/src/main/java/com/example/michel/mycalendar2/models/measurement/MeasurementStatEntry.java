package com.example.michel.mycalendar2.models.measurement;

public class MeasurementStatEntry extends MeasurementReminder {
    private double[] averageCurValues;
    private double[] standardValues;
    private String measurementValueTypeStr;

    public MeasurementStatEntry(int id, int idMeasurementType, int havingMealsType,
                                int isActive, int numberOfDoingAction, String startDate,
                                String endDate, int numberOfDoingActionLeft, int idMeasurementValueType,
                                double[] averageCurValues, double[] standardValues, String measurementValueTypeStr) {
        super(id, idMeasurementType, havingMealsType, isActive, numberOfDoingAction, startDate, endDate, numberOfDoingActionLeft, idMeasurementValueType);
        this.averageCurValues = averageCurValues;
        this.standardValues = standardValues;
        this.measurementValueTypeStr = measurementValueTypeStr;
    }

    public double[] getAverageCurValues() {
        return averageCurValues;
    }

    public void setAverageCurValues(double[] averageCurValues) {
        this.averageCurValues = averageCurValues;
    }

    public double[] getStandardValues() {
        return standardValues;
    }

    public void setStandardValues(double[] standardValues) {
        this.standardValues = standardValues;
    }

    public String getMeasurementValueTypeStr() {
        return measurementValueTypeStr;
    }

    public void setMeasurementValueTypeStr(String measurementValueTypeStr) {
        this.measurementValueTypeStr = measurementValueTypeStr;
    }
}
