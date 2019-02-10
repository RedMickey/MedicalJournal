package com.example.michel.mycalendar2.models.measurement;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.michel.mycalendar2.models.ReminderEntryModel;

import java.util.Date;
import java.util.UUID;

public class MeasurementReminderEntry extends ReminderEntryModel implements Parcelable {
    private double value1;
    private double value2;
    private int idMeasurementType;
    private String measurementTypeName;
    private String measurementValueTypeName;

    public MeasurementReminderEntry(UUID id, int havingMealsType,
                                    int idMeasurementType, String measurementValueTypeName, Date date,
                                    Date havingMealsTime, int isDone, boolean isLate,
                                    double value1, double value2, String measurementTypeName)
    {
        super(id, havingMealsType, date, havingMealsTime, isDone, isLate);
        this.idMeasurementType = idMeasurementType;
        this.measurementValueTypeName = measurementValueTypeName;
        this.value1 = value1;
        this.value2 = value2;
        this.measurementTypeName = measurementTypeName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id.toString());
        parcel.writeInt(havingMealsType);
        parcel.writeLong(date.getTime());
        parcel.writeLong(havingMealsTime.getTime());
        parcel.writeInt(isDone);
        parcel.writeInt(idMeasurementType);
        parcel.writeString(measurementValueTypeName);
        parcel.writeDouble(value1);
        parcel.writeDouble(value2);
        parcel.writeString(measurementTypeName);
    }

    public MeasurementReminderEntry(Parcel in){
        super(UUID.fromString(in.readString()), in.readInt(), new Date(in.readLong()), new Date(in.readLong()), in.readInt(), false);
        this.idMeasurementType = in.readInt();
        this.measurementValueTypeName = in.readString();
        this.value1 = in.readDouble();
        this.value2 = in.readDouble();
        this.measurementTypeName = in.readString();
    }

    public static final Parcelable.Creator<MeasurementReminderEntry> CREATOR = new Creator<MeasurementReminderEntry>() {
        @Override
        public MeasurementReminderEntry createFromParcel(Parcel parcel) {
            return new MeasurementReminderEntry(parcel);
        }

        @Override
        public MeasurementReminderEntry[] newArray(int i) {
            return new MeasurementReminderEntry[i];
        }
    };

    public double getValue1() {
        return value1;
    }

    public void setValue1(double value1) {
        this.value1 = value1;
    }

    public double getValue2() {
        return value2;
    }

    public void setValue2(double value2) {
        this.value2 = value2;
    }

    public int getIdMeasurementType() {
        return idMeasurementType;
    }

    public void setIdMeasurementType(int idMeasurementType) {
        this.idMeasurementType = idMeasurementType;
    }

    public String getMeasurementValueTypeName() {
        return measurementValueTypeName;
    }

    public void setMeasurementValueTypeName(String measurementValueTypeName) {
        this.measurementValueTypeName = measurementValueTypeName;
    }

    public String getMeasurementTypeName() {
        return measurementTypeName;
    }

    public void setMeasurementTypeName(String measurementTypeName) {
        this.measurementTypeName = measurementTypeName;
    }
}
