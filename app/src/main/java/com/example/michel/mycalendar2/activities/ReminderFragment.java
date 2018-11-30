package com.example.michel.mycalendar2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.michel.mycalendar2.adapters.MeasurementTypesListAdapter;
import com.example.michel.mycalendar2.adapters.TreatmentListAdapter;
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
    private MeasurementType selectedMT = null;
    private SlidingUpPanelLayout slidingUpPanelLayout;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = getView();
        view=inflater.inflate(R.layout.reminder_fragment, container, false);
        slidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout_add_reminder);
        slidingUpPanelLayout.setAnchorPoint(0.7f);
        //slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                if (newState == SlidingUpPanelLayout.PanelState.COLLAPSED&&selectedMT!=null){
                    Intent intent = new Intent(view.getContext(), AddMeasurementActivity.class);
                    intent.putExtra("MeasurementTypeID", selectedMT.getIndex());
                    intent.putExtra("MeasurementName", selectedMT.getName());
                    selectedMT = null;
                    view.getContext().startActivity(intent);
                }
            }
        });

        //slidingLinearLayout = (LinearLayout) view.findViewById(R.id.sliding_linear_layout);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_add_reminder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (viewPager.getCurrentItem()){
                    case 0:
                        Intent intent1 = new Intent(getActivity(), AddTreatmentActivity.class);
                        startActivity(intent1);
                        break;
                    case 1:
                        //Intent intent2 = new Intent(getActivity(), AddMeasurementActivity.class);
                        //startActivity(intent2);
                        slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
                        break;
                }
            }
        });

        viewPager = (ViewPager) view.findViewById(R.id.medicines_measurements_viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.medicines_measurements_tabs);
        TreatmentListAdapter adapter = new TreatmentListAdapter(getChildFragmentManager());
        adapter.addFragment(ReminderListFragment.newInstance(0));
        adapter.addFragment(ReminderListFragment.newInstance(1));
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        ListView listView = (ListView) view.findViewById(R.id.measurement_types_lv);
        /*ArrayList<MeasurementType> mts = new ArrayList<>();
        mts.add(new MeasurementType(1, "Температура"));
        mts.add(new MeasurementType(2, "Давление"));*/
        MeasurementTypesListAdapter mtla = new MeasurementTypesListAdapter(view.getContext(), R.layout.measurement_type_list_item, DBStaticEntries.measurementTypes);
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //slidingLinearLayout.setVisibility(View.GONE);
                selectedMT = (MeasurementType) adapterView.getItemAtPosition(i);
                //slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                /*MeasurementType mt = (MeasurementType) adapterView.getItemAtPosition(i);
                Intent intent = new Intent(view.getContext(), AddMeasurementActivity.class);
                intent.putExtra("MeasurementTypeID", mt.getIndex());
                intent.putExtra("MeasurementName", mt.getName());
                view.getContext().startActivity(intent);*/
            }
        };
        listView.setAdapter(mtla);
        listView.setOnItemClickListener(itemClickListener);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!(viewPager.getAdapter() == null)) {
            viewPager.getAdapter().notifyDataSetChanged();
        }
        //slidingLinearLayout.setVisibility(View.VISIBLE);
        //slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
