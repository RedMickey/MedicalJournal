package com.example.michel.mycalendar2.activities;

import android.app.Notification;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import com.example.michel.mycalendar2.app_async_tasks.NotificationsCreationTask;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.utils.DatabaseHelper;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.SimpleFormatter;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

                NotificationsCreationTask nct = new NotificationsCreationTask();
                nct.execute(getApplicationContext());
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

        switch(id) {
            case R.id.nav_medicines:
                //intent = new Intent(this, AddTreatmentActivity.class);
                newFragment= ReminderFragment.newInstance();
                break;
            default:
                //intent = new Intent(this, AddTreatmentActivity.class);
                newFragment= ReminderFragment.newInstance();
        }



        // Вставить фрагмент, заменяя любой существующий
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, newFragment);
        //transaction.addToBackStack(null);
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
