package com.example.michel.mycalendar2.main_fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.content.res.AppCompatResources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.AddMeasurementActivity;
import com.example.michel.mycalendar2.activities.AddOneTimeMeasurementActivity;
import com.example.michel.mycalendar2.activities.AddOneTimeTreatmentActivity;
import com.example.michel.mycalendar2.activities.AddTreatmentActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.adapters.MeasurementTypesListAdapter;
import com.example.michel.mycalendar2.calendarview.CellConfig;
import com.example.michel.mycalendar2.calendarview.adapters.CalendarViewExpAdapter;
import com.example.michel.mycalendar2.calendarview.adapters.DayAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.data.DayDifference;
import com.example.michel.mycalendar2.calendarview.data.MarkedDates;
import com.example.michel.mycalendar2.calendarview.listeners.OnDateClickListener;
import com.example.michel.mycalendar2.calendarview.listeners.OnExpDateClickListener;
import com.example.michel.mycalendar2.calendarview.listeners.OnMonthScrollListener;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.calendarview.views.ExpCalendarView;
import com.example.michel.mycalendar2.calendarview.views.MonthExpFragment;
import com.example.michel.mycalendar2.calendarview.views.WeekDayViewPager;
import com.example.michel.mycalendar2.services.AlarmService;
import com.example.michel.mycalendar2.utils.DBStaticEntries;
import com.example.michel.mycalendar2.utils.utilModels.MeasurementType;
import com.leinardi.android.speeddial.FabWithLabelView;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Calendar;

public class MainFragment extends Fragment{

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private View mView;
    private TextView YearMonthTv;
    private ExpCalendarView calendarView;
    private DateData selectedDate;
    private Calendar calendar;

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private LinearLayout calendarLayout;

    private WeekDayViewPager weekDayViewPager;

    SpeedDialView mSpeedDialView;
    private static final int ADD_ACTION_POSITION = 4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.main_calendar_view, container, false);

        calendar = Calendar.getInstance();

        // Get slidingUpPanelLayout
        slidingUpPanelLayout = (SlidingUpPanelLayout) mView.findViewById(R.id.sliding_layout);

        calendarLayout = (LinearLayout) mView.findViewById(R.id.calendar_layout);

        weekDayViewPager = (WeekDayViewPager) mView.findViewById((R.id.day_view));

        mSpeedDialView = mView.findViewById(R.id.speedDial);
        initSpeedDial();

        //      Get instance.
        calendarView = ((ExpCalendarView) mView.findViewById(R.id.calendar_view));


      /*  new Handler().post(new Runnable() {
            @Override
            public void run() {
                calendarView.setCurrentItem(500);
            }
        });
*/
        //calendarView.setCurrentItem(500);
        YearMonthTv = (TextView) mView.findViewById(R.id.YYMM_Tv);
        YearMonthTv.setText(CalendarUtil.getDateString(Calendar.getInstance().get(Calendar.YEAR),(Calendar.getInstance().get(Calendar.MONTH) + 1)));

