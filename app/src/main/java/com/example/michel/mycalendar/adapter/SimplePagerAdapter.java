package com.example.michel.mycalendar.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.example.michel.mycalendar.extensions.weekpager.adapter.WeekPagerAdapter;
import com.example.michel.mycalendar.fragment.SimpleFragment;

public class SimplePagerAdapter extends WeekPagerAdapter {

    public SimplePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override protected Fragment createFragmentPager(int position) {
        return SimpleFragment.newInstance(mDays.get(position));
    }
}
