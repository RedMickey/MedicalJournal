package com.example.michel.mycalendar2.models.measurement;

import android.os.Parcel;
import android.os.Parcelable;

public class MeasurementStatEntry extends MeasurementReminder implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(idMeasurementType);
        parcel.writeInt(havingMealsType);
        parcel.writeInt(isActive);
        parcel.writeInt(numberOfDoingAction);
        parcel.writeString(startDate);
        parcel.writeString(endDate);
        parcel.writeInt(numberOfDoingActionLeft);
        parcel.writeInt(idMeasurementValueType);
        parcel.writeDoubleArray(averageCurValues);
        parcel.writeDoubleArray(standardValues);
        parcel.writeString(measurementValueTypeStr);
    }

    public MeasurementStatEntry(Parcel in) {
        super(in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readString(), in.readString(), in.readInt(), in.readInt());
        averageCurValues = in.createDoubleArray();
        standardValues = in.createDoubleArray();
        measurementValueTypeStr = in.readString();
    }

    public static final Creator<MeasurementStatEntry> CREATOR = new Creator<MeasurementStatEntry>() {
        @Override
        public MeasurementStatEntry createFromParcel(Parcel in) {
            return new MeasurementStatEntry(in);
        }

        @Override
        public MeasurementStatEntry[] newArray(int size) {
            return new MeasurementStatEntry[size];
        }
    };
}
