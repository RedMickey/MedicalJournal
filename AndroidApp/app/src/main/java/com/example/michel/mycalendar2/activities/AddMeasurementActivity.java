package com.example.michel.mycalendar2.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.michel.mycalendar2.adapters.TimesOfTakingMedicineAdapter;
import com.example.michel.mycalendar2.app_async_tasks.AddMeasurementActivityCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.MeasurementNotificationsCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.MeasurementRemindersInsertionTask;
import com.example.michel.mycalendar2.app_async_tasks.MeasurementRemindersUpdateTask;
import com.example.michel.mycalendar2.app_async_tasks.PillNotificationsCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.synchronization.BeforeDeletionSynchronizationTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.expandableLayout.ExpandableRelativeLayout;
import com.example.michel.mycalendar2.models.CycleAndMeasurementComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderDBEntry;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class AddMeasurementActivity extends AppCompatActivity {
    private ExpandableRelativeLayout mExpandLayout1;
    private ExpandableRelativeLayout mExpandLayout2;

    private RadioGroup radioGroupCycleType;
    private RadioGroup radioGroupRegardingMeals;
    private Calendar cal;
    private Button pickDateButton;
    private DateData pickDateButtonDateData;
    private MeasurementReminderDBEntry oldMeasurementReminder;
    private UUID idWeekSchedule = null;
    private TextView active_ind_tv;
    int measurementTypeId;

    private ArrayList<String> time = new ArrayList();

    private LinearLayout timesOfTakingMeasurement;

    private TimesOfTakingMedicineAdapter timesOfTakingMedicineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_measurement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CycleDBInsertEntry cycleDBInsertEntry = new CycleDBInsertEntry();
                int[] weekSchedule = null;
                switch (radioGroupCycleType.getCheckedRadioButtonId()){
                    case R.id.every_day:
                        cycleDBInsertEntry.setIdCyclingType(DBStaticEntries.cycleTypes.get("every_day"));
                        LinearLayout everyDayPeriodLayout = (LinearLayout) findViewById(R.id.every_day_period);
                        try {
                            setDateParams(cycleDBInsertEntry, everyDayPeriodLayout, 0);
                        }
                        catch (NumberFormatException ex){
                            Toast.makeText(view.getContext(),"Введите период",Toast.LENGTH_LONG).show();
                            return;
                        }
                        break;
                    case R.id.specific_days:
                        cycleDBInsertEntry.setIdCyclingType(DBStaticEntries.cycleTypes.get("specific_days"));

                        GridLayout gridLayout = (GridLayout) findViewById(R.id.specific_days_layout_id);
                        weekSchedule = new int[7];
                        for (int i=7; i< 13; i++){
                            weekSchedule[i-6]=((CheckBox) gridLayout.getChildAt(i)).isChecked() ? 1 : 0;
                        }
                        weekSchedule[0]=((CheckBox) gridLayout.getChildAt(13)).isChecked() ? 1 : 0;
                        cycleDBInsertEntry.setWeekSchedule(weekSchedule);
                        try {
                            setDateParams(cycleDBInsertEntry, (LinearLayout) gridLayout.getChildAt(14), 0);
                        }
                        catch (NumberFormatException ex){
                            Toast.makeText(view.getContext(),"Введите период",Toast.LENGTH_LONG).show();
                            return;
                        }
                        break;
                    case R.id.day_interval:
                        cycleDBInsertEntry.setIdCyclingType(DBStaticEntries.cycleTypes.get("day_interval"));
                        LinearLayout once_aDateLayout = (LinearLayout) findViewById(R.id.once_a_date_layout);
                        try {
                            setDateParams(cycleDBInsertEntry, once_aDateLayout, 1);
                            setDateParams(cycleDBInsertEntry, (LinearLayout) findViewById(R.id.day_interval_period), 0);
                        }
                        catch (NumberFormatException ex){
                            Toast.makeText(view.getContext(),"Введите значения",Toast.LENGTH_LONG).show();
                            return;
                        }
                        break;
                }
                //********************************************************

                MeasurementReminderDBEntry measurementReminderDBEntry = new MeasurementReminderDBEntry();

                switch (radioGroupRegardingMeals.getCheckedRadioButtonId()){
                    case R.id.before_meals:
                        measurementReminderDBEntry.setIdHavingMealsType(1);
                        try {
                            measurementReminderDBEntry.setHavingMealsTime(
                                    -Integer.parseInt(((EditText) findViewById(R.id.count_of_minutes)).getText().toString())
                            );
                        }
                        catch (NumberFormatException ex){
                            Toast.makeText(view.getContext(),"Введите количество минут",Toast.LENGTH_LONG).show();
                            return;
                        }
                        break;
                    case R.id.with_meals:
                        measurementReminderDBEntry.setIdHavingMealsType(2);
                        break;
                    case R.id.after_meals:
                        measurementReminderDBEntry.setIdHavingMealsType(3);
                        try {
                            measurementReminderDBEntry.setHavingMealsTime(
                                    Integer.parseInt(((EditText) findViewById(R.id.count_of_minutes)).getText().toString())
                            );
                        }
                        catch (NumberFormatException ex){
                            Toast.makeText(view.getContext(),"Введите количество минут",Toast.LENGTH_LONG).show();
                            return;
                        }
                        break;
                    default:
                        Log.e("MEALS", "Incorrect ID");
                }

                measurementReminderDBEntry.setStartDate(pickDateButtonDateData);
                measurementReminderDBEntry.setAnnotation(
                        ((EditText)findViewById(R.id.annotation)).getText().toString()
                );

                ReminderTime[] reminderTimes = new ReminderTime[timesOfTakingMeasurement.getChildCount()];
                for ( int j = 0; j < timesOfTakingMeasurement.getChildCount(); j++) {
                    LinearLayout bView = (LinearLayout) timesOfTakingMeasurement.getChildAt(j);
                    //reminderTimes[j] = ((EditText)bView.findViewById(R.id.reminder_time)).getText().toString()+":00";
                    reminderTimes[j] = new ReminderTime (((EditText)bView.findViewById(R.id.reminder_time)).getText().toString()+":00");
                }
                measurementReminderDBEntry.setReminderTimes(reminderTimes);
                measurementReminderDBEntry.setIdMeasurementType(measurementTypeId);

                if (getIntent().getExtras().getString("MeasurementReminderID") == null) {
                    measurementReminderDBEntry.setIsActive(1);
                    /*Snackbar.make(view, "ReadyInsert", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();*/
                    MeasurementRemindersInsertionTask mrit = new MeasurementRemindersInsertionTask(getApplicationContext());
                    mrit.execute(new CycleAndMeasurementComby(cycleDBInsertEntry, measurementReminderDBEntry));
                }
                else {
                    measurementReminderDBEntry.setIsActive(((Switch)findViewById(R.id.switch_active_type)).isChecked()?1:0);
                    measurementReminderDBEntry.setIdCycle(oldMeasurementReminder.getIdCycle());
                    cycleDBInsertEntry.setIdCycle(oldMeasurementReminder.getIdCycle());
                    cycleDBInsertEntry.setIdWeekSchedule(idWeekSchedule);
                    measurementReminderDBEntry.setIdMeasurementReminder(oldMeasurementReminder.getIdMeasurementReminder());
                    /*Snackbar.make(view, "ReadyUpdate", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();*/
                    MeasurementRemindersUpdateTask mrut = new MeasurementRemindersUpdateTask(
                            getApplicationContext());
                    mrut.execute(new CycleAndMeasurementComby(cycleDBInsertEntry, measurementReminderDBEntry));
                }
                onBackPressed();
            }
        });

        // Declaration
        pickDateButtonDateData = new DateData();

        active_ind_tv = (TextView)findViewById(R.id.active_ind_tv_CaddT);

        mExpandLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout);
        mExpandLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);

        radioGroupCycleType = (RadioGroup) findViewById(R.id.cycle_type);

        radioGroupRegardingMeals = (RadioGroup) findViewById(R.id.regarding_meals);

        timesOfTakingMeasurement = (LinearLayout) findViewById(R.id.times_of_taking_measurement);

        pickDateButton = (Button) findViewById(R.id.pick_start_date_button);

        timesOfTakingMedicineAdapter = new TimesOfTakingMedicineAdapter(this, R.layout.reminder_time_item, time);

        Bundle arguments = getIntent().getExtras();

        String measurementName = arguments.getString("MeasurementName");
        measurementTypeId = arguments.getInt("MeasurementTypeID");
        String id = arguments.getString("MeasurementReminderID");

        // Declaration end

        radioGroupCycleType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.every_day:
                        mExpandLayout1.moveChild(0);
                        break;
                    case R.id.specific_days:
                        mExpandLayout1.moveChild(1);
                        break;
                    case R.id.day_interval:
                        mExpandLayout1.moveChild(2);
                        break;
                    default:
                        break;
                }
            }
        });

        radioGroupRegardingMeals.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        for (int j = 0; j < radioGroup.getChildCount(); j++) {
                            ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                            if (view.getId() == i)
                            {
                                view.setChecked(true);
                                switch (j){
                                    case 0:
                                        ((TextView)findViewById(R.id.concerning_meals)).setText("мин до еды");
                                        mExpandLayout2.moveChild(0);
                                        break;
                                    case 1:
                                        mExpandLayout2.collapse();
                                        break;
                                    case 2:
                                        ((TextView)findViewById(R.id.concerning_meals)).setText("мин после еды");
                                        mExpandLayout2.moveChild(0);
                                        break;
                                }
                            }
                            else
                                view.setChecked(false);
                        }
                    }
                }
        );

        ((Switch) findViewById(R.id.switch_active_type)).setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if (b){
                            active_ind_tv.setText("Активное");
                        }
                        else {
                            active_ind_tv.setText("Завершённое");
                        }
                    }
                }
        );

        getSupportActionBar().setTitle(measurementName);

        if (id == null){
            ((LinearLayout)findViewById(R.id.active_status_layout)).setVisibility(View.GONE);

            cal = Calendar.getInstance();
            time.add(String.valueOf(cal.get(Calendar.HOUR_OF_DAY))+":00");

            pickDateButton.setText(setDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR)));

            for (int i = 0; i < timesOfTakingMedicineAdapter.getCount(); i++) {
                View item = timesOfTakingMedicineAdapter.getView(i, null, null);
                timesOfTakingMeasurement.addView(item);
            }
        }
        else {
            AddMeasurementActivityCreationTask addMeasurementActivityCreationTask = new AddMeasurementActivityCreationTask(
                    this, pickDateButtonDateData, timesOfTakingMedicineAdapter);
            addMeasurementActivityCreationTask.execute(UUID.fromString(id));
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete_ATA:
                if (getIntent().getExtras() != null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddMeasurementActivity.this);
                    builder.setTitle("Удалить данные")
                            .setMessage(R.string.delete_measurement_message)
                            .setPositiveButton(R.string.d_agree, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    MeasurementNotificationsCreationTask nctOld = new MeasurementNotificationsCreationTask(1);
                                    try {
                                        nctOld.execute(getApplicationContext()).get();
                                    } catch (ExecutionException ee){
                                        Log.e("Err", ee.getMessage());
                                        return;
                                    }
                                    catch (InterruptedException ie){
                                        Log.e("Err", ie.getMessage());
                                        return;
                                    }

                                    DatabaseAdapter dbAdapter = new DatabaseAdapter();
                                    MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(dbAdapter.open().getDatabase());
                                    ReminderTimeDao reminderTimeDao = new ReminderTimeDao(dbAdapter.getDatabase());
                                    CycleDao cycleDao = new CycleDao(dbAdapter.getDatabase());

                                    if (AccountGeneralUtils.curUser.getId()==1){
                                        measurementReminderDao.deleteMeasurementReminderEntriesByMeasurementReminderId(oldMeasurementReminder.getIdMeasurementReminder());
                                        reminderTimeDao.deleteReminderTimeByReminderId(oldMeasurementReminder.getIdMeasurementReminder(), 1);
                                        if (idWeekSchedule!=null)
                                            cycleDao.deleteWeekScheduleByIdCascade(idWeekSchedule);
                                        cycleDao.deleteCycleByIdCascade(oldMeasurementReminder.getIdCycle());
                                    }
                                    else {
                                        measurementReminderDao.updateBeforeDeletionMeasurementReminderEntriesByMeasurementReminderId(
                                                oldMeasurementReminder.getIdMeasurementReminder());
                                        reminderTimeDao.updateBeforeDeletionReminderTimeByReminderId(oldMeasurementReminder.getIdMeasurementReminder(), 1);
                                        if (idWeekSchedule!=null)
                                            cycleDao.updateBeforeDeletionWeekScheduleById(idWeekSchedule);
                                        cycleDao.updateBeforeDeletionCycle(oldMeasurementReminder.getIdCycle());
                                        measurementReminderDao.updateBeforeDeletionMeasurementReminder(oldMeasurementReminder.getIdMeasurementReminder());
                                        BeforeDeletionSynchronizationTask beforeDeletionSynchronizationTask = new BeforeDeletionSynchronizationTask(
                                                getApplicationContext(), 2
                                        );
                                        beforeDeletionSynchronizationTask.setReminderId(oldMeasurementReminder.getIdMeasurementReminder());
                                        beforeDeletionSynchronizationTask.execute();
                                    }

                                    dbAdapter.close();
                                    MeasurementNotificationsCreationTask nctNew = new MeasurementNotificationsCreationTask(2);
                                    nctNew.execute(getApplicationContext());
                                    onBackPressed();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    Toast.makeText(AddMeasurementActivity.this,"Действие невозможно",Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_treatment_activity, menu);
        return true;
    }

    public void onToggleTB(View view) {
        ((RadioGroup)view.getParent()).check(view.getId());
        if (!((ToggleButton)view).isChecked())
        {
            mExpandLayout2.collapse();
            ((RadioGroup)view.getParent()).clearCheck();
            Toast.makeText(this,"false",Toast.LENGTH_SHORT).show();
        }
    }

    public void add_reminder_time(View view) {
        Calendar cal = Calendar.getInstance();
        timesOfTakingMedicineAdapter.add(String.valueOf(cal.get(Calendar.HOUR_OF_DAY))+":00");
        timesOfTakingMedicineAdapter.notifyDataSetChanged();
        View item = timesOfTakingMedicineAdapter.getView(timesOfTakingMedicineAdapter.getCount()-1, null, null);
        timesOfTakingMeasurement.addView(item);
    }

    public void delete_reminder_time(View view) {
        int j = 0;
        if (timesOfTakingMeasurement.getChildCount()>1) {
            for (j = 0; j < timesOfTakingMeasurement.getChildCount(); j++) {
                LinearLayout bView = (LinearLayout) timesOfTakingMeasurement.getChildAt(j);
                if (bView == ((View) view.getParent()))
                    break;
            }
            timesOfTakingMedicineAdapter.remove(time.get(j));
            timesOfTakingMedicineAdapter.notifyDataSetChanged();
            timesOfTakingMeasurement.removeView((View) view.getParent());
        }
    }

    private void setDateParams(CycleDBInsertEntry cycleDBInsertEntry, LinearLayout linearLayout, int type) throws NumberFormatException{
        Integer period = Integer.parseInt(((EditText)linearLayout.getChildAt(1)).getText().toString());
        String periodDMType = ((Spinner)linearLayout.getChildAt(2)).getSelectedItem().toString();
        if (type==0)
        {
            cycleDBInsertEntry.setPeriod(period); //required verification
            cycleDBInsertEntry.setPeriodDMType(
                    DBStaticEntries.dateTypes.get(periodDMType)
            );
            switch (periodDMType){
                case "дн.":
                    cycleDBInsertEntry.setDayCount(period);
                    break;
                case "нед.":
                    cycleDBInsertEntry.setDayCount(period*7);
                    break;
                case "мес.":
                    cycleDBInsertEntry.setDayCount(period*30);
                    break;
            }
        }
        else {
            cycleDBInsertEntry.setOnce_aPeriod(period); //required verification
            cycleDBInsertEntry.setOnce_aPeriodDMType(
                    DBStaticEntries.dateTypes.get(periodDMType)
            );
            switch (periodDMType){
                case "дн.":
                    cycleDBInsertEntry.setDayInterval(period);
                    break;
                case "нед.":
                    cycleDBInsertEntry.setDayInterval(period*7);
                    break;
                case "мес.":
                    cycleDBInsertEntry.setDayInterval(period*30);
                    break;
            }
        }

    }

    public String setDate(int day, int month, int year){
        pickDateButtonDateData.setDay(day);
        pickDateButtonDateData.setMonth(month);
        pickDateButtonDateData.setYear(year);

        return String.valueOf(day)+" "+ CalendarUtil.getRusMonthName(month, 0)
                +" "+String.valueOf(year)+" г.";
    }

    public void onPickDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                pickDateButton.setText(setDate(i2, i1+1, i));
            }
        }, pickDateButtonDateData.getYear(), pickDateButtonDateData.getMonth()-1, pickDateButtonDateData.getDay());
        datePickerDialog.show();
    }

    public void setOldMeasurementReminder(MeasurementReminderDBEntry oldMeasurementReminder) {
        this.oldMeasurementReminder = oldMeasurementReminder;
    }

    public void setIdWeekSchedule(UUID idWeekSchedule) {
        this.idWeekSchedule = idWeekSchedule;
    }

    public int containsTme(ReminderTime[] array, String v) {

        int index = -1;

        for(int i=0; i<array.length; i++){
            if(array[i].getReminderTimeStr().equals(v)){
                index = i;
                break;
            }
        }

        return index;
    }

}
