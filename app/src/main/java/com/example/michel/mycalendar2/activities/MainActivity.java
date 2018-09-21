package com.example.michel.mycalendar2.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michel.mycalendar2.calendarview.CellConfig;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.listeners.OnDateClickListener;
import com.example.michel.mycalendar2.calendarview.listeners.OnExpDateClickListener;
import com.example.michel.mycalendar2.calendarview.listeners.OnMonthScrollListener;
import com.example.michel.mycalendar2.calendarview.views.ExpCalendarView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private TextView YearMonthTvW;
    private TextView YearMonthTvM;
    private ExpCalendarView weekCalendarView;
    private ExpCalendarView monthCalendarView;
    private DateData selectedDate;

    private SlidingUpPanelLayout slidingUpPanelLayout;

    private LinearLayout weekCalendarLayout;
    private LinearLayout monthCalendarLayout;

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

        weekCalendarLayout = (LinearLayout) findViewById(R.id.week_layout);
        monthCalendarLayout = (LinearLayout) findViewById(R.id.month_layout);

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                switch (newState){
                    case EXPANDED:
                        weekCalendarLayout.setVisibility(View.GONE);
                        monthCalendarLayout.setVisibility(View.VISIBLE);
                        break;
                    case COLLAPSED:
                        weekCalendarLayout.setVisibility(View.VISIBLE);
                        monthCalendarLayout.setVisibility(View.GONE);
                        break;
                }
            }
        });

        //      Get instance.
        weekCalendarView = ((ExpCalendarView) findViewById(R.id.calendar_week));
        YearMonthTvW = (TextView) findViewById(R.id.w_YYMM_Tv);
        YearMonthTvW.setText(getDateString(Calendar.getInstance().get(Calendar.YEAR),(Calendar.getInstance().get(Calendar.MONTH) + 1)));

        monthCalendarView = ((ExpCalendarView) findViewById(R.id.calendar_month));
        YearMonthTvM = (TextView) findViewById(R.id.m_YYMM_Tv);
        YearMonthTvM.setText(getDateString(Calendar.getInstance().get(Calendar.YEAR),(Calendar.getInstance().get(Calendar.MONTH) + 1)));
//      Set up listeners.

        monthCalendarView.setIfExpand(true);

        weekCalendarView.setOnDateClickListener(new OnExpDateClickListener()).setOnMonthScrollListener(new OnMonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month) {
                YearMonthTvW.setText(getDateString(year, month));
            }

            @Override
            public void onMonthScroll(float positionOffset) {
//                Log.i("listener", "onMonthScroll:" + positionOffset);
            }
        });

        monthCalendarView.setOnDateClickListener(new OnExpDateClickListener()).setOnMonthScrollListener(new OnMonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month) {
                YearMonthTvM.setText(getDateString(year, month));
            }

            @Override
            public void onMonthScroll(float positionOffset) {
//                Log.i("listener", "onMonthScroll:" + positionOffset);
            }
        });

        monthCalendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                monthCalendarView.getMarkedDates().removeAdd();
                monthCalendarView.markDate(date);
                selectedDate = date;
            }
        });

        weekCalendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                weekCalendarView.getMarkedDates().removeAdd();
                weekCalendarView.markDate(date);
                selectedDate = date;
            }
        });

        //imageInit();

        Calendar calendar = Calendar.getInstance();
        selectedDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        weekCalendarView.markDate(selectedDate);
        monthCalendarView.markDate(selectedDate);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private String getDateString(int year, int month){
        String monthName = "";
        switch (month){
            case 1:
                monthName = "Январь";
                break;
            case 2:
                monthName = "Февраль";
                break;
            case 3:
                monthName = "Март";
                break;
            case 4:
                monthName = "Апрель";
                break;
            case 5:
                monthName = "Май";
                break;
            case 6:
                monthName = "Июнь";
                break;
            case 7:
                monthName = "Июль";
                break;
            case 8:
                monthName = "Август";
                break;
            case 9:
                monthName = "Сентябрь";
                break;
            case 10:
                monthName = "Октябрь";
                break;
            case 11:
                monthName = "Ноябрь";
                break;
            case 12:
                monthName = "Декабрь";
                break;
        }
        return monthName + " " + String.valueOf(year);
    }

    private boolean ifExpand = false;
/*
    private void imageInit() {
        final ImageView expandIV = (ImageView) findViewById(R.id.w_expandIV);
        expandIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ifExpand) {
                    CellConfig.Month2WeekPos = CellConfig.middlePosition;
                    weekCalendarView.setIfExpand(false);
                    expandIV.setImageResource(R.mipmap.icon_arrow_down);
                    CellConfig.weekAnchorPointDate = selectedDate;
                    weekCalendarView.expand();
                } else {
                    CellConfig.Week2MonthPos = CellConfig.middlePosition;
                    weekCalendarView.setIfExpand(true);
                    expandIV.setImageResource(R.mipmap.icon_arrow_up);
                    weekCalendarView.expand();
                }
                ifExpand = !ifExpand;
            }
        });
    }
*/
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
