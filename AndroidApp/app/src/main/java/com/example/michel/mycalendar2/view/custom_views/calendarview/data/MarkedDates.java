package com.example.michel.mycalendar2.view.custom_views.calendarview.data;

import java.util.ArrayList;
import java.util.Observable;

import com.example.michel.mycalendar2.view.custom_views.calendarview.MarkStyle;

/**
 * Created by bob.sun on 15/8/28.
 */
public class MarkedDates extends Observable {
    private static MarkedDates staticInstance;
    private ArrayList<DateData> data;

    private MarkedDates(){
        super();
        data = new ArrayList<>();
    }

    public static MarkedDates getInstance(){
        if (staticInstance == null)
            staticInstance = new MarkedDates();
        return staticInstance;
    }

    public static void destroyMarkedDates(){
        staticInstance = null;
    }

    public MarkStyle check(DateData date){
        int index = data.indexOf(date);
        if (index == -1) {
            return null;
        }
        return data.get(index).getMarkStyle();
    }

    public boolean remove(DateData date){
        this.setChanged();
        this.notifyObservers();
        return data.remove(date);

    }

    public MarkedDates add(DateData dateData){
        data.add(dateData);
        this.setChanged();
        this.notifyObservers();
        return this;
    }


    public ArrayList<DateData> getAll(){
        return data;
    }

    public MarkedDates removeAdd(){
        data.clear();
        this.setChanged();
        this.notifyObservers();
        return this;
    }
}
