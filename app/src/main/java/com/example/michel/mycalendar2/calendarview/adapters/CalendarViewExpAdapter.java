package com.example.michel.mycalendar2.calendarview.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.example.michel.mycalendar2.calendarview.views.ExpCalendarView;
import com.example.michel.mycalendar2.calendarview.views.MonthExpFragment;
import com.example.michel.mycalendar2.calendarview.data.DateData;

public class CalendarViewExpAdapter extends FragmentStatePagerAdapter {

    private DateData date;

    private int dateCellId;
    private int markCellId;
    private boolean hasTitle = true;
    private boolean ifExpand;

    private Context context;
    private int mCurrentPosition = -1;

    public CalendarViewExpAdapter(FragmentManager fm, boolean ifExpand) {
        super(fm);
        this.ifExpand = ifExpand;
    }

    public void setIfExpand(boolean ifExpand){
        this.ifExpand = ifExpand;
    }

    public CalendarViewExpAdapter setDate(DateData date){
        this.date = date;
        return this;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public CalendarViewExpAdapter setDateCellId(int dateCellRes){
        this.dateCellId =  dateCellRes;
        return this;
    }


    public CalendarViewExpAdapter setMarkCellId(int markCellId){
        this.markCellId = markCellId;
        return this;
    }


    @Override
    public Fragment getItem(int position) {
        MonthExpFragment fragment = new MonthExpFragment();
        fragment.setData(position, dateCellId, markCellId, this.ifExpand);
        return fragment;
    }

    @Override
    public int getCount() {
        return 1000;
    }

    public CalendarViewExpAdapter setTitle(boolean hasTitle){
        this.hasTitle = hasTitle;
        return this;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
        ((ExpCalendarView) container).measureCurrentView(position);
    }

    /**
     * 重写该方法，为了刷新页面
     * @param object
     * @return
     */
    @Override
    public int getItemPosition(Object object) {
        if (object.getClass().getName().equals(MonthExpFragment.class.getName())) {
            return POSITION_NONE;
        }
        return super.getItemPosition(object);
    }

}