//      Set up listeners.
        calendarView.setOnDateClickListener(new OnExpDateClickListener()).setOnMonthScrollListener(new OnMonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month) {
                YearMonthTv.setText(CalendarUtil.getDateString(year, month));
            }

            @Override
            public void onMonthScroll(float positionOffset) {
                //Log.i("listener", "onMonthScroll:" + positionOffset);
            }
        });

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                DateData preData = calendarView.getMarkedDates().getAll().get(0);
                //Calendar calendarNext = Calendar.getInstance();
                //DateData dateBuff = new DateData(preData.getYear(),preData.getMonth(),preData.getDay());

                calendar.set(preData.getYear(), preData.getMonth()-1, preData.getDay());

                DayDifference dayDifference = CalendarUtil.calculateDaysBetweenDates(preData, date);

                if (dayDifference.isSwipeRight())
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                else
                    calendar.add(Calendar.DAY_OF_MONTH, -1);

                ((DayAdapter)weekDayViewPager.getAdapter()).setDayClickedDate(date);

                calendarView.getMarkedDates().removeAdd();
                for(int i = 0; i<dayDifference.getDays();i++)
                {
                    ((DayAdapter)weekDayViewPager.getAdapter()).setDayClicked(true);
                    if (dayDifference.isSwipeRight()){
                        calendar.add(Calendar.DAY_OF_MONTH, 1);

                        ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(
                                new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)));

                        weekDayViewPager.setCurrentItem(weekDayViewPager.getCurrentItem()+1);
                    }

                    else {
                        calendar.add(Calendar.DAY_OF_MONTH, -1);

                        ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(
                                new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)));

                        weekDayViewPager.setCurrentItem(weekDayViewPager.getCurrentItem()-1);
                    }

                    //preData = calendarView.getMarkedDates().getAll().get(0);
                    //calendar.set(preData.getYear(), preData.getMonth()-1, preData.getDay());
                }
                if (dayDifference.isSwipeRight())
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                else
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendarView.markDate(new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)));
                CellConfig.selectedDate = calendarView.getMarkedDates().getAll().get(0);
                DateData s = CellConfig.selectedDate;


            }
        });

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                switch (newState){
                    case EXPANDED:
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        calendarLayout.setLayoutParams(params);

                        CellConfig.Week2MonthPos = CellConfig.middlePosition;
                        CellConfig.ifMonth = true;
                        calendarView.expand();
                        break;
                    case COLLAPSED:
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        calendarLayout.setLayoutParams(params);

                        CellConfig.Month2WeekPos = CellConfig.middlePosition;
                        int pos = CellConfig.middlePosition;
                        CellConfig.ifMonth = false;
                        DateData f = CellConfig.selectedDate;
                        CellConfig.weekAnchorPointDate = CellConfig.selectedDate;
                        YearMonthTv.setText(CalendarUtil.getDateString(CellConfig.selectedDate.getYear(), CellConfig.selectedDate.getMonth()));
                        calendarView.expand();
                        break;
                }
            }
        });
        //imageInit();

        final Calendar calendar = Calendar.getInstance();
        if (CellConfig.selectedDate==null)
            CellConfig.selectedDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        calendarView.markDate(CellConfig.selectedDate);

        weekDayViewPager.setDayAdapter(new DayAdapter(getChildFragmentManager()));
        weekDayViewPager.init(calendarView);

        weekDayViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("Item", String.valueOf(weekDayViewPager.getCurrentItem()));
                Log.i("Pos", String.valueOf(position));

                if (!((DayAdapter)weekDayViewPager.getAdapter()).isDayClicked()) {
                    DateData date = calendarView.getMarkedDates().getAll().get(0);
                    calendar.set(date.getYear(), date.getMonth()-1, date.getDay());
                    //boolean y = ((DayAdapter)weekDayViewPager.getAdapter()).isDayClicked();

                    if (position > weekDayViewPager.LastPage) {
                        calendarView.getMarkedDates().removeAdd();
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        calendarView.markDate(new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        ((DayAdapter) weekDayViewPager.getAdapter()).setCurrentDate(
                                new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));

                        MonthExpFragment fr = (MonthExpFragment) ((CalendarViewExpAdapter) calendarView.getAdapter()).getCurrentFragment();

                        if (!fr.getMonthViewExpd().getAdapter().listContainItemByDate(calendarView.getMarkedDates().getAll().get(0))) {
                            Log.i("IsContein", "does not contain");
                            calendarView.setCurrentItem(calendarView.getCurrentItem() + 1);
                        }
                    }
                    if (position < weekDayViewPager.LastPage) {
                        calendarView.getMarkedDates().removeAdd();
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        calendarView.markDate(new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        ((DayAdapter) weekDayViewPager.getAdapter()).setCurrentDate(
                                new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));

                        MonthExpFragment fr = (MonthExpFragment) ((CalendarViewExpAdapter) calendarView.getAdapter()).getCurrentFragment();

                        if (!fr.getMonthViewExpd().getAdapter().listContainItemByDate(calendarView.getMarkedDates().getAll().get(0))) {
                            Log.i("IsContein", "does not contain");
                            calendarView.setCurrentItem(calendarView.getCurrentItem() - 1);
                        }
                    }
                }
                ((DayAdapter)weekDayViewPager.getAdapter()).setDayClicked(false);
                weekDayViewPager.LastPage = position;

                //Log.i("DateP", date.getMonthString()+ " " + date.getDayString());
