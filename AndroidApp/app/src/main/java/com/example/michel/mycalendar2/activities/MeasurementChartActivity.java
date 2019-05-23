package com.example.michel.mycalendar2.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.michel.mycalendar2.controllers.app_async_tasks.MeasurementChartCreationTask;
import com.example.michel.mycalendar2.view.custom_views.calendarview.data.DateData;
import com.example.michel.mycalendar2.view.custom_views.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.models.measurement.MeasurementStatEntry;
import com.example.michel.mycalendar2.utils.ConvertingUtils;
import com.example.michel.mycalendar2.utils.DBStaticEntries;
import com.github.mikephil.charting.charts.LineChart;

import java.util.Calendar;
import java.util.Date;

public class MeasurementChartActivity extends AppCompatActivity {
    private LineChart chart;
    private Calendar calendar;
    private DateData startDate;
    private DateData endDate;
    private ImageButton buttonMonthNext;
    private ImageButton buttonMonthBefore;
    private TextView textViewCurrentDate;
    private Spinner reminderTimeSpinner;
    private MeasurementStatEntry mse;
    private MeasurementChartActivity selfMca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle arguments = getIntent().getExtras();
        selfMca = this;

        buttonMonthBefore = (ImageButton) findViewById(R.id.button_month_before);
        buttonMonthNext = (ImageButton) findViewById(R.id.button_month_next);
        textViewCurrentDate = (TextView) findViewById(R.id.text_current_date);
        reminderTimeSpinner = (Spinner) findViewById(R.id.toolbar_reminder_time_spinner);
        chart = (LineChart) findViewById(R.id.measurement_chart);
        chart.setNoDataText("Загрузка...");

        mse = arguments.getParcelable("mse");
        calendar = Calendar.getInstance();
        String[] startDateStrArr = mse.getStartDate().split("\\.");
        String[] endDateStrArr = mse.getEndDate().split("\\.");
        startDate = new DateData(Integer.valueOf(startDateStrArr[2]), Integer.valueOf(startDateStrArr[1]), Integer.valueOf(startDateStrArr[0]));
        endDate = new DateData(Integer.valueOf(endDateStrArr[2]), Integer.valueOf(endDateStrArr[1]), Integer.valueOf(endDateStrArr[0]));

        getSupportActionBar().setTitle(DBStaticEntries.getMeasurementTypeById(mse.getIdMeasurementType()).getName());
        if (!dateBelongsTimePeriod(calendar.getTime(), startDate, endDate)){
            calendar.set(Calendar.YEAR, endDate.getYear());
            calendar.set(Calendar.MONTH, endDate.getMonth()-1);
            buttonMonthNext.setEnabled(false);
        }

        String curValueStr = "";
        String standardValueStr = "";
        String[] curValueAndStandardValueStrs = null;
        switch (mse.getIdMeasurementType()){
            case 1:
                curValueAndStandardValueStrs = createCurValueAndStandardValueStrs(mse);
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
            case 3:
                curValueAndStandardValueStrs = createCurValueAndStandardValueStrs(mse);
                curValueStr = curValueAndStandardValueStrs[0];
                standardValueStr = curValueAndStandardValueStrs[1];
                break;
            case 4:
                curValueAndStandardValueStrs = createCurValueAndStandardValueStrs(mse);
                curValueStr = curValueAndStandardValueStrs[0];
                standardValueStr = curValueAndStandardValueStrs[1];
                break;
            case 5:
                curValueAndStandardValueStrs = createCurValueAndStandardValueStrs(mse);
                curValueStr = curValueAndStandardValueStrs[0];
                ((LinearLayout)findViewById(R.id.standard_value_layout)).setVisibility(View.GONE);
                //standardValueStr = curValueAndStandardValueStrs[1];
                break;
            case 6:
                curValueAndStandardValueStrs = createCurValueAndStandardValueStrs(mse);
                curValueStr = curValueAndStandardValueStrs[0];
                ((LinearLayout)findViewById(R.id.standard_value_layout)).setVisibility(View.GONE);
                //standardValueStr = curValueAndStandardValueStrs[1];
                break;
            case 7:
                curValueAndStandardValueStrs = createCurValueAndStandardValueStrs(mse);
                curValueStr = curValueAndStandardValueStrs[0];
                ((LinearLayout)findViewById(R.id.standard_value_layout)).setVisibility(View.GONE);
                //standardValueStr = curValueAndStandardValueStrs[1];
                break;
            case 8:
                curValueAndStandardValueStrs = createCurValueAndStandardValueStrs(mse);
                curValueStr = curValueAndStandardValueStrs[0];
                ((LinearLayout)findViewById(R.id.standard_value_layout)).setVisibility(View.GONE);
                //standardValueStr = curValueAndStandardValueStrs[1];
                break;
        }

