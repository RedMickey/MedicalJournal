package com.example.michel.mycalendar2.view.custom_views.calendarview.data;

public class DayDifference {
    private int days;
    private boolean swipeRight;

    public DayDifference(int days, boolean swipeRight){
        this.days = days;
        this.swipeRight = swipeRight;
    }

    public int getDays() {
        return days;
    }

    public boolean isSwipeRight() {
        return swipeRight;
    }
}
