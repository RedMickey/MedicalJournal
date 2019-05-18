package com.example.michel.mycalendar2.calendarview.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.example.michel.mycalendar2.calendarview.MarkStyle;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.listeners.OnDateClickListener;
import com.example.michel.mycalendar2.calendarview.utils.CurrentCalendar;
import com.example.michel.mycalendar2.calendarview.views.BaseCellView;
import com.example.michel.mycalendar2.calendarview.views.BaseMarkView;
import com.example.michel.mycalendar2.calendarview.views.DefaultCellView;
import com.example.michel.mycalendar2.calendarview.views.DefaultMarkView;
import com.example.michel.mycalendar2.calendarview.data.DayData;
import com.example.michel.mycalendar2.calendarview.data.MarkedDates;

public class CalendarExpAdapter extends ArrayAdapter implements Observer {
    private ArrayList<DayData> data;
    private int cellView = -1;
    private int markView = -1;

    public CalendarExpAdapter(Context context, int resource, ArrayList data) {
        super(context, resource);
        this.data = data;
        MarkedDates.getInstance().addObserver(this);
    }

    public CalendarExpAdapter setCellViews(int cellView, int markView) {
        this.cellView = cellView;
        this.markView = markView;
        return this;
    }

    public ArrayList getData() {
        return data;
    }

    public DateData findItemInListByDate(DateData dateExample){
        for (DayData dt: data) {
            DateData buf = dt.getDate();
            if (buf.getDay()==dateExample.getDay()&&buf.getMonth()==dateExample.getMonth()&&buf.getYear()==dateExample.getYear())
                return buf;
        }
        return null;
    }

    public boolean listContainItemByDate(DateData dateExample){
        for (DayData dt: data) {
            DateData buf = dt.getDate();
            if (buf.getDay()==dateExample.getDay()&&buf.getMonth()==dateExample.getMonth()&&buf.getYear()==dateExample.getYear())
                return true;
        }
        return false;
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View ret = null;
        DayData dayData = (DayData) data.get(position);
        MarkStyle style = MarkedDates.getInstance().check(dayData.getDate());
        boolean marked = style != null;
        if (marked) {
            dayData.getDate().setMarkStyle(style);
            if (markView > 0) {
                BaseMarkView baseMarkView = (BaseMarkView) View.inflate(getContext(), markView, null);
                baseMarkView.setDisplayText(dayData);
                ret = baseMarkView;
            } else {
                ret = new DefaultMarkView(getContext());
                ((DefaultMarkView) ret).setDisplayText(dayData);
            }
        } else {
            if (cellView > 0) {
                BaseCellView baseCellView = (BaseCellView) View.inflate(getContext(), cellView, null);
                baseCellView.setDisplayText(dayData);
                ret = baseCellView;
            } else {
                ret = new DefaultCellView(getContext());
                ((DefaultCellView) ret).setTextColor(dayData.getText(), dayData.getTextColor());
            }
        }
        ((BaseCellView) ret).setDate(dayData.getDate());
        if (OnDateClickListener.instance != null) {
            ((BaseCellView) ret).setOnDateClickListener(OnDateClickListener.instance);
        }
        if (dayData.getDate().equals(CurrentCalendar.getCurrentDateData()) &&
                ret instanceof DefaultCellView) {
            ((DefaultCellView) ret).setDateToday();

        }
        return ret;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public void update(Observable observable, Object data) {
        this.notifyDataSetChanged();
    }
}