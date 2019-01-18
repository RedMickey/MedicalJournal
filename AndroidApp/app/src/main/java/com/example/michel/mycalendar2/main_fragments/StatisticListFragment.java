package com.example.michel.mycalendar2.main_fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.adapters.StatisticListAdapter;
import com.example.michel.mycalendar2.app_async_tasks.StatisticListItemsCreationTask;

import java.util.ArrayList;
import java.util.List;

public class StatisticListFragment extends Fragment {
    public static StatisticListFragment newInstance() {
        return new StatisticListFragment();
    }
    private View mView;
    private Spinner toolbarSpinner;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = getView();
        mView=inflater.inflate(R.layout.statistic_list_fragment, container, false);
        toolbarSpinner = (Spinner)getActivity().findViewById(R.id.toolbar_quantity_intervals_spinner);
        toolbarSpinner.setSelection(0);

        StatisticListItemsCreationTask slict = new StatisticListItemsCreationTask(mView);
        slict.execute(10);

        toolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //RecyclerView statisticRecycleView = (RecyclerView)mView.findViewById(R.id.statistic_recycle_view);
                StatisticListItemsCreationTask slict = new StatisticListItemsCreationTask(mView);

                switch (i){
                    case 0:
                        slict.execute(10);
                        break;
                    case 1:
                        slict.execute(20);
                        break;
                    case 2:
                        slict.execute(90);
                        break;
                    case 3:
                        slict.execute(-1);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return mView;
    }
}
