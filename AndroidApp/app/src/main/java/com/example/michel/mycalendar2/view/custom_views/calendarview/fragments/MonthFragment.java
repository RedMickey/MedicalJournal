package com.example.michel.mycalendar2.view.custom_views.calendarview.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.view.custom_views.calendarview.adapters.CalendarAdapter;
import com.example.michel.mycalendar2.view.custom_views.calendarview.views.MonthView;
import com.example.michel.mycalendar2.view.custom_views.calendarview.data.MonthData;

public class MonthFragment extends Fragment {
    private MonthData monthData;
    private int cellView = -1;
    private int markView = -1;
    private boolean hasTitle = true;

    public void setData(MonthData monthData, int cellView, int markView) {
        this.monthData = monthData;
        this.cellView = cellView;
        this.markView = markView;
    }

    public void setTitle(boolean hasTitle) {
        this.hasTitle = hasTitle;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        LinearLayout ret = new LinearLayout(getContext());
        ret.setOrientation(LinearLayout.VERTICAL);
        ret.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ret.setGravity(Gravity.CENTER);
        if ((monthData != null) && (monthData.getDate() != null)) {
            if (hasTitle) {
                TextView textView = new TextView(getContext());
                textView.setText(String.format("%d-%d", monthData.getDate().getYear(), monthData.getDate().getMonth()));
                ret.addView(textView);
            }
            MonthView monthView = new MonthView(getContext());
            monthView.setAdapter(new CalendarAdapter(getContext(), 1, monthData.getData()).setCellViews(cellView, markView));
            ret.addView(monthView);
        }
        return ret;
    }
}
