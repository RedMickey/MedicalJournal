package com.example.michel.mycalendar2.calendarview.views;

import android.content.Context;
import android.util.AttributeSet;

import com.example.michel.mycalendar2.calendarview.adapters.CalendarExpAdapter;
import com.example.michel.mycalendar2.calendarview.data.MonthWeekData;

/**
 * Created by Bigflower on 2015/12/8.
 */
public class MonthViewExpd extends MonthView {
    private MonthWeekData monthWeekData;
    private CalendarExpAdapter adapter;

    public MonthViewExpd(Context context) {
        super(context);
    }

    public MonthViewExpd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void initMonthAdapter(int pagePosition, int cellView, int markView, boolean ifExpand) {
        getMonthWeekData(pagePosition, ifExpand);
        adapter = new CalendarExpAdapter(getContext(), 1, monthWeekData.getData(ifExpand)).setCellViews(cellView, markView);
        this.setAdapter(adapter);
    }

    private void getMonthWeekData(int position, boolean ifExpand) {
        monthWeekData = new MonthWeekData(position, ifExpand);
    }


}