        ((TextView)findViewById(R.id.current_value_tv)).setText(curValueStr);
        ((TextView)findViewById(R.id.standard_value_tv)).setText(standardValueStr);

        textViewCurrentDate.setText(CalendarUtil.getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1));

        if (startDate.getYear() == calendar.get(Calendar.YEAR)&&startDate.getMonth() == (calendar.get(Calendar.MONTH)+1)){
            buttonMonthBefore.setEnabled(false);
        }
        if (endDate.getYear() == calendar.get(Calendar.YEAR)&&endDate.getMonth() == (calendar.get(Calendar.MONTH)+1)){
            buttonMonthNext.setEnabled(false);
        }

        reminderTimeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(view.getContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
                MeasurementChartCreationTask mcct = new MeasurementChartCreationTask(
                        selfMca, mse.getIdMeasurementType(), mse.getMeasurementValueTypeStr(),
                        mse.getId());
                mcct.execute(calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR), i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        reminderTimeSpinner.setSelection(4);

        /*MeasurementChartCreationTask mcct = new MeasurementChartCreationTask(
                this, mse.getIdMeasurementType(), mse.getMeasurementValueTypeStr());
        mcct.execute(mse.getId(), endDate.getMonth(), endDate.getYear(), 4);*/
    }

    public void onButtonBeforeClick(View view) {
        calendar.add(Calendar.MONTH, -1);
        if (startDate.getYear() == calendar.get(Calendar.YEAR)&&startDate.getMonth() == (calendar.get(Calendar.MONTH)+1)){
            buttonMonthBefore.setEnabled(false);
        }
        buttonMonthNext.setEnabled(true);
        textViewCurrentDate.setText(CalendarUtil.getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1));
        MeasurementChartCreationTask mcct = new MeasurementChartCreationTask(
                this, mse.getIdMeasurementType(), mse.getMeasurementValueTypeStr(),
                mse.getId());
        mcct.execute(calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR), reminderTimeSpinner.getSelectedItemPosition());
    }

    public void onButtonNextClick(View view) {
        calendar.add(Calendar.MONTH, 1);
        if (endDate.getYear() == calendar.get(Calendar.YEAR)&&endDate.getMonth() == (calendar.get(Calendar.MONTH)+1)){

            buttonMonthNext.setEnabled(false);
        }
        buttonMonthBefore.setEnabled(true);
        textViewCurrentDate.setText(CalendarUtil.getDateString(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1));
        MeasurementChartCreationTask mcct = new MeasurementChartCreationTask(
                this, mse.getIdMeasurementType(), mse.getMeasurementValueTypeStr(),
                mse.getId());
        mcct.execute(calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.YEAR), reminderTimeSpinner.getSelectedItemPosition());
    }

    private String[] createCurValueAndStandardValueStrs(MeasurementStatEntry mse){
        String[] curValueAndStandardValueStrs = new String[]{"", ""};

        if (mse.getAverageCurValues()[0]!=-10000){
            curValueAndStandardValueStrs[0]+=String.format("%.1f", mse.getAverageCurValues()[0]) + " " +
                    createCountTypeEnding(mse, mse.getAverageCurValues()[0]);
        }
        if (mse.getAverageCurValues()[1]!=-10000){
            curValueAndStandardValueStrs[0]+=" - " + String.format("%.1f", mse.getAverageCurValues()[1]) + " " +
                    createCountTypeEnding(mse, mse.getAverageCurValues()[1]);
        }

        if (mse.getStandardValues()[0]!=-10000){
            curValueAndStandardValueStrs[1]+=String.format("%.1f", mse.getStandardValues()[0]) + " " +
                    createCountTypeEnding(mse, mse.getStandardValues()[0]);
        }
        if (mse.getStandardValues()[1]!=-10000){
            curValueAndStandardValueStrs[1]+=" - " + String.format("%.1f", mse.getStandardValues()[1]) + " " +
                    createCountTypeEnding(mse, mse.getStandardValues()[1]);
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

    private String createCountTypeEnding(MeasurementStatEntry mse, double value){
        switch (mse.getIdMeasurementType()){
            case 8:
                return ConvertingUtils.smartEnding((int)value, new String[]{"", "а", "ов"}, mse.getMeasurementValueTypeStr());
            default:
                return mse.getMeasurementValueTypeStr();
        }
    }

    private boolean dateBelongsTimePeriod(Date today, DateData startDD, DateData endDD){
        Calendar calBuf = Calendar.getInstance();
        calBuf.set(startDD.getYear(), startDD.getMonth()-1, startDate.getDay());
        Date startD = calBuf.getTime();
        calBuf.set(endDD.getYear(), endDD.getMonth()-1, endDD.getDay());
        Date endD = calBuf.getTime();
        return !(today.before(startD) || today.after(endD));
    }
}
