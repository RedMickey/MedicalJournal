package com.example.michel.mycalendar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import butterknife.ButterKnife;
import butterknife.BindView;
import com.example.michel.mycalendar.extensions.monthswitchpager.view.MonthSwitchView;
import com.example.michel.mycalendar.extensions.monthswitchpager.view.MonthView;
import com.example.michel.mycalendar.extensions.model.CalendarDay;
import com.example.michel.mycalendar.R;

import java.util.Calendar;

/**
 * Created by tudou on 15-5-19.
 */
public class MonthSwitchActivity extends AppCompatActivity implements MonthView.OnDayClickListener {

    @BindView(R.id.view_month) MonthSwitchView mMonthPagerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_switch);
        ButterKnife.bind(this);

        updateData();
    }

    private void updateData() {
        mMonthPagerView.setData(new CalendarDay(1970, 1, 1), new CalendarDay(3000, 1, 1));
        mMonthPagerView.setOnDayClickListener(this);
        Calendar calendar = Calendar.getInstance();
        mMonthPagerView.setSelectDay(new CalendarDay(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)+1));
    }

    @Override public void onDayClick(CalendarDay calendarDay) {
        Toast.makeText(this, calendarDay.getDayString(), Toast.LENGTH_SHORT).show();
    }
}
