package com.example.michel.mycalendar2.calendarview.views;

import android.content.Context;
import android.util.AttributeSet;

import com.example.michel.mycalendar2.calendarview.adapters.CalendarExpAdapter;
import com.example.michel.mycalendar2.calendarview.data.MonthWeekData;

import java.util.ArrayList;

public class MonthViewExpd extends MonthView {
    private MonthWeekData monthWeekData;
    private CalendarExpAdapter adapter;

    public MonthViewExpd(Context context) {
        super(context);
    }

    public MonthViewExpd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initMonthAdapter(int pagePosition, int cellView, int markView) {
        getMonthWeekData(pagePosition);
        adapter = new CalendarExpAdapter(getContext(), 1, monthWeekData.getData()).setCellViews(cellView, markView);
        this.setAdapter(adapter);
    }

    public ArrayList getMonthWeekData() {
        return monthWeekData.getData();
    }

    @Override
    public CalendarExpAdapter getAdapter() {
        return adapter;
    }

    private void getMonthWeekData(int position) {
        monthWeekData = new MonthWeekData(position);
    }


}
