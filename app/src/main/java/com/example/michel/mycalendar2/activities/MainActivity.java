package com.example.michel.mycalendar2.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.calendarview.CellConfig;
import com.example.michel.mycalendar2.calendarview.adapters.CalendarViewExpAdapter;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.adapters.DayAdapter;
import com.example.michel.mycalendar2.calendarview.appAsyncTasks.TasksViewCreationTask;
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
import com.example.michel.mycalendar2.models.TakingMedicine;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView YearMonthTv;
    private ExpCalendarView calendarView;
    private DateData selectedDate;
    private DatabaseHelper databaseHelper;

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private LinearLayout calendarLayout;

    private WeekDayViewPager weekDayViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Get slidingUpPanelLayout
        slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        calendarLayout = (LinearLayout) findViewById(R.id.calendar_layout);

        weekDayViewPager = (WeekDayViewPager) findViewById((R.id.day_view));

        //      Get instance.
        calendarView = ((ExpCalendarView) findViewById(R.id.calendar_view));
        YearMonthTv = (TextView) findViewById(R.id.YYMM_Tv);
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

                selectedDate = calendarView.getMarkedDates().getAll().get(0);
                DateData s = selectedDate;
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
                        CellConfig.weekAnchorPointDate = selectedDate;
                        YearMonthTv.setText(CalendarUtil.getDateString(selectedDate.getYear(), selectedDate.getMonth()));
                        calendarView.expand();
                        break;
                }
            }
        });
        //imageInit();

        final Calendar calendar = Calendar.getInstance();
        selectedDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        calendarView.markDate(selectedDate);

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

        databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        databaseHelper.create_db();
        DatabaseAdapter.AppContext = getApplicationContext();
        DatabaseAdapter d = new DatabaseAdapter();
        d.open();
        d.getAllTables();
        d.close();
        //TasksViewCreationTask t = new TasksViewCreationTask();
        //t.execute(selectedDate);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private boolean ifExpand = false;

    private void imageInit() {
        final ImageView expandIV = (ImageView) findViewById(R.id.main_expandIV);
        expandIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifExpand) {
                    CellConfig.Month2WeekPos = CellConfig.middlePosition;
                    CellConfig.ifMonth = false;
                    expandIV.setImageResource(R.mipmap.icon_arrow_down);
                    CellConfig.weekAnchorPointDate = selectedDate;
                    calendarView.expand();
                } else {
                    CellConfig.Week2MonthPos = CellConfig.middlePosition;
                    CellConfig.ifMonth = true;
                    expandIV.setImageResource(R.mipmap.icon_arrow_up);
                    calendarView.expand();
                }
                ifExpand = !ifExpand;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
