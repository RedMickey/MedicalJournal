package com.example.michel.mycalendar2.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.michel.mycalendar2.auxiliary_fragments.ChartMarkerView;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.HealthDataTypes;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getDateTimeInstance;

public class GFitDetailsActivity extends AppCompatActivity {
    private int fitMeasurementType;
    private Calendar calendar;
    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gfit_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();

        fitMeasurementType = arguments.getInt("measurementType", 0);
        chart = (LineChart) findViewById(R.id.measurement_chart);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        Log.i("MT", "fitMeasurementType " + String.valueOf(fitMeasurementType));
        downloadMonthData();
        //downloadDayData();
    }

    private void downloadMonthData(){
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.MONTH) != calendar.get(Calendar.MONTH) || cal.get(Calendar.YEAR) != calendar.get(Calendar.YEAR)){
            cal.setTimeInMillis(calendar.getTimeInMillis());
        }
        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        //cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        long startTime = cal.getTimeInMillis();
        //cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        //long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateTimeInstance();
        Log.i("TAG", "Range Start: " + dateFormat.format(startTime));
        Log.i("TAG", "Range End: " + dateFormat.format(endTime));

        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(queryMonthData(startTime, endTime))
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                printData(dataReadResponse);
                                createChart(dataReadResponse);
                                //setupGeneralData(dataReadResponse);
                                //checkTaskCompletion();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ERR", "There was a problem reading the data.", e);
                                Toast.makeText(getApplicationContext(),"Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                                //checkTaskCompletion();
                            }
                        });
    }

    private void downloadDayData(){
        Calendar cal = Calendar.getInstance();
        if (cal.get(Calendar.MONTH) != calendar.get(Calendar.MONTH) || cal.get(Calendar.YEAR) != calendar.get(Calendar.YEAR)
                || cal.get(Calendar.DAY_OF_MONTH) != calendar.get(Calendar.DAY_OF_MONTH)){
            cal.setTimeInMillis(calendar.getTimeInMillis());
        }
        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateTimeInstance();
        Log.i("TAG", "Range Start: " + dateFormat.format(startTime));
        Log.i("TAG", "Range End: " + dateFormat.format(endTime));

        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(queryDayData(startTime, endTime))
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                printData(dataReadResponse);
                                //setupGeneralData(dataReadResponse);
                                //checkTaskCompletion();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ERR", "There was a problem reading the data.", e);
                                Toast.makeText(getApplicationContext(),"Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                                //checkTaskCompletion();
                            }
                        });
    }

    private DataReadRequest queryMonthData(long startTime, long endTime){

        DataType dataType = null;
        DataType dataAggregate = null;
        switch (fitMeasurementType){
            case 0:
                dataType = DataType.TYPE_STEP_COUNT_DELTA;
                dataAggregate = DataType.AGGREGATE_STEP_COUNT_DELTA;
                break;
            case 1:
                dataType = DataType.TYPE_CALORIES_EXPENDED;
                dataAggregate = DataType.AGGREGATE_CALORIES_EXPENDED;
                break;
            case 2:
                dataType = DataType.TYPE_DISTANCE_DELTA;
                dataAggregate = DataType.AGGREGATE_DISTANCE_DELTA;
                break;
            case 3:
                dataType = DataType.TYPE_MOVE_MINUTES;
                dataAggregate = DataType.AGGREGATE_MOVE_MINUTES;
                break;
            case 4:
                dataType = DataType.TYPE_HEART_RATE_BPM;
                dataAggregate = DataType.AGGREGATE_HEART_RATE_SUMMARY;
                break;
            case 5:
                dataType = DataType.TYPE_WEIGHT;
                dataAggregate = DataType.AGGREGATE_WEIGHT_SUMMARY;
                break;
            case 6:
                dataType = HealthDataTypes.TYPE_BLOOD_PRESSURE;
                dataAggregate = HealthDataTypes.AGGREGATE_BLOOD_PRESSURE_SUMMARY;
                break;
        }

        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        .aggregate(dataType, dataAggregate)
                        .bucketByTime(1, TimeUnit.DAYS)
                        //.enableServerQueries()
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();

        return readRequest;
    }

    private DataReadRequest queryDayData(long startTime, long endTime){

        DataType dataType = null;
        DataType dataAggregate = null;
        switch (fitMeasurementType){
            case 0:
                dataType = DataType.TYPE_STEP_COUNT_DELTA;
                dataAggregate = DataType.AGGREGATE_STEP_COUNT_DELTA;
                break;
            case 1:
                dataType = DataType.TYPE_CALORIES_EXPENDED;
                dataAggregate = DataType.AGGREGATE_CALORIES_EXPENDED;
                break;
            case 2:
                dataType = DataType.TYPE_DISTANCE_DELTA;
                dataAggregate = DataType.AGGREGATE_DISTANCE_DELTA;
                break;
            case 3:
                dataType = DataType.TYPE_MOVE_MINUTES;
                dataAggregate = DataType.AGGREGATE_MOVE_MINUTES;
                break;
            case 4:
                dataType = DataType.TYPE_HEART_RATE_BPM;
                break;
            case 5:
                dataType = DataType.TYPE_WEIGHT;
                break;
            case 6:
                dataType = HealthDataTypes.TYPE_BLOOD_PRESSURE;
                break;
        }

        DataReadRequest readRequest = null;

        if (dataAggregate != null)
            readRequest = new DataReadRequest.Builder()
                        //.read(dataType)
                        .aggregate(dataType, dataAggregate)
                        .bucketByTime(30, TimeUnit.MINUTES)
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();
        else
            readRequest = new DataReadRequest.Builder()
                    .read(dataType)
                    .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                    .build();

        return readRequest;
    }

    private void createChart(DataReadResponse dataReadResult){
        int[] daysOfMonth = new int[CalendarUtil.getDaysInMonth(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR))];
        List<Entry> zeroEntries = new ArrayList<Entry>();
        List<Entry> dataEntries1 = new ArrayList<Entry>();
        List<Entry> dataEntries2 = new ArrayList<Entry>();
        float zeroY = 30.01f;
        String labelStr1 = "";
        String labelStr2 = "";

        Calendar calBuf = Calendar.getInstance();
        if (fitMeasurementType!=6){
            labelStr1 = getMeasurementName(fitMeasurementType) + "  ";
            for (Bucket bucket : dataReadResult.getBuckets()){
                if (bucket.getDataSets().get(0).getDataPoints().size()>0) {
                    DataPoint dataPoint = bucket.getDataSets().get(0).getDataPoints().get(0);
                    calBuf.setTimeInMillis(dataPoint.getStartTime(TimeUnit.MILLISECONDS));
                    dataEntries1.add(new Entry(calBuf.get(Calendar.DAY_OF_MONTH), dataPoint.getValue(Field.FIELD_STEPS).asInt()));
                }
            }
            if (dataEntries1.size()>0)
                zeroY = dataEntries1.get(0).getY();
        }
        else {

        }

        zeroY+=0.01f;
        for(int i = 0; i<daysOfMonth.length;i++){
            daysOfMonth[i] = i+1;
            zeroEntries.add(new Entry(daysOfMonth[i], zeroY));
        }

        LineDataSet zeroDataSet = new LineDataSet(zeroEntries, ""); // add entries to dataset
        zeroDataSet.setLineWidth(2.5f);
        zeroDataSet.setCircleRadius(3);
        zeroDataSet.setColors(new int[]{R.color.transparent}, this);
        zeroDataSet.setCircleColors(new int[]{R.color.transparent}, this);
        zeroDataSet.setCircleColorHole(ContextCompat.getColor(this, R.color.transparent));
        zeroDataSet.setDrawValues(false);
        zeroDataSet.setDrawHighlightIndicators(false);

        LineDataSet dataSet1 = new LineDataSet(dataEntries1, labelStr1); // add entries to dataset
        dataSet1.setLineWidth(2.5f);
        dataSet1.setCircleRadius(3);
        dataSet1.setDrawValues(false);

        LineDataSet dataSet2 = new LineDataSet(dataEntries2, labelStr2); // add entries to dataset
        dataSet2.setLineWidth(2.5f);
        dataSet2.setCircleRadius(3);
        dataSet2.setColors(new int[]{R.color.material_green_200}, this);
        dataSet2.setCircleColors(new int[]{R.color.material_green_200}, this);
        dataSet2.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet1);
        if (dataEntries2.size() != 0)
            dataSets.add(dataSet2);
        dataSets.add(zeroDataSet);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);

        // no description text
        chart.getDescription().setEnabled(false);

        chart.getAxisLeft().setDrawGridLines(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.animateX(1000, Easing.EasingOption.Linear);

        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(14f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        LineData lineData = new LineData(dataSets);
        //lineData.setValueFormatter(new MyYAxisValueFormatter());

        ChartMarkerView cmv = new ChartMarkerView(this, R.layout.chart_marker_view, "шаг.", zeroY);

        // Set the marker to the chart
        cmv.setChartView(chart);
        chart.setMarker(cmv);

        chart.setData(lineData);
        chart.invalidate(); // refresh
    }



    private void printData(DataReadResponse dataReadResult) {
        // [START parse_read_data_result]
        // If the DataReadRequest object specified aggregated data, dataReadResult will be returned
        // as buckets containing DataSets, instead of just DataSets.
        if (dataReadResult.getBuckets().size() > 0) {
            Log.i(
                    "TAG", "Number of returned buckets of DataSets is: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    dumpDataSet(dataSet);
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            Log.i("TAG", "Number of returned DataSets is: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                dumpDataSet(dataSet);
            }
        }
        // [END parse_read_data_result]
    }

    // [START parse_dataset]
    private void dumpDataSet(DataSet dataSet) {
        Log.i("SET", "Data returned for Data type: " + dataSet.getDataType().getName());
        //DateFormat dateFormat = getTimeInstance();
        //Field.FIELD_STEPS;
        DateFormat dateFormat = getDateTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.i("T", "Data point:");
            Log.i("T", "\tType: " + dp.getDataType().getName());
            Log.i("T", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.i("T", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.i("V", "\tField: " + field.getName() + " Value: " + dp.getValue(field));
            }
        }
    }

    // function will be replaced in future
    private String getMeasurementName(int index){
        switch (index){
            case 0:
                return "Количество шагов";
            case 1:
                return "Потраченные ккал";
            case 2:
                return "Пройденное расстояние, км";
            case 3:
                return "Минуты активности";
            case 4:
                return "Пульс, уд/мин";
            case 5:
                return "Вес, кг";
                default:
                    return "";
        }
    }

}
