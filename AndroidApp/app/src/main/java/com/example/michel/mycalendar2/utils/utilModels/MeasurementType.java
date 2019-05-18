package com.example.michel.mycalendar2.utils.utilModels;

public class MeasurementType {
    private int index;
    private String name;
    private int idMeasurementValueType;
    private double[] standardValues;
    private String measurementValueTypeName;

    public MeasurementType(int index, String name, int idMeasurementValueType, String measurementValueTypeName, double[] standardValues){
        this.index = index;
        this.name = name;
        this.idMeasurementValueType = idMeasurementValueType;
        this.standardValues = standardValues;
        this.measurementValueTypeName = measurementValueTypeName;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIdMeasurementValueType() {
        return idMeasurementValueType;
    }

    public void setIdMeasurementValueType(int idMeasurementValueType) {
        this.idMeasurementValueType = idMeasurementValueType;
    }

    public double[] getStandardValues() {
        return standardValues;
    }

    public void setStandardValues(double[] standardValues) {
        this.standardValues = standardValues;
    }

    public String getMeasurementValueTypeName() {
        return measurementValueTypeName;
    }

    public void setMeasurementValueTypeName(String measurementValueTypeName) {
        this.measurementValueTypeName = measurementValueTypeName;
    }
}
