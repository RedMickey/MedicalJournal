package com.example.michel.mycalendar2.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.michel.mycalendar2.controllers.app_async_tasks.ChoosingMeasurCourseActCreationTask;

import java.util.Date;

public class ChoosingMeasurementCourseActivity extends AppCompatActivity {
    private int idMeasurementType;
    private Date curDate;
    private double value1;
    private double value2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosing_measurement_course);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();

        idMeasurementType = arguments.getInt("fitMeasurementType");
        value1 = arguments.getDouble("value1");
        value2 = arguments.getDouble("value2");
        try {
            curDate = new Date(arguments.getLong("curDate"));
        }
        catch (Exception ex){
            curDate = new Date();
        }

        ChoosingMeasurCourseActCreationTask cmcact = new ChoosingMeasurCourseActCreationTask(
               this, idMeasurementType, curDate);
        cmcact.execute();
    }

    public int getIdMeasurementType() {
        return idMeasurementType;
    }

    public Date getCurDate() {
        return curDate;
    }

    public double getValue1() {
        return value1;
    }

    public double getValue2() {
        return value2;
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
