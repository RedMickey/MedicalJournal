package com.example.michel.mycalendar2.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.michel.mycalendar2.adapters.TimesOfTakingMedicineAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.expandableLayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.ListIterator;

public class AddTreatmentActivity extends AppCompatActivity {
    private ExpandableRelativeLayout mExpandLayout1;
    private ExpandableRelativeLayout mExpandLayout2;

    private RadioGroup radioGroup12;
    private Calendar cal;
    private Button pickDateButton;
    private DateData pickDateButtonDateData;

    private ArrayList<String> time = new ArrayList();
    //private ListView timesOfTakingMedicine;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        pickDateButtonDateData = new DateData();

        mExpandLayout1 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout);
        mExpandLayout2 = (ExpandableRelativeLayout) findViewById(R.id.expandableLayout2);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup);

        radioGroup12 = (RadioGroup) findViewById(R.id.radioGroup2);

        timesOfTakingMedicine = (LinearLayout) findViewById(R.id.times_of_taking_medicine);
        cal = Calendar.getInstance();
        time.add(String.valueOf(cal.get(Calendar.HOUR_OF_DAY))+":00");

        pickDateButton = (Button) findViewById(R.id.pick_date_button);
        pickDateButton.setText(setDate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR)));

        timesOfTakingMedicineAdapter = new TimesOfTakingMedicineAdapter(this, R.layout.reminder_time_item, time);

        for (int i = 0; i < timesOfTakingMedicineAdapter.getCount(); i++) {
            View item = timesOfTakingMedicineAdapter.getView(i, null, null);
            timesOfTakingMedicine.addView(item);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

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

        ((RadioGroup) findViewById(R.id.radioGroup2)).setOnCheckedChangeListener(
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

    private String setDate(int day, int month, int year){
        pickDateButtonDateData.setDay(day);
        pickDateButtonDateData.setMonth(month);
        pickDateButtonDateData.setYear(year);

        return String.valueOf(day)+" "+ CalendarUtil.getShortMonthName(month)
                +" "+String.valueOf(year)+" г.";
    }

    public void onPickDate(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                pickDateButton.setText(setDate(i2, i1+1, i));
                pickDateButtonDateData.setDay(i2);
                pickDateButtonDateData.setMonth(i1+1);
                pickDateButtonDateData.setYear(i);
            }
        }, pickDateButtonDateData.getYear(), pickDateButtonDateData.getMonth()-1, pickDateButtonDateData.getDay());
        datePickerDialog.show();
    }
}
