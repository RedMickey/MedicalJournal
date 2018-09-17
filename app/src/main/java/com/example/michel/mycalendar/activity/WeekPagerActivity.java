package com.example.michel.mycalendar.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.BindView;
import com.example.michel.mycalendar.extensions.weekpager.adapter.WeekViewAdapter;
import com.example.michel.mycalendar.extensions.weekpager.view.WeekDayViewPager;
import com.example.michel.mycalendar.extensions.weekpager.view.WeekRecyclerView;
import com.example.michel.mycalendar.extensions.model.CalendarDay;
import com.example.michel.mycalendar.extensions.util.DayUtils;
import com.example.michel.mycalendar.R;
import com.example.michel.mycalendar.adapter.SimplePagerAdapter;
import java.util.ArrayList;

/**
 * Created by tudou on 15-5-19.
 */
public class WeekPagerActivity extends AppCompatActivity implements WeekDayViewPager.DayScrollListener  {

    private final static String TAG = "WeekPagerActivity";

    @BindView(R.id.view_pager) WeekDayViewPager mViewPagerContent;
    @BindView(R.id.header_recycler_view) WeekRecyclerView mWeekRecyclerView;
    @BindView(R.id.text_day_label) TextView mTextView;

    private SimplePagerAdapter mPagerAdapter;
    private WeekViewAdapter mWeekViewAdapter;
    private static final int OFFSCREEN_PAGE_LIMIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_pager);
        ButterKnife.bind(this);
        setUpPager();
        setUpData();
    }


    private void setUpPager() {
        mPagerAdapter = new SimplePagerAdapter(getSupportFragmentManager());
        mViewPagerContent.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        mViewPagerContent.setAdapter(mPagerAdapter);
        mViewPagerContent.setWeekRecyclerView(mWeekRecyclerView);
        mViewPagerContent.setDayScrollListener(this);
        mWeekViewAdapter = new WeekViewAdapter(this, mViewPagerContent);
        mWeekViewAdapter.setTextNormalColor(getResources().getColor(android.R.color.darker_gray));
        mWeekRecyclerView.setAdapter(mWeekViewAdapter);
    }

    private void setUpData() {
        ArrayList<CalendarDay> reachAbleDays = new ArrayList<>();
        reachAbleDays.add(new CalendarDay(1970, 5, 1));
        reachAbleDays.add(new CalendarDay(2015, 5, 4));
        reachAbleDays.add(new CalendarDay(2015, 5, 6));
        reachAbleDays.add(new CalendarDay(2015, 5, 20));
        mWeekViewAdapter.setData(reachAbleDays.get(0), reachAbleDays.get(reachAbleDays.size() - 1), null);
        mPagerAdapter.setData(reachAbleDays.get(0), reachAbleDays.get(reachAbleDays.size() - 1));
        mViewPagerContent.setCurrentPosition(DayUtils.calculateDayPosition(mWeekViewAdapter.getFirstShowDay(), new CalendarDay(1971, 4, 23)));
        //mViewPagerContent.setCurrentPosition(1500);
    }

    @Override public void onDayPageScrolled(int position, float positionOffset,
                                            int positionOffsetPixels) {
        mTextView.setText(DayUtils.formatEnglishTime(mPagerAdapter.getDatas().get(position).getTime()));
    }

    @Override public void onDayPageSelected(int position) {
    }

    @Override public void onDayPageScrollStateChanged(int state) {

    }

}
