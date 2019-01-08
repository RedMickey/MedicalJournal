package com.example.michel.mycalendar2.activities;

import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.michel.mycalendar2.app_async_tasks.MeasurementChartActivityCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.MeasurementChartCreationTask;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.models.measurement.MeasurementStatEntry;
import com.github.mikephil.charting.charts.LineChart;

import java.util.Calendar;

public class MeasurementChartActivity extends AppCompatActivity {
    private LineChart chart;
    private Calendar calendar;
    private DateData startDate;
    private DateData endDate;
    private ImageButton buttonMonthNext;
    private ImageButton buttonMonthBefore;
    private TextView textViewCurrentDate;
    private PopupWindow mPopupWindow;
    private CoordinatorLayout activityMeasurementChartLayout;
    private Spinner reminderTimeSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle arguments = getIntent().getExtras();

        MeasurementStatEntry mse = arguments.getParcelable("mse");
        String[] startDateStrArr = mse.getStartDate().split("\\.");
        String[] endDateStrArr = mse.getEndDate().split("\\.");
        startDate = new DateData(Integer.valueOf(startDateStrArr[2]), Integer.valueOf(startDateStrArr[1]), Integer.valueOf(startDateStrArr[0]));
        endDate = new DateData(Integer.valueOf(endDateStrArr[2]), Integer.valueOf(endDateStrArr[1]), Integer.valueOf(endDateStrArr[0]));

        calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, endDate.getYear());
        calendar.set(Calendar.MONTH, endDate.getMonth()-1);

        String curValueStr = "";
        String standardValueStr = "";
        switch (mse.getIdMeasurementType()){
            case 1:
                String[] curValueAndStandardValueStrs = createCurValueAndStandardValueStrs(mse);
                curValueStr = curValueAndStandardValueStrs[0];
                standardValueStr = curValueAndStandardValueStrs[1];
                break;
            case 2:
                if (mse.getAverageCurValues()[0]!=-10000){
                    curValueStr+="Нижнее: " + String.format("%.1f", mse.getAverageCurValues()[0]) + " " + mse.getMeasurementValueTypeStr();
                }
                if (mse.getAverageCurValues()[1]!=-10000){
                    curValueStr+="\n Верхнее: " + String.format("%.1f", mse.getAverageCurValues()[1]) + " " + mse.getMeasurementValueTypeStr();
                }

                if (mse.getStandardValues()[0]!=-10000){
                    standardValueStr+="Нижнее: " + String.format("%.1f", mse.getStandardValues()[0]) + " " + mse.getMeasurementValueTypeStr();
                }
                if (mse.getStandardValues()[1]!=-10000){
                    standardValueStr+="\n Верхнее: " + String.format("%.1f", mse.getStandardValues()[1]) + " " + mse.getMeasurementValueTypeStr();
                }
                break;
        }

        ((TextView)findViewById(R.id.current_value_tv)).setText(curValueStr);
        ((TextView)findViewById(R.id.standard_value_tv)).setText(standardValueStr);

        chart = (LineChart) findViewById(R.id.measurement_chart);
        buttonMonthBefore = (ImageButton) findViewById(R.id.button_month_before);
        buttonMonthNext = (ImageButton) findViewById(R.id.button_month_next);
        textViewCurrentDate = (TextView) findViewById(R.id.text_current_date);
        activityMeasurementChartLayout = (CoordinatorLayout) findViewById(R.id.activity_measurement_chart_layout);
        reminderTimeSpinner = (Spinner) findViewById(R.id.toolbar_reminder_time_spinner);

        textViewCurrentDate.setText(CalendarUtil.getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1));
        buttonMonthNext.setEnabled(false);
        if (startDate.getYear() == calendar.get(Calendar.YEAR)&&startDate.getMonth() == (calendar.get(Calendar.MONTH)+1)){
            buttonMonthBefore.setEnabled(false);
        }

        reminderTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView)  adapterView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        String monthNumberStr = mse.getEndDate().split("\\.")[1];
        MeasurementChartActivityCreationTask mcact = new MeasurementChartActivityCreationTask(this, mse);
        mcact.execute();

        /*MeasurementChartCreationTask mcct = new MeasurementChartCreationTask(
                this, mse.getIdMeasurementType(), mse.getMeasurementValueTypeStr());
        mcct.execute(mse.getId(), Integer.valueOf(monthNumberStr), 2018);*/
    }

    public void onButtonBeforeClick(View view) {
        /*// Initialize a new instance of LayoutInflater service
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        // Inflate the custom layout/view
        View customView = inflater.inflate(R.layout.popup_window,null);

        // Initialize a new instance of popup window
        mPopupWindow = new PopupWindow(
                customView,
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );

        // Set an elevation value for popup window
        // Call requires API level 21
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        // Get a reference for the custom view close button
        ImageButton closeButton = (ImageButton) customView.findViewById(R.id.ib_close);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });

        // Finally, show the popup window at the center location of root relative layout
        mPopupWindow.showAtLocation(activityMeasurementChartLayout, Gravity.CENTER,0,0);*/
    }

    public void onButtonNextClick(View view) {
    }

    private String[] createCurValueAndStandardValueStrs(MeasurementStatEntry mse){
        String[] curValueAndStandardValueStrs = new String[]{"", ""};

        if (mse.getAverageCurValues()[0]!=-10000){
            curValueAndStandardValueStrs[0]+=String.format("%.1f", mse.getAverageCurValues()[0]) + " " + mse.getMeasurementValueTypeStr();
        }
        if (mse.getAverageCurValues()[1]!=-10000){
            curValueAndStandardValueStrs[0]+=" - " + String.format("%.1f", mse.getAverageCurValues()[1]) + " " + mse.getMeasurementValueTypeStr();
        }

        if (mse.getStandardValues()[0]!=-10000){
            curValueAndStandardValueStrs[1]+=String.format("%.1f", mse.getStandardValues()[0]) + " " + mse.getMeasurementValueTypeStr();
        }
        if (mse.getStandardValues()[1]!=-10000){
            curValueAndStandardValueStrs[1]+=" - " + String.format("%.1f", mse.getStandardValues()[1]) + " " + mse.getMeasurementValueTypeStr();
        }
        return curValueAndStandardValueStrs;
    }

    public TextView getTextViewCurrentDate() {
        return textViewCurrentDate;
    }

    public Spinner getReminderTimeSpinner() {
        return reminderTimeSpinner;
    }

    public DateData getEndDate() {
        return endDate;
    }
}
