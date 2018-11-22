package com.example.michel.mycalendar2.models.pill;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.michel.mycalendar2.models.PillModel;

import java.util.Calendar;
import java.util.Date;

public class PillReminderEntry extends PillModel implements Parcelable {
    private int isDone;
    private Date date;
    private Date havingMealsTime;
    private boolean isLate;

    public PillReminderEntry(int id, String pillName, int pillCount, String pillCountType,
                             Date date, int havingMealsType, Date havingMealsTime, int isDone, boolean isLate)
    {
        super(id, pillName, pillCount, pillCountType, havingMealsType);
        this.date = date;
        this.havingMealsTime = havingMealsTime;
        this.isDone = isDone;
        this.isLate=isLate;
    }

    public boolean isLateCheck(){
        Calendar calendar = Calendar.getInstance();
        isLate =  calendar.getTime().compareTo(date)>0?true:false;
        return isLate;
    }

    public int getIsDone() {
        return isDone;
    }

    public void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getHavingMealsTime() {
        return havingMealsTime;
    }

    public boolean isLate() {
        return isLate;
    }

    public void setLate(boolean late) {
        isLate = late;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(pillName);
        parcel.writeInt(pillCount);
        parcel.writeString(pillCountType);
        parcel.writeInt(havingMealsType);
        parcel.writeLong(date.getTime());
        parcel.writeLong(havingMealsTime.getTime());
        parcel.writeInt(isDone);
    }

    public PillReminderEntry(Parcel in){
        super(in.readInt(), in.readString(), in.readInt(), in.readString(), in.readInt());
        this.date = new Date(in.readLong());
        this.havingMealsTime= new Date(in.readLong());
        this.isDone = in.readInt();
        this.isLate=false;
    }

    public static final Parcelable.Creator<PillReminderEntry> CREATOR = new Parcelable.Creator<PillReminderEntry>(){
        @Override
        public PillReminderEntry createFromParcel(Parcel parcel) {
            return new PillReminderEntry(parcel);
        }

        @Override
        public PillReminderEntry[] newArray(int i) {
            return new PillReminderEntry[i];
        }
    };
}
