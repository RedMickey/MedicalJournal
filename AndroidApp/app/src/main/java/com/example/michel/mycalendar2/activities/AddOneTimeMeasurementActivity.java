package com.example.michel.mycalendar2.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.michel.mycalendar2.adapters.TimesOfTakingMedicineAdapter;
import com.example.michel.mycalendar2.app_async_tasks.AddMeasurementActivityCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.MeasurementNotificationsCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.MeasurementRemindersInsertionTask;
import com.example.michel.mycalendar2.app_async_tasks.MeasurementRemindersUpdateTask;
import com.example.michel.mycalendar2.app_async_tasks.OneTimeMeasurementReminderInsertionTask;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.expandableLayout.ExpandableRelativeLayout;
import com.example.michel.mycalendar2.models.CycleAndMeasurementComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderDBEntry;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;


public class AddOneTimeMeasurementActivity extends AppCompatActivity {
    private ExpandableRelativeLayout mExpandLayout2;

    private RadioGroup radioGroupCycleType;
    private RadioGroup radioGroupRegardingMeals;
    private Calendar cal;
    private Button pickDateButton;
    private Button pickTimeButton;
    private DateData pickDateButtonDateData;
    private TextView measurementTypeTv;
    private EditText valueEt1;
    private EditText valueEt2 = null;
    int measurementTypeId;

    private ArrayList<String> time = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_one_time_measurement);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                ReminderTime[] reminderTimes = new ReminderTime[1];
                reminderTimes[0] = new ReminderTime (pickTimeButton.getText().toString()+":00");
                measurementReminderDBEntry.setReminderTimes(reminderTimes);
                measurementReminderDBEntry.setIdMeasurementType(measurementTypeId);

                measurementReminderDBEntry.setIsActive(1);
                Snackbar.make(view, "ReadyInsert", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                double value1 = Double.parseDouble(valueEt1.getText().toString());
                double value2 = valueEt2 == null?-10000:Double.parseDouble(valueEt2.getText().toString());

                OneTimeMeasurementReminderInsertionTask otmrit = new OneTimeMeasurementReminderInsertionTask(value1, value2);
                otmrit.execute(measurementReminderDBEntry);
            }
        });

        // Declaration
        pickDateButtonDateData = new DateData();

        mExpandLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);

        radioGroupCycleType = (RadioGroup) findViewById(R.id.cycle_type);

        radioGroupRegardingMeals = (RadioGroup) findViewById(R.id.regarding_meals);

        pickDateButton = (Button) findViewById(R.id.pick_date_button);
        pickTimeButton = (Button) findViewById(R.id.pick_time_button);

        Bundle arguments = getIntent().getExtras();

        String measurementName = arguments.getString("MeasurementName");
        measurementTypeId = arguments.getInt("MeasurementTypeID");
        int id = arguments.getInt("MeasurementReminderID");

        // Declaration end

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

        getSupportActionBar().setTitle(measurementName);
        cal = Calendar.getInstance();
        switch (measurementTypeId){
            case 1:
                ((ImageView) findViewById(R.id.measurement_type_iv1)).setImageResource(R.drawable.ic_thermometer);
                defineTableRow(0);

                break;
            case 2:
                ((ImageView) findViewById(R.id.measurement_type_iv2)).setImageResource(R.drawable.ic_tonometer);
                defineTableRow(1);
                break;
        }

        measurementTypeTv.setText(DBStaticEntries.getMeasurementTypeById(measurementTypeId).getMeasurementValueTypeName());

        pickDateButton.setText(setDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR)));
        pickTimeButton.setText(String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));
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

    public void onPickTime(View view) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                pickTimeButton.setText(String.format("%02d:%02d", i, i1));
                cal.set(Calendar.HOUR_OF_DAY, i);
                cal.set(Calendar.MINUTE, i1);
            }
        }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void defineTableRow(int type){
        if (type == 0){
            measurementTypeTv = (TextView) findViewById(R.id.measurement_type_tv1);
            valueEt1 = (EditText) findViewById(R.id.value1_et1);
        }
        else {
            measurementTypeTv = (TextView) findViewById(R.id.measurement_type_tv2);
            ((TableRow) findViewById(R.id.values_tr1)).setVisibility(View.GONE);
            ((TableRow) findViewById(R.id.values_tr2)).setVisibility(View.VISIBLE);
            valueEt1 = (EditText) findViewById(R.id.value1_et2);
            valueEt2 = (EditText) findViewById(R.id.value2_et2);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
