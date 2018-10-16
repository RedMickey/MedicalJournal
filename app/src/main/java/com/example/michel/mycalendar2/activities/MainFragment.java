package com.example.michel.mycalendar2.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.calendarview.CellConfig;
import com.example.michel.mycalendar2.calendarview.adapters.CalendarViewExpAdapter;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.adapters.DayAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.data.DayDifference;
import com.example.michel.mycalendar2.calendarview.listeners.OnDateClickListener;
import com.example.michel.mycalendar2.calendarview.listeners.OnExpDateClickListener;
import com.example.michel.mycalendar2.calendarview.listeners.OnMonthScrollListener;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.calendarview.utils.DatabaseHelper;
import com.example.michel.mycalendar2.calendarview.views.ExpCalendarView;
import com.example.michel.mycalendar2.calendarview.views.MonthExpFragment;
import com.example.michel.mycalendar2.calendarview.views.WeekDayViewPager;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Calendar;

public class MainFragment extends Fragment{

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private View view;
    private TextView YearMonthTv;
    private ExpCalendarView calendarView;
    private DateData selectedDate;

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private LinearLayout calendarLayout;

    private WeekDayViewPager weekDayViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.main_calendar_view, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Get slidingUpPanelLayout
        slidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);

        calendarLayout = (LinearLayout) view.findViewById(R.id.calendar_layout);

        weekDayViewPager = (WeekDayViewPager) view.findViewById((R.id.day_view));

        //      Get instance.
        calendarView = ((ExpCalendarView) view.findViewById(R.id.calendar_view));


      /*  new Handler().post(new Runnable() {
            @Override
            public void run() {
                calendarView.setCurrentItem(500);
            }
        });
*/
        //calendarView.setCurrentItem(500);
        YearMonthTv = (TextView) view.findViewById(R.id.YYMM_Tv);
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
                //DateData dateDelta = new DateData(preData.getYear()-date.getYear(),preData.getMonth()-date.getMonth(),preData.getDay()-date.getDay());
                DateData dateBuff = new DateData(preData.getYear(),preData.getMonth(),preData.getDay());
                DayDifference dayDifference = CalendarUtil.calculateDaysBetweenDates(preData, date);

                ((DayAdapter)weekDayViewPager.getAdapter()).setDayClickedDate(date);

                for(int i = 0; i<dayDifference.getDays();i++)
                {
                    ((DayAdapter)weekDayViewPager.getAdapter()).setDayClicked(true);
                    int newYear, newMonth, newDay;
                    if (dayDifference.isSwipeRight()){
                        dateBuff.setDay(dateBuff.getDay()+1);
                        if(dateBuff.getDay()>=CalendarUtil.getDaysInMonth(dateBuff.getMonth()-1,dateBuff.getYear()))
                        {
                            newDay = 1;
                            if (dateBuff.getMonth()==12)
                            {
                                newMonth = 1;
                                newYear = dateBuff.getYear()+1;
                            }
                            else {
                                newMonth = dateBuff.getMonth() + 1;
                                newYear = dateBuff.getYear();
                            }
                            if (dateBuff.getDay()>CalendarUtil.getDaysInMonth(dateBuff.getMonth()-1,dateBuff.getYear()))
                                ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(newYear, newMonth, newDay+1));
                            else
                                ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(newYear, newMonth, newDay));
                        }
                        else {
                            ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(dateBuff.getYear(),dateBuff.getMonth(),dateBuff.getDay()+1));
                        }

                        weekDayViewPager.setCurrentItem(weekDayViewPager.getCurrentItem()+1);
                    }

                    else {
                        dateBuff.setDay(dateBuff.getDay()-1);
                        if(dateBuff.getDay()<=1)
                        {
                            if (dateBuff.getMonth()==1)
                            {
                                newDay = 31;
                                newMonth = 12;
                                newYear = dateBuff.getYear()-1;
                            }
                            else {
                                newDay = CalendarUtil.getDaysInMonth(dateBuff.getMonth()-2,dateBuff.getYear());
                                newMonth = dateBuff.getMonth() - 1;
                                newYear = dateBuff.getYear();
                            }

                            if (dateBuff.getDay()==1)
                                ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(newYear, newMonth, newDay));
                            else
                                ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(newYear, newMonth, newDay-1));
                        }
                        else {
                            ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(dateBuff.getYear(),dateBuff.getMonth(),dateBuff.getDay()-1));
                        }

                        weekDayViewPager.setCurrentItem(weekDayViewPager.getCurrentItem()-1);

                        //((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(date.getYear(),date.getMonth(),date.getDay()-1));
                        //weekDayViewPager.setCurrentItem(weekDayViewPager.getCurrentItem()-1);
                    }

                    preData = calendarView.getMarkedDates().getAll().get(0);
                    dateBuff = new DateData(preData.getYear(),preData.getMonth(),preData.getDay());
                }

                CellConfig.selectedDate = calendarView.getMarkedDates().getAll().get(0);
                DateData s = CellConfig.selectedDate;
                /*calendarView.getMarkedDates().removeAdd();
                calendarView.markDate(date);
                selectedDate = date;*/
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
                        CellConfig.ifMonth = false;
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

        weekDayViewPager.init(calendarView);

        weekDayViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("Item", String.valueOf(weekDayViewPager.getCurrentItem()));
                Log.i("Pos", String.valueOf(position));
                DateData date = calendarView.getMarkedDates().getAll().get(0);
                boolean y = ((DayAdapter)weekDayViewPager.getAdapter()).isDayClicked();
                int newYear, newMonth, newDay;
                if (position>weekDayViewPager.LastPage)
                {
                    calendarView.getMarkedDates().removeAdd();
                    if(date.getDay()==CalendarUtil.getDaysInMonth(date.getMonth()-1,date.getYear()))
                    {
                        newDay = 1;
                        if (date.getMonth()==12)
                        {
                            newMonth = 1;
                            newYear = date.getYear()+1;
                        }
                        else {
                            newMonth = date.getMonth() + 1;
                            newYear = date.getYear();
                        }
                        calendarView.markDate(new DateData(newYear,newMonth,newDay));
                        ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(newYear, newMonth,newDay+1));
                    }
                    else if(date.getDay()+1==CalendarUtil.getDaysInMonth(date.getMonth()-1,date.getYear()))
                    {
                        calendarView.markDate(new DateData(date.getYear(),date.getMonth(),date.getDay()+1));
                        ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(date.getYear(),date.getMonth(),1));
                    }
                    else {
                        calendarView.markDate(new DateData(date.getYear(),date.getMonth(),date.getDay()+1));
                        ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(date.getYear(),date.getMonth(),date.getDay()+2));
                    }

                    MonthExpFragment fr = (MonthExpFragment)((CalendarViewExpAdapter)calendarView.getAdapter()).getCurrentFragment();

                    if (!fr.getMonthViewExpd().getAdapter().listContainItemByDate(calendarView.getMarkedDates().getAll().get(0))&&
                            !((DayAdapter)weekDayViewPager.getAdapter()).isDayClicked()){
                        Log.i("IsContein", "does not contain");
                        calendarView.setCurrentItem(calendarView.getCurrentItem()+1);
                    }
                }
                if (position<weekDayViewPager.LastPage)
                {
                    calendarView.getMarkedDates().removeAdd();
                    if(date.getDay()==1)
                    {
                        if (date.getMonth()==1)
                        {
                            newDay = 31;
                            newMonth = 12;
                            newYear = date.getYear()-1;
                        }
                        else {
                            newDay = CalendarUtil.getDaysInMonth(date.getMonth()-2,date.getYear());
                            newMonth = date.getMonth() - 1;
                            newYear = date.getYear();
                        }
                        calendarView.markDate(new DateData(newYear,newMonth,newDay));
                        ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(newYear, newMonth,newDay-1));
                    }
                    else if(date.getDay()-1==1)
                    {   if (date.getMonth()==1)
                        newDay = 31;
                    else
                        newDay = CalendarUtil.getDaysInMonth(date.getMonth()-2,date.getYear());
                        calendarView.markDate(new DateData(date.getYear(),date.getMonth(),date.getDay()-1));
                        ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(date.getYear(),date.getMonth(),newDay));
                    }
                    else {
                        calendarView.markDate(new DateData(date.getYear(),date.getMonth(),date.getDay()-1));
                        ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(new DateData(date.getYear(),date.getMonth(),date.getDay()-2));
                        Log.i("IsContein", "does not contain");
                    }

                    MonthExpFragment fr = (MonthExpFragment)((CalendarViewExpAdapter)calendarView.getAdapter()).getCurrentFragment();

                    if (!fr.getMonthViewExpd().getAdapter().listContainItemByDate(calendarView.getMarkedDates().getAll().get(0))&&
                            !((DayAdapter)weekDayViewPager.getAdapter()).isDayClicked()) {
                        Log.i("IsContein", "does not contain");
                        calendarView.setCurrentItem(calendarView.getCurrentItem()-1);
                    }
                }

                ((DayAdapter)weekDayViewPager.getAdapter()).setDayClicked(false);
                weekDayViewPager.LastPage = position;

                Log.i("DateP", date.getMonthString()+ " " + date.getDayString());
                DateData date2 = calendarView.getMarkedDates().getAll().get(0);
                Log.i("DateN", date2.getMonthString()+ " " + date2.getDayString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //calendarView.setCurrentItem(500);
        return view;
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



        //slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        //calendarView.expand();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CellConfig.resetAllDatas();
    }
}
