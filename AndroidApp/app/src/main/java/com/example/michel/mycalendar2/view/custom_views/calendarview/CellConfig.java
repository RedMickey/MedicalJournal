package com.example.michel.mycalendar2.view.custom_views.calendarview;

import com.example.michel.mycalendar2.view.custom_views.calendarview.data.DateData;
import com.example.michel.mycalendar2.view.custom_views.calendarview.data.MarkedDates;

public class CellConfig {
    public static float cellWidth = 100;
    public static float cellHeight = 100;

    public static boolean isFirstStart = true;
    public static boolean ifMonth = false;

    public static int middlePosition = 500;

    public static int Month2WeekPos = 500;
    public static int Week2MonthPos = 500;

    public static DateData w2mPointDate;
    public static DateData m2wPointDate;

    public static DateData weekAnchorPointDate;

    public static DateData selectedDate;

    public static void resetAllDatas(){
        cellWidth = 100;
        cellHeight = 100;

        ifMonth = false;

        middlePosition = 500;

        Month2WeekPos = 500;
        Week2MonthPos = 500;

        w2mPointDate =null;
        m2wPointDate =null;

        weekAnchorPointDate =null;
        selectedDate = null;

        MarkedDates.getInstance().removeAdd();
        MarkedDates.destroyMarkedDates();
    }
}
