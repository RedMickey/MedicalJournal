package com.example.michel.mycalendar2.view.custom_views.calendarview.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.michel.mycalendar2.view.custom_views.calendarview.listeners.OnDateClickListener;
import com.example.michel.mycalendar2.view.custom_views.calendarview.data.DateData;

public abstract class BaseMarkView extends BaseCellView{
    private OnDateClickListener clickListener;
    private DateData date;

    public BaseMarkView(Context context) {
        super(context);
    }

    public BaseMarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

}
