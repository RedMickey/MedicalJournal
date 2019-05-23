package com.example.michel.mycalendar2.activities;

import android.accounts.AccountManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.michel.mycalendar2.controllers.app_async_tasks.MeasurementNotificationsCreationTask;
import com.example.michel.mycalendar2.controllers.app_async_tasks.PillNotificationsCreationTask;
import com.example.michel.mycalendar2.controllers.app_async_tasks.PostSignInTask;
import com.example.michel.mycalendar2.controllers.app_async_tasks.SetUpCurrentUserTask;
import com.example.michel.mycalendar2.controllers.app_async_tasks.synchronization.GettingDataFromServerTask;
import com.example.michel.mycalendar2.services.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.DatabaseHelper;
import com.example.michel.mycalendar2.view.main_fragments.GoogleFitFragment;
import com.example.michel.mycalendar2.view.main_fragments.HelpFragment;
import com.example.michel.mycalendar2.view.main_fragments.HistoryFragment;
import com.example.michel.mycalendar2.view.main_fragments.MainFragment;
import com.example.michel.mycalendar2.view.main_fragments.ReminderFragment;
import com.example.michel.mycalendar2.view.main_fragments.SettingsFragment;
import com.example.michel.mycalendar2.view.main_fragments.StatisticListFragment;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int RESULT_SIGN_IN_CODE = 5531;
    private static final int RESULT_USER_CONFIG_CODE = 5532;
    private static final int REQUEST_OAUTH_REQUEST_CODE = 5541;

    private AppBarLayout appBarLayout;
    private LinearLayout toolbarLinearLayout1;
    private LinearLayout toolbarLinearLayout2;
    private int preFragmentId = -1;
    private NavigationView navigationView;
    private MainActivity mainActivity;
    private Toolbar toolbar;
    private String curFragmentTag = "MAIN_FRAGMENT";

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

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
                        if (AccountGeneralUtils.curUser.getId() == 1){
                            Intent intent = new Intent(mainActivity, LoginActivity.class);
                            mainActivity.startActivityForResult(intent, RESULT_SIGN_IN_CODE);
                        }
                        else {
                            Intent intent = new Intent(mainActivity, UserActivity.class);
                            mainActivity.startActivityForResult(intent, RESULT_USER_CONFIG_CODE);
                        }
                        //Intent intent = new Intent(mainActivity, UserActivity.class);
                        //mainActivity.startActivity(intent);
                        /*final AccountManager mAccountManager = AccountManager.get(mainActivity);
                        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(AccountGeneralUtils.ACCOUNT_TYPE, AccountGeneralUtils.AUTHTOKEN_TYPE_FULL_ACCESS,
                                null, null, mainActivity, new AccountManagerCallback<Bundle>() {
                            @Override
                            public void run(AccountManagerFuture<Bundle> future) {
                                try {
                                    Bundle bnd = future.getResult();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }, null);*/
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



            //databaseAdapter.insertTestTable();

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

                SetUpCurrentUserTask setUpCurrentUserTask = new SetUpCurrentUserTask(this);
                setUpCurrentUserTask.setNotificationsCreationWorkable(true);
                setUpCurrentUserTask.execute();

                /*PillNotificationsCreationTask pnct = new PillNotificationsCreationTask();
                pnct.execute(getApplicationContext());

                MeasurementNotificationsCreationTask mnct = new MeasurementNotificationsCreationTask();
                mnct.execute(getApplicationContext());*/
            }
            else {
                SetUpCurrentUserTask setUpCurrentUserTask = new SetUpCurrentUserTask(this);
                setUpCurrentUserTask.execute();
            }
        }

        MainFragment mainFragment = MainFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frame_container, mainFragment, curFragmentTag)
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            curFragmentTag = "MAIN_FRAGMENT";
            Fragment mainFragment = (MainFragment)getSupportFragmentManager().findFragmentByTag(curFragmentTag);
            if (mainFragment == null || !mainFragment.isVisible()) {
                preFragmentId = -1;
                checkToolBarLinearLayoutVisibility(0);
                MainFragment mainFragmentNew = MainFragment.newInstance();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.frame_container, mainFragmentNew, curFragmentTag)
                        .commit();
                //Toast.makeText(this,"here",Toast.LENGTH_SHORT).show();
                toolbar.setTitle(getResources().getString(R.string.main_fragment_title));
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
        switch (item.getItemId()) {
            /*case R.id.action_settings:
                return true;*/
            case R.id.synchronize:
                synchronizeDataWithServer();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void synchronizeDataWithServer(){
        if (AccountGeneralUtils.curUser.getId()!=1){
            GettingDataFromServerTask gettingDataFromServerTask = new GettingDataFromServerTask(
                    this
            );
            gettingDataFromServerTask.execute();
        }
        else {
            Toast.makeText(this, "Войдите в учетную запись", Toast.LENGTH_SHORT)
                    .show();
        }

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
                    curFragmentTag = "REMINDER_FRAGMENT";
                    newFragment = ReminderFragment.newInstance();
                    checkToolBarLinearLayoutVisibility(0);
                    toolbar.setTitle(getResources().getString(R.string.reminder_fragment_title));
                    break;
                case R.id.nav_history:
                    curFragmentTag = "HISTORY_FRAGMENT";
                    newFragment = HistoryFragment.newInstance();
                    activeAppBarLayoutDragCallback();
                    checkToolBarLinearLayoutVisibility(1);
                    toolbarLinearLayout1.setVisibility(View.VISIBLE);
                    toolbar.setTitle(getResources().getString(R.string.history_fragment_title));
                    break;
                case R.id.nav_statistic:
                    curFragmentTag = "STATISTIC_FRAGMENT";
                    newFragment = StatisticListFragment.newInstance();
                    activeAppBarLayoutDragCallback();
                    checkToolBarLinearLayoutVisibility(1);
                    toolbarLinearLayout2.setVisibility(View.VISIBLE);
                    toolbar.setTitle(getResources().getString(R.string.statistic_fragment_title));
                    break;
                case R.id.nav_google_fit:
                    curFragmentTag = "GOOGLE_FIT_FRAGMENT";
                    newFragment = GoogleFitFragment.newInstance();
                    checkToolBarLinearLayoutVisibility(0);
                    toolbar.setTitle(getResources().getString(R.string.google_fit_fragment_title));
                    break;
                case R.id.nav_settings:
                    curFragmentTag = "SETTINGS_FRAGMENT";
                    newFragment = SettingsFragment.newInstance();
                    checkToolBarLinearLayoutVisibility(0);
                    toolbar.setTitle(getResources().getString(R.string.settings_fragment_title));
                    break;
                case R.id.nav_help:
                    curFragmentTag = "HELP_FRAGMENT";
                    newFragment = HelpFragment.newInstance();
                    checkToolBarLinearLayoutVisibility(0);
                    toolbar.setTitle(getResources().getString(R.string.help_fragment_title));
                    break;
                case R.id.nav_main:
                    newFragment = null;
                    isMainFrame = 1;
                    curFragmentTag = "MAIN_FRAGMENT";
                    Fragment mainFragment = (MainFragment)getSupportFragmentManager().findFragmentByTag(curFragmentTag);
                    if (mainFragment == null || !mainFragment.isVisible()) {
                        preFragmentId = -1;
                        checkToolBarLinearLayoutVisibility(0);
                        MainFragment mainFragmentNew = MainFragment.newInstance();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.frame_container, mainFragmentNew, curFragmentTag)
                                .commit();
                        toolbar.setTitle(getResources().getString(R.string.main_fragment_title));
                    }
                    break;
                default:
                    //intent = new Intent(this, AddTreatmentActivity.class);
                    curFragmentTag = "REMINDER_FRAGMENT";
                    newFragment = ReminderFragment.newInstance();
            }

            // Вставить фрагмент, заменяя любой существующий
            //FragmentManager fragmentManager = getSupportFragmentManager();
            //fragmentManager.beginTransaction().replace(R.id.flContent, fragment).addToBackStack(null).commit();
            if (isMainFrame!=1){
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_container, newFragment, curFragmentTag);
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
        curFragmentTag = "MAIN_FRAGMENT";
        Fragment mainFragment = (MainFragment)getSupportFragmentManager().findFragmentByTag(curFragmentTag);
        if (mainFragment == null || !mainFragment.isVisible()) {
            MainFragment mainFragmentNew = MainFragment.newInstance();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, mainFragmentNew, curFragmentTag)
                    .commit();
            //Toast.makeText(this,"here",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == RESULT_SIGN_IN_CODE) {
            if (resultCode == RESULT_OK) {
                String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                String authtoken = data.getStringExtra(AccountManager.KEY_AUTHTOKEN);
                MeasurementNotificationsCreationTask mnct = new MeasurementNotificationsCreationTask(
                        1, 1);
                mnct.execute(this);
                PillNotificationsCreationTask pnct = new PillNotificationsCreationTask(
                        1, 1);
                pnct.execute(this);
                PostSignInTask postSignInTask = new PostSignInTask(mainActivity);
                postSignInTask.execute(authtoken, accountName);
            }
            return;
        }
        if (requestCode == RESULT_USER_CONFIG_CODE) {
            if (resultCode == RESULT_OK) {
                boolean isLogOut = data.getBooleanExtra("is_log_out", false);
                if (isLogOut){
                    ((TextView) getNavigationView().findViewById(R.id.username_tv)).setText(getResources().getText(R.string.def_username));
                    ((TextView) getNavigationView().findViewById(R.id.profile_config_tv)).setText("Создать Профиль");
                    ((CircleImageView)getNavigationView().findViewById(R.id.profile_image)).setImageResource(R.drawable.avatar2);
                    updateCurFragment(1);
                }
                else {
                    ((TextView) getNavigationView().findViewById(R.id.username_tv)).setText(AccountGeneralUtils.curUser.getName());
                    switch (AccountGeneralUtils.curUser.getGenderId()){
                        case 1:
                            ((CircleImageView)getNavigationView().findViewById(R.id.profile_image))
                                    .setImageResource(R.drawable.avatar2);
                            break;
                        case 2:
                            ((CircleImageView)getNavigationView().findViewById(R.id.profile_image))
                                    .setImageResource(R.drawable.boy_avatar);
                            break;
                        case 3:
                            ((CircleImageView)getNavigationView().findViewById(R.id.profile_image))
                                    .setImageResource(R.drawable.girl_avatar);
                            break;
                    }
                }
            }
            return;
        }
        /*if (requestCode == REQUEST_OAUTH_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                super.onActivityResult(requestCode, resultCode, data);
            }

        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void updateCurFragment(int commitType){
        Fragment newFragment;
        switch (curFragmentTag){
            case "REMINDER_FRAGMENT":
                newFragment = ReminderFragment.newInstance();
                break;
            case "GOOGLE_FIT_FRAGMENT":
                newFragment = GoogleFitFragment.newInstance();
                break;
            case "HELP_FRAGMENT":
                newFragment = HelpFragment.newInstance();
                break;
            case "SETTINGS_FRAGMENT":
                newFragment = SettingsFragment.newInstance();
                break;
            case "HISTORY_FRAGMENT":
                newFragment = HistoryFragment.newInstance();
                break;
            case "STATISTIC_FRAGMENT":
                newFragment = StatisticListFragment.newInstance();
                break;
            case "MAIN_FRAGMENT":
                newFragment = MainFragment.newInstance();
                break;
            default:
                newFragment = MainFragment.newInstance();
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, newFragment, curFragmentTag);
        if (commitType == 0){
            transaction.commit();
        }
        else {
            transaction.commitAllowingStateLoss();
        }
    }

    public NavigationView getNavigationView() {
        return navigationView;
    }

    public String getCurFragmentTag() {
        return curFragmentTag;
    }

    public void setCurFragmentTag(String curFragmentTag) {
        this.curFragmentTag = curFragmentTag;
    }

    public static int getRESULT_SIGN_IN_CODE() {
        return RESULT_SIGN_IN_CODE;
    }

    public static int getRESULT_USER_CONFIG_CODE() {
        return RESULT_USER_CONFIG_CODE;
    }
}
