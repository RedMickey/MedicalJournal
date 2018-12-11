package com.example.michel.mycalendar2.models.pill;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.ReminderEntryModel;

import java.util.Date;

public class PillReminderEntry extends ReminderEntryModel implements Parcelable {
    private String pillName;
    private int pillCount;
    private String pillCountType;

    public PillReminderEntry(int id, String pillName, int pillCount, String pillCountType,
                             Date date, int havingMealsType, Date havingMealsTime, int isDone, boolean isLate)
    {
        super(id, havingMealsType, date, havingMealsTime, isDone, isLate);
        this.pillName = pillName;
        this.pillCount = pillCount;
        this.pillCountType = pillCountType;
    }

    public String getPillName() {
        return pillName;
    }

    public void setPillName(String pillName) {
        this.pillName = pillName;
    }

    public int getPillCount() {
        return pillCount;
    }

    public void setPillCount(int pillCount) {
        this.pillCount = pillCount;
    }

    public String getPillCountType() {
        return pillCountType;
    }

    public void setPillCountType(String pillCountType) {
        this.pillCountType = pillCountType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(havingMealsType);
        parcel.writeLong(date.getTime());
        parcel.writeLong(havingMealsTime.getTime());
        parcel.writeInt(isDone);
        parcel.writeString(pillName);
        parcel.writeInt(pillCount);
        parcel.writeString(pillCountType);

    }

    public PillReminderEntry(Parcel in){
        super(in.readInt(), in.readInt(), new Date(in.readLong()), new Date(in.readLong()), in.readInt(), false);
        this.pillName = in.readString();
        this.pillCount = in.readInt();
        this.pillCountType = in.readString();
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

    @Override
    public boolean equals(Object obj) {
        if (obj==this) return true;
        if (obj==null || obj.getClass()!=this.getClass()) return false;
        return (this.id==((PillReminderEntry) obj).getId()&&
                this.pillName.equals(((PillReminderEntry) obj).getPillName())&&
                this.pillCount==((PillReminderEntry) obj).pillCount&&
                this.pillCountType.equals(((PillReminderEntry) obj).getPillCountType())&&
                this.date.compareTo(((PillReminderEntry) obj).getDate())==0&&
                this.havingMealsType==((PillReminderEntry) obj).getHavingMealsType()&&
                this.havingMealsTime.compareTo(((PillReminderEntry) obj).getHavingMealsTime())==0&&
                this.isDone==((PillReminderEntry) obj).getIsDone()&&
                this.isLate==((PillReminderEntry) obj).isLate
        );
    }
}
