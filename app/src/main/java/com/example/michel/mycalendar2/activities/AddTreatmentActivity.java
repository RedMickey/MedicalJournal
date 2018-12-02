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
import com.example.michel.mycalendar2.app_async_tasks.AddTreatmentActivityCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.PillNotificationsCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.PillRemindersInsertionTask;
import com.example.michel.mycalendar2.app_async_tasks.PillRemindersUpdateTask;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.expandableLayout.ExpandableRelativeLayout;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

public class AddTreatmentActivity extends AppCompatActivity {
    private ExpandableRelativeLayout mExpandLayout1;
    private ExpandableRelativeLayout mExpandLayout2;

    private RadioGroup radioGroupCycleType;
    private RadioGroup radioGroupRegardingMeals;
    private Calendar cal;
    private Button pickDateButton;
    private DateData pickDateButtonDateData;
    PillReminderDBInsertEntry oldPillReminder;
    private int idWeekSchedule = 0;
    private TextView active_ind_tv;

    private ArrayList<String> time = new ArrayList();

    private LinearLayout timesOfTakingMedicine;

    private TimesOfTakingMedicineAdapter timesOfTakingMedicineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_treatment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //active_ind_tv = (TextView)findViewById(R.id.active_ind_tv_CaddT); // must be deleted

// Declaration

        pickDateButtonDateData = new DateData();

        active_ind_tv = (TextView)findViewById(R.id.active_ind_tv_CaddT);

        mExpandLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout);
        mExpandLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);

        radioGroupCycleType = (RadioGroup) findViewById(R.id.cycle_type);

        radioGroupRegardingMeals = (RadioGroup) findViewById(R.id.regarding_meals);

        timesOfTakingMedicine = (LinearLayout) findViewById(R.id.times_of_taking_medicine);

        pickDateButton = (Button) findViewById(R.id.pick_start_date_button);

        timesOfTakingMedicineAdapter = new TimesOfTakingMedicineAdapter(this, R.layout.reminder_time_item, time);

        Bundle arguments = getIntent().getExtras();

