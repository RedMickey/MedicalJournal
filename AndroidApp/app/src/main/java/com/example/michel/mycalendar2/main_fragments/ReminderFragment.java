package com.example.michel.mycalendar2.main_fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.michel.mycalendar2.activities.AddMeasurementActivity;
import com.example.michel.mycalendar2.activities.AddTreatmentActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.adapters.MeasurementTypesListAdapter;
import com.example.michel.mycalendar2.adapters.TabListAdapter;
import com.example.michel.mycalendar2.auxiliary_fragments.ReminderListFragment;
import com.example.michel.mycalendar2.utils.DBStaticEntries;
import com.example.michel.mycalendar2.utils.utilModels.MeasurementType;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

public class ReminderFragment extends Fragment {
    public static ReminderFragment newInstance() {
        return new ReminderFragment();
    }
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = getView();
        mView=inflater.inflate(R.layout.reminder_fragment, container, false);

        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.fab_add_reminder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (viewPager.getCurrentItem()){
                    case 0:
                        Intent intent1 = new Intent(getActivity(), AddTreatmentActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        LayoutInflater inflater = LayoutInflater.from(mView.getContext());
                        final BottomSheetDialog choosingMeasurementTypeDialog = new BottomSheetDialog(mView.getContext());
                        View bottomSheetView = inflater.inflate(R.layout.choosing_measurement_type_dialog_layout, null, false);
                        ListView listView = (ListView) bottomSheetView.findViewById(R.id.measurement_types_lv);
                        MeasurementTypesListAdapter mtla = new MeasurementTypesListAdapter(mView.getContext(), R.layout.measurement_type_list_item, DBStaticEntries.measurementTypes);
                        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                MeasurementType selectedMT = (MeasurementType) adapterView.getItemAtPosition(i);
                                Intent intent = new Intent(mView.getContext(), AddMeasurementActivity.class);
                                intent.putExtra("MeasurementTypeID", selectedMT.getIndex());
                                intent.putExtra("MeasurementName", selectedMT.getName());
                                mView.getContext().startActivity(intent);
                                choosingMeasurementTypeDialog.dismiss();
                            }
                        };
                        listView.setAdapter(mtla);
                        listView.setOnItemClickListener(itemClickListener);
                        choosingMeasurementTypeDialog.setContentView(bottomSheetView);
                        choosingMeasurementTypeDialog.show();
                        break;
                }
            }
        });

        viewPager = (ViewPager) mView.findViewById(R.id.medicines_measurements_viewpager);
        tabLayout = (TabLayout) mView.findViewById(R.id.medicines_measurements_tabs);
        TabListAdapter adapter = new TabListAdapter(getChildFragmentManager(),
                new String[] { "Медикаменты", "Измерения" });
        adapter.addFragment(ReminderListFragment.newInstance(0));
        adapter.addFragment(ReminderListFragment.newInstance(1));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!(viewPager.getAdapter() == null)) {
            viewPager.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
