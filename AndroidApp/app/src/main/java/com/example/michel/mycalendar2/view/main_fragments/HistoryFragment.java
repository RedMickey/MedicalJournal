package com.example.michel.mycalendar2.view.main_fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.controllers.app_async_tasks.DailyHistoryItemsCreationTask;
import com.example.michel.mycalendar2.view.custom_views.calendarview.data.DateData;

import java.util.Calendar;

public class HistoryFragment extends Fragment {
    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }
    private View mView;
    private Spinner toolbarSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = getView();
        mView=inflater.inflate(R.layout.history_fragment, container, false);
        toolbarSpinner = (Spinner)getActivity().findViewById(R.id.toolbar_time_intervals_spinner);
        toolbarSpinner.setSelection(0);
        Calendar cal = Calendar.getInstance();

        DateData endDate = new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
        cal.add(Calendar.DAY_OF_MONTH, -7);
        DateData startDate = new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));

        DailyHistoryItemsCreationTask dhict = new DailyHistoryItemsCreationTask(mView);
        dhict.execute(startDate, endDate);

        toolbarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((LinearLayout)mView.findViewById(R.id.history_linear_layout)).removeAllViews();
                Calendar cal = Calendar.getInstance();
                DateData endDate = new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
                DailyHistoryItemsCreationTask dhict = new DailyHistoryItemsCreationTask(mView);

                switch (i){
                    case 0:
                        cal.add(Calendar.DAY_OF_MONTH, -7);
                        DateData startDate = new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
                        dhict.execute(startDate, endDate);
                        break;
                    case 1:
                        cal.add(Calendar.DAY_OF_MONTH, -30);
                        DateData startDate2 = new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
                        dhict.execute(startDate2, endDate);
                        break;
                    case 2:
                        cal.add(Calendar.DAY_OF_MONTH, -90);
                        DateData startDate3 = new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
                        dhict.execute(startDate3, endDate);
                        break;
                    case 3:
                        DateData startDate4 = new DateData();
                        dhict.execute(startDate4, endDate);
                        break;
                }
                //Toast.makeText(mView.getContext(), String.valueOf(i),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return mView;
    }
}
