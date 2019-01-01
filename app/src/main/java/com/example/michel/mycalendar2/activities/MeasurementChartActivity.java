package com.example.michel.mycalendar2.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.github.mikephil.charting.charts.LineChart;

import java.util.Calendar;
import java.util.List;

public class MeasurementChartActivity extends AppCompatActivity {
    private LineChart chart;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_chart);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle arguments = getIntent().getExtras();

        int idMeasurementReminder = arguments.getInt("id");
        String startDateStr = arguments.getString("startDateStr");
        String endDateStr = arguments.getString("endDateStr");

        chart = (LineChart) findViewById(R.id.measurement_chart);

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        List<float[]> measurementReminderEntryValues = databaseAdapter.getMeasurementReminderEntriesPerMonth(idMeasurementReminder, startDateStr);
        databaseAdapter.close();
    }

    public void onButtonBeforeClick(View view) {
    }

    public void onButtonNextClick(View view) {
    }
}
