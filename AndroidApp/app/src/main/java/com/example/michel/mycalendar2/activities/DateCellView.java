package com.example.michel.mycalendar2.activities;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.michel.mycalendar2.calendarview.views.BaseCellView;
import com.example.michel.mycalendar2.calendarview.data.DayData;

/**
 * Created by bob.sun on 15/8/30.
 */
public class DateCellView extends BaseCellView {
    public DateCellView(Context context) {
        super(context);
    }

    public DateCellView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setDisplayText(DayData day) {
        ((TextView) this.findViewById(R.id.id_cell_text)).setText(day.getText());
    }
}
