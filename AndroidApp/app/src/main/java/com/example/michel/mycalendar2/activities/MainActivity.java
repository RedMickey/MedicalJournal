package com.example.michel.mycalendar2.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.example.michel.mycalendar2.app_async_tasks.MeasurementNotificationsCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.PillNotificationsCreationTask;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.utils.DatabaseHelper;
import com.example.michel.mycalendar2.main_fragments.HistoryFragment;
import com.example.michel.mycalendar2.main_fragments.MainFragment;
import com.example.michel.mycalendar2.main_fragments.ReminderFragment;
import com.example.michel.mycalendar2.main_fragments.StatisticListFragment;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private AppBarLayout appBarLayout;
    private LinearLayout toolbarLinearLayout1;
    private LinearLayout toolbarLinearLayout2;
    private int preFragmentId = -1;
    private NavigationView navigationView;
    private MainActivity mainActivity;

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

        toolbarLinearLayout1 = (LinearLayout)findViewById(R.id.toolbar_linear_layout1);
        toolbarLinearLayout2 = (LinearLayout)findViewById(R.id.toolbar_linear_layout2);
        appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false);

        if (appBarLayout.getLayoutParams() != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
            appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return false;
                }
            });
            layoutParams.setBehavior(appBarLayoutBehaviour);
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mainActivity = this;

        ((LinearLayout) navigationView.getHeaderView(0).findViewById(R.id.nav_user_layout)).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mainActivity, LoginActivity.class);
                        mainActivity.startActivity(intent);
                    }
                }
        );

        if ( savedInstanceState == null )   // приложение запущено впервые
        {
            databaseHelper = new DatabaseHelper(getApplicationContext());
            // создаем базу данных
            databaseHelper.create_db();
            DatabaseAdapter.AppContext = getApplicationContext();

            DatabaseAdapter databaseAdapter = new DatabaseAdapter();
            databaseAdapter.open();

            DBStaticEntries.cycleTypes = databaseAdapter.getCycleTypes();
            DBStaticEntries.dateTypes = databaseAdapter.getDateTypes();
            DBStaticEntries.doseTypes = databaseAdapter.getDoseTypes();
            DBStaticEntries.measurementTypes = databaseAdapter.getMeasurementTypes();

            databaseAdapter.insertTestTable();

            databaseAdapter.close();

            SharedPreferences settings = getSharedPreferences("MedicalJournalSettings", MODE_PRIVATE);
            String curDate = settings.getString("cur_date", "0000-00-00");
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            if (!curDate.equals(sdf.format(cal.getTime())))
            {
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("cur_date");
                editor.putString("cur_date",sdf.format(cal.getTime()));
                editor.apply();

                PillNotificationsCreationTask pnct = new PillNotificationsCreationTask();
                pnct.execute(getApplicationContext());

                MeasurementNotificationsCreationTask mnct = new MeasurementNotificationsCreationTask();
                mnct.execute(getApplicationContext());
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment mainFragment = (MainFragment)getSupportFragmentManager().findFragmentByTag("MAIN_FRAGMENT");
            if (mainFragment == null || !mainFragment.isVisible()) {
                preFragmentId = -1;
                checkToolBarLinearLayoutVisibility(0);
                MainFragment mainFragmentNew = MainFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.frame_container, mainFragmentNew, "MAIN_FRAGMENT")
                        .commit();
                //Toast.makeText(this,"here",Toast.LENGTH_SHORT).show();
                navigationView.getMenu().getItem(0).setChecked(true);
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
        int isMainFrame = 0;

        if (id!=preFragmentId) {
            Fragment newFragment;
            switch (id) {
                case R.id.nav_medicines:
                    newFragment = ReminderFragment.newInstance();
                    checkToolBarLinearLayoutVisibility(0);
                    break;
                case R.id.nav_history:
                    newFragment = HistoryFragment.newInstance();
                    activeAppBarLayoutDragCallback();
                    checkToolBarLinearLayoutVisibility(1);
                    toolbarLinearLayout1.setVisibility(View.VISIBLE);
                    break;
                case R.id.nav_statistic:
                    newFragment = StatisticListFragment.newInstance();
                    activeAppBarLayoutDragCallback();
                    checkToolBarLinearLayoutVisibility(1);
                    toolbarLinearLayout2.setVisibility(View.VISIBLE);
                    break;
                case R.id.nav_main:
                    newFragment = null;
                    isMainFrame = 1;
                    Fragment mainFragment = (MainFragment)getSupportFragmentManager().findFragmentByTag("MAIN_FRAGMENT");
                    if (mainFragment == null || !mainFragment.isVisible()) {
                        preFragmentId = -1;
                        checkToolBarLinearLayoutVisibility(0);
                        MainFragment mainFragmentNew = MainFragment.newInstance();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.frame_container, mainFragmentNew, "MAIN_FRAGMENT")
                                .commit();
                    }
                    break;
                default:
                    //intent = new Intent(this, AddTreatmentActivity.class);
                    newFragment = ReminderFragment.newInstance();
            }

            // Вставить фрагмент, заменяя любой существующий
            //FragmentManager fragmentManager = getSupportFragmentManager();
            //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
            if (isMainFrame!=1){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, newFragment);
                //transaction.addToBackStack(null);
                transaction.commit();
            }

            // Выделение существующего элемента выполнено с помощью
            // NavigationView
            item.setChecked(true);
        }

        preFragmentId = id;
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void activeAppBarLayoutDragCallback(){
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
        appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
            @Override
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return true;
            }
        });
        layoutParams.setBehavior(appBarLayoutBehaviour);
    }

    private void checkToolBarLinearLayoutVisibility(int param){
        switch (param){
            case 0:
                if (toolbarLinearLayout1.getVisibility()!=View.GONE||toolbarLinearLayout2.getVisibility()!=View.GONE){
                    appBarLayout.setExpanded(false);
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                    AppBarLayout.Behavior appBarLayoutBehaviour = new AppBarLayout.Behavior();
                    appBarLayoutBehaviour.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                        @Override
                        public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                            return false;
                        }
                    });
                    layoutParams.setBehavior(appBarLayoutBehaviour);
                    toolbarLinearLayout1.setVisibility(View.GONE);
                    toolbarLinearLayout2.setVisibility(View.GONE);
                }
                break;
            case 1:
                if (toolbarLinearLayout1.getVisibility()!=View.GONE){
                    toolbarLinearLayout1.setVisibility(View.GONE);
                }
                if (toolbarLinearLayout2.getVisibility()!=View.GONE){
                    toolbarLinearLayout2.setVisibility(View.GONE);
                }
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //Toast.makeText(this,"newIntent", Toast.LENGTH_SHORT).show();
        Fragment mainFragment = (MainFragment)getSupportFragmentManager().findFragmentByTag("MAIN_FRAGMENT");
        if (mainFragment == null || !mainFragment.isVisible()) {
            MainFragment mainFragmentNew = MainFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, mainFragmentNew, "MAIN_FRAGMENT")
                    .commit();
            //Toast.makeText(this,"here",Toast.LENGTH_SHORT).show();
        }
    }
}
