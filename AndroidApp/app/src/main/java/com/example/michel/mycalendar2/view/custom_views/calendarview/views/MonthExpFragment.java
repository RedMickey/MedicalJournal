package com.example.michel.mycalendar2.view.custom_views.calendarview.views;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class MonthExpFragment extends Fragment {
    private int cellView = -1;
    private int markView = -1;
    private int pagePosition ;

    private MonthViewExpd monthViewExpd;
    private boolean ifExpand = false;

    public MonthViewExpd getMonthViewExpd() {
        return monthViewExpd;
    }

    public void setData(int pagePosition, int cellView, int markView) {
        this.pagePosition = pagePosition;
        this.cellView = cellView;
        this.markView = markView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        LinearLayout ret = new LinearLayout(getContext());
        ret.setBackgroundColor(Color.WHITE);
        ret.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        monthViewExpd = new MonthViewExpd(getContext());
        monthViewExpd.initMonthAdapter(pagePosition, cellView, markView);
        ret.addView(monthViewExpd);

        return ret;
    }

}
