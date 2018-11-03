package com.example.michel.mycalendar2.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.example.michel.mycalendar2.adapters.TreatmentListAdapter;
import com.example.michel.mycalendar2.auxiliary_fragments.ReminderListFragment;

public class ReminderFragment extends Fragment {
    public static ReminderFragment newInstance() {
        return new ReminderFragment();
    }
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getView();
        view=inflater.inflate(R.layout.reminder_fragment, container, false);

        viewPager = (ViewPager) view.findViewById(R.id.medicines_measurements_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.medicines_measurements_tabs);
        TreatmentListAdapter adapter = new TreatmentListAdapter(getChildFragmentManager());
        adapter.addFragment(ReminderListFragment.newInstance(0));
        adapter.addFragment(ReminderListFragment.newInstance(1));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!(viewPager.getAdapter() == null)) {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }
}
