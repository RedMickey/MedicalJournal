package com.example.michel.mycalendar2.calendarview.views;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.calendarview.async_tasks.TasksViewCreationTask;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;

public class DayFragment extends Fragment{

    private DateData currentData;
    private TextView mText;

    public void setData(DateData currentDate){
        this.currentData = currentDate;
    }

    public static DayFragment newInstance(DateData currentData) {
        DayFragment dayFragment = new DayFragment();
        /*if (dayDelta>0)
            dayFragment.currentData = new DateData(currentData.getYear(), currentData.getMonth(), currentData.getDay()+dayDelta);
        else if (dayDelta == 0)
            dayFragment.currentData = currentData;
        else
            dayFragment.currentData = new DateData(currentData.getYear(), currentData.getMonth(), currentData.getDay()-dayDelta);*/
        dayFragment.currentData = currentData;
        return dayFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.day_fragment, container, false);
        return result;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((TextView) view.findViewById(R.id.text_current_date)).setText(currentData.getDayString()+" "+ CalendarUtil.getRusMonthName(currentData.getMonth(),1));
        //((TextView) view.findViewById(R.id.text_day_of_week)).setText(CalendarUtil.getDayOfWeekRusName(currentData));
        ((TextView) view.findViewById(R.id.text_day_of_week)).setText(CalendarUtil.getDayOfWeekRusName(currentData));
        TasksViewCreationTask tv = new TasksViewCreationTask(view);
        tv.execute(currentData);
        //mText = (TextView) view.findViewById(R.id.text);
        //mText.setText(currentData.getDayString());
    }
}
