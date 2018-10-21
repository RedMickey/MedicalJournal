package com.example.michel.mycalendar2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import android.widget.Toast;

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
import com.example.michel.mycalendar2.models.PillReminderEntry;
import com.example.michel.mycalendar2.utils.DBStaticEntries;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        MainFragment mainFragment = MainFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame_container, mainFragment, "MAIN_FRAGMENT")
                .commit();

        //DatabaseAdapter d = new DatabaseAdapter();
        /*d.open();
        d.getAllTables();
        d.close();*/
        //TasksViewCreationTask t = new TasksViewCreationTask();
        //t.execute(selectedDate);
        databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        databaseHelper.create_db();
        DatabaseAdapter.AppContext = getApplicationContext();

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();

        DBStaticEntries.cycleTypes = databaseAdapter.getCycleTypes();
        DBStaticEntries.dateTypes = databaseAdapter.getDateTypes();
        DBStaticEntries.doseTypes = databaseAdapter.getDoseTypes();

        /*databaseAdapter.insertPillReminder("pill1", 0,0, null, 0, 0,
                null, null, 0);*/
        databaseAdapter.close();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

/*
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
*/
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment mainFragment = (MainFragment)getSupportFragmentManager().findFragmentByTag("MAIN_FRAGMENT");
            if (mainFragment == null || !mainFragment.isVisible()) {
                MainFragment mainFragmentNew = MainFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.frame_container, mainFragmentNew, "MAIN_FRAGMENT")
                        .commit();
                //Toast.makeText(this,"here",Toast.LENGTH_SHORT).show();
            }
            else
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
        Class fragmentClass;
        int id = item.getItemId();
        Fragment newFragment;
        //Intent intent;
        /*
        if (id == R.id.nav_medicines) {
            fragmentClass = AddTreatmentFragment.class;
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        switch(id) {
            case R.id.nav_medicines:
                //intent = new Intent(this, AddTreatmentActivity.class);
                newFragment=ReminderListFragment.newInstance();
                break;
            default:
                //intent = new Intent(this, AddTreatmentActivity.class);
                newFragment=ReminderListFragment.newInstance();
        }



        // Вставить фрагмент, заменяя любой существующий
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
        // Выделение существующего элемента выполнено с помощью
        // NavigationView
        item.setChecked(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        //startActivity(intent);
        return true;
    }
}