// Declaration end

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

                PillReminderDBInsertEntry pillReminderDBInsertEntry = new PillReminderDBInsertEntry();

                switch (radioGroupRegardingMeals.getCheckedRadioButtonId()){
                    case R.id.before_meals:
                        pillReminderDBInsertEntry.setIdHavingMealsType(1);
                        try {
                            pillReminderDBInsertEntry.setHavingMealsTime(
                                    -Integer.parseInt(((EditText) findViewById(R.id.count_of_minutes)).getText().toString())
                            );
                        }
                        catch (NumberFormatException ex){
                            Toast.makeText(view.getContext(),"Введите количество минут",Toast.LENGTH_LONG).show();
                            return;
                        }
                        break;
                    case R.id.with_meals:
                        pillReminderDBInsertEntry.setIdHavingMealsType(2);
                        break;
                    case R.id.after_meals:
                        pillReminderDBInsertEntry.setIdHavingMealsType(3);
                        try {
                            pillReminderDBInsertEntry.setHavingMealsTime(
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

                pillReminderDBInsertEntry.setPillName(
                        ((EditText)findViewById(R.id.edit_text_medicine_name)).getText().toString()
                );
                if (pillReminderDBInsertEntry.getPillName().equals("")){
                    Toast.makeText(view.getContext(),"Введите название препарата",Toast.LENGTH_LONG).show();
                    return;
                }
                try {
                pillReminderDBInsertEntry.setPillCount(
                        Integer.parseInt(((EditText)findViewById(R.id.edit_text_dose)).getText().toString())
                );
                }
                catch (NumberFormatException ex){
                    Toast.makeText(view.getContext(),"Введите дозу",Toast.LENGTH_LONG).show();
                    return;
                }
                pillReminderDBInsertEntry.setIdPillCountType(
                        DBStaticEntries.doseTypes.get(
                                ((Spinner)findViewById(R.id.dose_type)).getSelectedItem().toString()
                        )
                );
                pillReminderDBInsertEntry.setStartDate(pickDateButtonDateData);
                pillReminderDBInsertEntry.setAnnotation(
                        ((EditText)findViewById(R.id.annotation)).getText().toString()
                );

                ReminderTime[] reminderTimes = new ReminderTime[timesOfTakingMedicine.getChildCount()];
                for ( int j = 0; j < timesOfTakingMedicine.getChildCount(); j++) {
                    LinearLayout bView = (LinearLayout) timesOfTakingMedicine.getChildAt(j);
                    //reminderTimes[j] = ((EditText)bView.findViewById(R.id.reminder_time)).getText().toString()+":00";
                    reminderTimes[j] = new ReminderTime (((EditText)bView.findViewById(R.id.reminder_time)).getText().toString()+":00");
                }
                pillReminderDBInsertEntry.setReminderTimes(reminderTimes);

                if (getIntent().getExtras() == null) {
                    pillReminderDBInsertEntry.setIsActive(1);
                    Snackbar.make(view, "ReadyInsert", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    PillRemindersInsertionTask rct = new PillRemindersInsertionTask(getApplicationContext());
                    rct.execute(new CycleAndPillComby(cycleDBInsertEntry, pillReminderDBInsertEntry));
                }
                else {
                    pillReminderDBInsertEntry.setIsActive(((Switch)findViewById(R.id.switch_active_type)).isChecked()?1:0);
                    pillReminderDBInsertEntry.setIdCycle(oldPillReminder.getIdCycle());
                    cycleDBInsertEntry.setIdCycle(oldPillReminder.getIdCycle());
                    cycleDBInsertEntry.setIdWeekSchedule(idWeekSchedule);
                    pillReminderDBInsertEntry.setIdPillReminder(oldPillReminder.getIdPillReminder());
                    Snackbar.make(view, "ReadyUpdate", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    PillRemindersUpdateTask rut = new PillRemindersUpdateTask(
                            !pillReminderDBInsertEntry.getPillName().equals(oldPillReminder.getPillName()),
                            getApplicationContext());
                    rut.execute(new CycleAndPillComby(cycleDBInsertEntry, pillReminderDBInsertEntry));
                }
            }
        });

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


        if (arguments == null){
            //active_ind_tv.setText("Empty");
            ((LinearLayout)findViewById(R.id.active_status_layout)).setVisibility(View.GONE);

            cal = Calendar.getInstance();
            time.add(String.valueOf(cal.get(Calendar.HOUR_OF_DAY))+":00");

            pickDateButton.setText(setDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR)));

            for (int i = 0; i < timesOfTakingMedicineAdapter.getCount(); i++) {
                View item = timesOfTakingMedicineAdapter.getView(i, null, null);
                timesOfTakingMedicine.addView(item);
            }
        }
        else {
            int id = arguments.getInt("PillReminderID");
            AddTreatmentActivityCreationTask addTreatmentActivityCreationTask = new AddTreatmentActivityCreationTask(
                    this, pickDateButtonDateData, timesOfTakingMedicineAdapter);
            addTreatmentActivityCreationTask.execute(id);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_treatment_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_delete_ATA:
                if (getIntent().getExtras() != null){
                    AlertDialog.Builder builder = new AlertDialog.Builder(AddTreatmentActivity.this);
                    builder.setTitle("Удалить данные")
                            .setMessage(R.string.delete_pill_message)
                            .setPositiveButton(R.string.d_agree, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    PillNotificationsCreationTask nctOld = new PillNotificationsCreationTask(1);
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
                                    dbAdapter.open();
                                    dbAdapter.deletePillReminderEntriesByPillReminderId(oldPillReminder.getIdPillReminder());
                                    dbAdapter.deleteReminderTimeByReminderId(oldPillReminder.getIdPillReminder(), 0);
                                    if (idWeekSchedule!=0)
                                        dbAdapter.deleteWeekScheduleByIdCascade(idWeekSchedule);
                                    dbAdapter.deleteCycleByIdCascade(oldPillReminder.getIdCycle());
                                    dbAdapter.close();
                                    PillNotificationsCreationTask nctNew = new PillNotificationsCreationTask(2);
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
                    Toast.makeText(AddTreatmentActivity.this,"Действие невозможно",Toast.LENGTH_LONG).show();
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
        //timesOfTakingMedicineAdapter.add(String.valueOf(cal.get(Calendar.HOUR_OF_DAY))+":"+String.valueOf(cal.get(Calendar.SECOND)));
        timesOfTakingMedicineAdapter.notifyDataSetChanged();
        View item = timesOfTakingMedicineAdapter.getView(timesOfTakingMedicineAdapter.getCount()-1, null, null);
        timesOfTakingMedicine.addView(item);
    }

    public void delete_reminder_time(View view) {
        int j = 0;
        if (timesOfTakingMedicine.getChildCount()>1) {
            for (j = 0; j < timesOfTakingMedicine.getChildCount(); j++) {
                LinearLayout bView = (LinearLayout) timesOfTakingMedicine.getChildAt(j);
                if (bView == ((View) view.getParent()))
                    //Log.i("del",String.valueOf(j));
                    break;
            }
            timesOfTakingMedicineAdapter.remove(time.get(j));
            timesOfTakingMedicineAdapter.notifyDataSetChanged();
            timesOfTakingMedicine.removeView((View) view.getParent());
            /*Log.i("del",String.valueOf(time.size()));
            for (String s: time) {
                Log.i("timeStr",s);
            }*/
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

    public void setOldPillReminder(PillReminderDBInsertEntry oldPillReminder) {
        this.oldPillReminder = oldPillReminder;
    }

    public void setIdWeekSchedule(int idWeekSchedule) {
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
