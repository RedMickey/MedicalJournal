package com.example.michel.mycalendar2.utils.utilModels;

public class MeasurementType {
    private int index;
    private String name;
    private int idMeasurementValueType;

    public MeasurementType(int index, String name, int idMeasurementValueType){
        this.index = index;
        this.name = name;
        this.idMeasurementValueType = idMeasurementValueType;
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
}
