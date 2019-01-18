package com.example.michel.mycalendar2.calendarview.listeners;

import android.view.View;

import com.example.michel.mycalendar2.calendarview.utils.CurrentCalendar;
import com.example.michel.mycalendar2.calendarview.views.DefaultCellView;
import com.example.michel.mycalendar2.calendarview.data.DateData;

public class OnExpDateClickListener extends OnDateClickListener {

    private View lastClickedView;
    private DateData lastClickedDate = CurrentCalendar.getCurrentDateData();

    @Override
    public void onDateClick(View view, DateData date) {

        if(view instanceof DefaultCellView) {

            if (lastClickedView != null) {

                if (lastClickedView == view)
                    return;
                if (lastClickedDate.equals(CurrentCalendar.getCurrentDateData())) {
                    ((DefaultCellView) lastClickedView).setDateToday();
                } else {
                    ((DefaultCellView) lastClickedView).setDateNormal();
                }
            }

            ((DefaultCellView) view).setDateChoose();
            lastClickedView = view;
            lastClickedDate = date;
        }


    }

}
