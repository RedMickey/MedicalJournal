package com.example.michel.mycalendar.extensions.expandcalendar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.example.michel.mycalendar.extensions.expandcalendar.view.ExpandCalendarMonthView;
import com.example.michel.mycalendar.extensions.model.CalendarDay;
import com.example.michel.mycalendar.extensions.util.DayUtils;
import java.util.ArrayList;

public class MonthViewAdapter extends RecyclerView.Adapter<MonthViewAdapter.MonthViewHolder> implements
        ExpandCalendarMonthView.OnDayClickListener {

    private Context mContext;
    private CalendarDay mStartDay;
    private CalendarDay mEndDay;
    private CalendarDay mSelectCalendarDay;
    private ArrayList<CalendarDay> mAbleCalendayDays;
    private ExpandCalendarMonthView.OnDayClickListener mOnDayClickListener;

    public MonthViewAdapter(Context context, ExpandCalendarMonthView.OnDayClickListener onDayClickListener) {
        mContext = context;
        mOnDayClickListener = onDayClickListener;
        mAbleCalendayDays = new ArrayList<>();
        mSelectCalendarDay = new CalendarDay(System.currentTimeMillis());
    }

    public void setData(CalendarDay startDay, CalendarDay endDay, ArrayList<CalendarDay> calendarDayArrayList) {
        mStartDay = startDay;
        mEndDay = endDay;
        if (calendarDayArrayList != null) {
            mAbleCalendayDays.clear();
            mAbleCalendayDays.addAll(calendarDayArrayList);
        }
        notifyDataSetChanged();
    }

    public CalendarDay getStartDay() {
        if (mStartDay == null) {
            try {
                throw new Exception("The StartDay must initial before the select Day!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            return mStartDay;
        }
        return null;
    }

    public void setSelectDay(CalendarDay calendarDay) {
        if (calendarDay == null) return;
        mSelectCalendarDay = calendarDay;
        notifyDataSetChanged();
    }

    public CalendarDay getSelectDay() {
        return mSelectCalendarDay;
    }

    @Override
    public int getItemCount() {
        if (mStartDay == null || mEndDay == null) {
            return 0;
        }
        int monthCount = DayUtils.calculateMonthCount(mStartDay, mEndDay);
        return monthCount;
    }

    @Override
    public MonthViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        ExpandCalendarMonthView expandCalendarMonthView = new ExpandCalendarMonthView(mContext);
        expandCalendarMonthView.setOnDayClickListener(this);
        int width = mContext.getResources().getDisplayMetrics().widthPixels;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                ViewGroup.LayoutParams.MATCH_PARENT);
        expandCalendarMonthView.setLayoutParams(params);
        MonthViewHolder viewHolder = new MonthViewHolder(expandCalendarMonthView, mStartDay, mAbleCalendayDays);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MonthViewHolder viewHolder, final int position) {
        viewHolder.bind(position, mSelectCalendarDay);
    }

    @Override public void onDayClick(CalendarDay calendarDay) {
        mSelectCalendarDay = calendarDay;
        mOnDayClickListener.onDayClick(calendarDay);
        notifyDataSetChanged();
    }

    public static class MonthViewHolder extends RecyclerView.ViewHolder {

        ExpandCalendarMonthView expandCalendarMonthView;

        public MonthViewHolder(View view,CalendarDay startDay, ArrayList<CalendarDay> mAbleCalendayDays) {
            super(view);
            expandCalendarMonthView = (ExpandCalendarMonthView) view;
            expandCalendarMonthView.setFirstDay(startDay);
        }

        public void bind(int position, CalendarDay calendarDay) {
            expandCalendarMonthView.setSelectDay(calendarDay);
            expandCalendarMonthView.setMonthPosition(position);
        }

    }
}