//                DateData date2 = calendarView.getMarkedDates().getAll().get(0);
                //Log.i("DateN", date2.getMonthString()+ " " + date2.getDayString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //calendarView.setCurrentItem(500);
        return mView;
    }

    private void initSpeedDial() {
        FabWithLabelView fabWithLabelView;
        Drawable drawable;

        drawable = AppCompatResources.getDrawable(mView.getContext(), R.drawable.ic_schedule);
        fabWithLabelView = mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id
                .fab_pill_schedule, drawable)
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000, getActivity().getTheme()))
                .setLabel("График приёмов")
                .create());
        /*if (fabWithLabelView != null) {
            fabWithLabelView.setSpeedDialActionItem(fabWithLabelView.getSpeedDialActionItemBuilder()
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000,
                            getActivity().getTheme()))
                    .create());
        }*/

        drawable = AppCompatResources.getDrawable(mView.getContext(), R.drawable.ic_pill_100_light);
        fabWithLabelView = mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id
                .fab_pill_taking, drawable)
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000, getActivity().getTheme()))
                .setLabel("Одиночный приём")
                .create());
        if (fabWithLabelView != null) {
            fabWithLabelView.setSpeedDialActionItem(fabWithLabelView.getSpeedDialActionItemBuilder()
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_indigo_300,
                            getActivity().getTheme()))
                    .create());
        }

        drawable = AppCompatResources.getDrawable(mView.getContext(), R.drawable.ic_schedule);
        fabWithLabelView = mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id
                .fab_measurement_schedule, drawable)
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000, getActivity().getTheme()))
                .setLabel("График измерений")
                .create());
        if (fabWithLabelView != null) {
            fabWithLabelView.setSpeedDialActionItem(fabWithLabelView.getSpeedDialActionItemBuilder()
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_test1,
                            getActivity().getTheme()))
                    .create());
        }

        drawable = AppCompatResources.getDrawable(mView.getContext(), R.drawable.ic_ruler1);
        fabWithLabelView = mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id
                .fab_measurement_taking, drawable)
                .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000, getActivity().getTheme()))
                .setLabel("Измерение")
                .create());
        if (fabWithLabelView != null) {
            fabWithLabelView.setSpeedDialActionItem(fabWithLabelView.getSpeedDialActionItemBuilder()
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_test2,
                            getActivity().getTheme()))
                    .create());
        }

        //Set main action clicklistener.
        mSpeedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                //showToast("Main action clicked!");
                Log.d("DS", "onMainActionSelected");
                return false; // True to keep the Speed Dial open
            }

            @Override
            public void onToggleChanged(boolean isOpen) {
                Log.d("DS", "Speed dial toggle state changed. Open = " + isOpen);
                //mSpeedDialView.close();
                return;
            }
        });

        //mSpeedDialView.liste

        //Set option fabs clicklisteners.
        mSpeedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                switch (actionItem.getId()) {
                    case R.id.fab_pill_schedule:
                        Intent intent1 = new Intent(getActivity(), AddTreatmentActivity.class);
                        startActivity(intent1);
                        mSpeedDialView.close();
                        return true;
                    case R.id.fab_pill_taking:
                        Intent intent2 = new Intent(getActivity(), AddOneTimeTreatmentActivity.class);
                        startActivity(intent2);
                        mSpeedDialView.close();
                        return true;
                    case R.id.fab_measurement_schedule:
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
                        mSpeedDialView.close();
                        return true;
                    case R.id.fab_measurement_taking:
                        LayoutInflater inflater3 = LayoutInflater.from(mView.getContext());
                        final BottomSheetDialog choosingMeasurementTypeDialog2 = new BottomSheetDialog(mView.getContext());
                        View bottomSheetView2 = inflater3.inflate(R.layout.choosing_measurement_type_dialog_layout, null, false);
                        ListView listView2 = (ListView) bottomSheetView2.findViewById(R.id.measurement_types_lv);
                        MeasurementTypesListAdapter mtla2 = new MeasurementTypesListAdapter(mView.getContext(), R.layout.measurement_type_list_item, DBStaticEntries.measurementTypes);
                        AdapterView.OnItemClickListener itemClickListener2 = new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                MeasurementType selectedMT = (MeasurementType) adapterView.getItemAtPosition(i);
                                Intent intent = new Intent(mView.getContext(), AddOneTimeMeasurementActivity.class);
                                intent.putExtra("MeasurementTypeID", selectedMT.getIndex());
                                intent.putExtra("MeasurementName", selectedMT.getName());
                                mView.getContext().startActivity(intent);
                                choosingMeasurementTypeDialog2.dismiss();
                            }
                        };
                        listView2.setAdapter(mtla2);
                        listView2.setOnItemClickListener(itemClickListener2);
                        choosingMeasurementTypeDialog2.setContentView(bottomSheetView2);
                        choosingMeasurementTypeDialog2.show();
                        mSpeedDialView.close();
                        return true;
                }
                return true; // To keep the Speed Dial open
            }
        });

    }
/*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }
    }
    */

    @Override
    public void onResume() {
        super.onResume();
        if (!(weekDayViewPager.getAdapter() == null)) {

            weekDayViewPager.getAdapter().notifyDataSetChanged();


        }
       // weekDayViewPager.init(calendarView);

        //slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        //calendarView.expand();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((DayAdapter)weekDayViewPager.getAdapter()).cleanI();
        ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(MarkedDates.getInstance().getAll().get(0));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CellConfig.resetAllDatas();
    }
}
