package com.example.michel.mycalendar2.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
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
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.michel.mycalendar2.adapters.TimesOfTakingMedicineAdapter;
import com.example.michel.mycalendar2.app_async_tasks.AddTreatmentActivityCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.OneTimePillReminderInsertionTask;
import com.example.michel.mycalendar2.app_async_tasks.PillRemListSpinnerCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.PillRemindersInsertionTask;
import com.example.michel.mycalendar2.app_async_tasks.PillRemindersUpdateTask;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.expandableLayout.ExpandableRelativeLayout;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.util.ArrayList;
import java.util.Calendar;

public class AddOneTimeTreatmentActivity extends AppCompatActivity {
    private ExpandableRelativeLayout mExpandLayout2;

    private RadioGroup radioGroupRegardingMeals;
    private Calendar cal;
    private Button pickDateButton;
    private Button pickTimeButton;
    private DateData pickDateButtonDateData;
    private TextView prescriptionTv;
    private AddOneTimeTreatmentActivity activity;

    private ArrayList<String> time = new ArrayList();

    private LinearLayout timesOfTakingMedicine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_one_time_treatment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

// Declaration

        pickDateButtonDateData = new DateData();

        mExpandLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);

        radioGroupRegardingMeals = (RadioGroup) findViewById(R.id.regarding_meals);

        timesOfTakingMedicine = (LinearLayout) findViewById(R.id.times_of_taking_medicine);
        prescriptionTv = (TextView) findViewById(R.id.prescription_tv);

        pickDateButton = (Button) findViewById(R.id.pick_date_button);
        pickTimeButton = (Button) findViewById(R.id.pick_time_button);

        activity = this;
        Bundle arguments = getIntent().getExtras();

// Declaration end

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                pillReminderDBInsertEntry.setIsActive(1);
                pillReminderDBInsertEntry.setAnnotation("");

                ReminderTime[] reminderTimes = new ReminderTime[1];
                reminderTimes[0] = new ReminderTime (pickTimeButton.getText().toString()+":00");
                pillReminderDBInsertEntry.setReminderTimes(reminderTimes);

                Snackbar.make(view, "ReadyInsert", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();

                OneTimePillReminderInsertionTask otprit = new OneTimePillReminderInsertionTask();
                otprit.execute(pillReminderDBInsertEntry);
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

        prescriptionTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());

                View dialogView = inflater.inflate(R.layout.choosing_pill_reminder_dialog_layout, null, false);


                builder.setView(dialogView)
                    .setCancelable(true);
                AlertDialog dialog = builder.create();
                PillRemListSpinnerCreationTask prlsct = new PillRemListSpinnerCreationTask(dialogView, activity, dialog);
                prlsct.execute();

                dialog.show();
            }
        });

        cal = Calendar.getInstance();

        pickDateButton.setText(setDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR)));
        pickTimeButton.setText(String.format("%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE)));
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

    public String setDate(int day, int month, int year){
        pickDateButtonDateData.setDay(day);
        pickDateButtonDateData.setMonth(month);
        pickDateButtonDateData.setYear(year);

        return String.valueOf(day)+" "+ CalendarUtil.getRusMonthName(month, 0)
                +" "+String.valueOf(year)+" г.";
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

    public void onPickDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                pickDateButton.setText(setDate(i2, i1+1, i));
            }
        }, pickDateButtonDateData.getYear(), pickDateButtonDateData.getMonth()-1, pickDateButtonDateData.getDay());
        datePickerDialog.show();
    }

    public TextView getPrescriptionTv() {
        return prescriptionTv;
    }
}
