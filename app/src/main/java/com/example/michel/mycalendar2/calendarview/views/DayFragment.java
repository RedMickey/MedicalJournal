package com.example.michel.mycalendar2.calendarview.views;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.data.DayData;

public class DayFragment extends Fragment{

    private DateData dayData;
    private TextView mText;

    public void setData(DateData currentDate){
        this.dayData = currentDate;
    }

    public static DayFragment newInstance(DateData dayData) {
        DayFragment dayFragment = new DayFragment();
        dayFragment.dayData = dayData;
        return dayFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.day_fragment, container, false);
        TextView mText2 = (TextView)result.findViewById(R.id.text);
        mText2.setText("123456");
        return result;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mText = (TextView) view.findViewById(R.id.text);
        mText.setText(dayData.getDayString());
    }
}
