package com.example.michel.mycalendar2.main_fragments;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.GFitDetailsActivity;
import com.example.michel.mycalendar2.activities.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.HealthDataTypes;
import com.google.android.gms.fitness.data.HealthFields;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataDeleteRequest;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataUpdateRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getDateTimeInstance;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GoogleFitFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final int REQUEST_OAUTH_REQUEST_CODE = 5541;

    public static GoogleFitFragment newInstance() {
        return new GoogleFitFragment();
    }
    private View mView;
    private FitnessOptions fitnessOptions;
    private Button[] indicatorButtons;
    private RelativeLayout[] measurementLayouts;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int tasksCompletion = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = getView();
        mView=inflater.inflate(R.layout.google_fit_fragment, container, false);
        indicatorButtons = new Button[4];
        measurementLayouts = new RelativeLayout[3];
        indicatorButtons[0] = (Button)mView.findViewById(R.id.steps_but);
        indicatorButtons[1] = (Button)mView.findViewById(R.id.calories_but);
        indicatorButtons[2] = (Button)mView.findViewById(R.id.kilometers_but);
        indicatorButtons[3] = (Button)mView.findViewById(R.id.act_duration_but);
        measurementLayouts[0] = (RelativeLayout)mView.findViewById(R.id.pulse_item);
        measurementLayouts[1] = (RelativeLayout)mView.findViewById(R.id.weight_item);
        measurementLayouts[2] = (RelativeLayout)mView.findViewById(R.id.blood_pressure_item);
        ((TextView)measurementLayouts[0].getChildAt(0)).setText("Пульс");
        ((TextView)measurementLayouts[1].getChildAt(0)).setText("Вес");
        ((TextView)measurementLayouts[2].getChildAt(0)).setText("Артериальное давление");
        swipeRefreshLayout = mView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        fitnessOptions = FitnessOptions.builder()
                        .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                        .addDataType(DataType.TYPE_MOVE_MINUTES, FitnessOptions.ACCESS_READ)
                        .addDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE, FitnessOptions.ACCESS_READ)
                        .build();

        //GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this.getContext(),GoogleSignInOptions.DEFAULT_SIGN_IN);
        //googleSignInClient.signOut();

        indicatorButtons[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GFitDetailsActivity.class);
                intent.putExtra("measurementType", 8);
                startActivity(intent);
            }
        });
        indicatorButtons[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GFitDetailsActivity.class);
                intent.putExtra("measurementType", 6);
                startActivity(intent);
            }
        });
        indicatorButtons[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GFitDetailsActivity.class);
                intent.putExtra("measurementType", 110);
                startActivity(intent);
            }
        });
        indicatorButtons[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GFitDetailsActivity.class);
                intent.putExtra("measurementType", 111);
                startActivity(intent);
            }
        });
        measurementLayouts[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GFitDetailsActivity.class);
                intent.putExtra("measurementType", 3);
                startActivity(intent);
            }
        });
        measurementLayouts[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GFitDetailsActivity.class);
                intent.putExtra("measurementType", 5);
                startActivity(intent);
            }
        });
        measurementLayouts[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GFitDetailsActivity.class);
                intent.putExtra("measurementType", 2);
                startActivity(intent);
            }
        });

        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_dark,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        indicatorButtons[0].setText(Html.fromHtml("<big>0</big><br/>шагов"));
        indicatorButtons[1].setText(Html.fromHtml("<big>0</big><br/>ккал"));
        indicatorButtons[2].setText(Html.fromHtml("<big>0</big><br/>км"));
        indicatorButtons[3].setText(Html.fromHtml("<big>0</big><br/>Мин.акт."));
        if (GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this.getContext()), fitnessOptions)){
            ((RelativeLayout)mView.findViewById(R.id.google_fit_login_layout)).setVisibility(View.GONE);
            //((LinearLayout)mView.findViewById(R.id.google_fit_content_layout)).setVisibility(View.VISIBLE);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            downloadData();
        }
        else {
            ((Button)mView.findViewById(R.id.log_in_google_fit_button)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    login();
                }
            });
        }

        return mView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_OAUTH_REQUEST_CODE) {
                ((RelativeLayout)mView.findViewById(R.id.google_fit_login_layout)).setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                downloadData();
            }
        }
    }

    private void downloadData(){
        tasksCompletion = 2;
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        long startTime = cal.getTimeInMillis();

        Fitness.getHistoryClient(this.getContext(), GoogleSignIn.getLastSignedInAccount(this.getContext()))
                .readData(queryGeneralFitnessData(startTime, endTime))
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                setupGeneralData(dataReadResponse);
                                checkTaskCompletion();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ERR", "There was a problem reading the data.", e);
                                Toast.makeText(getContext(),"Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                                checkTaskCompletion();
                            }
                        });

        Fitness.getHistoryClient(this.getContext(), GoogleSignIn.getLastSignedInAccount(this.getContext()))
                .readData(queryLastMeasurementData(startTime, endTime))
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                setupLastMeasurementData(dataReadResponse);
                                checkTaskCompletion();
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ERR", "There was a problem reading the data.", e);
                                Toast.makeText(getContext(),"Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                                checkTaskCompletion();
                            }
                        });
    }

    private void checkTaskCompletion(){
        tasksCompletion--;
        if (tasksCompletion == 0)
            swipeRefreshLayout.setRefreshing(false);
    }

    private DataReadRequest queryGeneralFitnessData(long startTime, long endTime) {
        java.text.DateFormat dateFormat = getDateInstance();
        Log.i("TAG", "Range Start: " + dateFormat.format(startTime));
        Log.i("TAG", "Range End: " + dateFormat.format(endTime));
        DataReadRequest readRequest =
                    new DataReadRequest.Builder()
                            .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                            .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                            .aggregate(DataType.TYPE_DISTANCE_DELTA, DataType.AGGREGATE_DISTANCE_DELTA)
                            .aggregate(DataType.TYPE_MOVE_MINUTES, DataType.AGGREGATE_MOVE_MINUTES)
                            .bucketByTime(1, TimeUnit.DAYS)
                            .enableServerQueries()
                            .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                            .build();

        return readRequest;
    }

    private DataReadRequest queryLastMeasurementData(long startTime, long endTime) {
        java.text.DateFormat dateFormat = getDateInstance();
        Log.i("TAG", "Range Start: " + dateFormat.format(startTime));
        Log.i("TAG", "Range End: " + dateFormat.format(endTime));
        DataReadRequest readRequest =
                new DataReadRequest.Builder()
                        .read(DataType.TYPE_HEART_RATE_BPM)
                        .read(DataType.TYPE_WEIGHT)
                        .read(HealthDataTypes.TYPE_BLOOD_PRESSURE)
                        .enableServerQueries()
                        .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                        .build();

        return readRequest;
    }

    private void setupGeneralData(DataReadResponse dataReadResult){
        if (dataReadResult.getBuckets().size() > 0) {
            List<DataSet> dataSets = dataReadResult.getBuckets().get(0).getDataSets();
            if (dataSets.get(0).getDataPoints().size()>0)
                indicatorButtons[0].setText(Html.fromHtml(String.format(
                        "<big>%s</big><br/>шагов",
                        dataSets.get(0).getDataPoints().get(0).getValue(Field.FIELD_STEPS)
                        ))
                );
            if (dataSets.get(1).getDataPoints().size()>0)
                indicatorButtons[1].setText(Html.fromHtml(String.format(
                        "<big>%d</big><br/>ккал",
                        (int)dataSets.get(1).getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat()
                        ))
                );
            if (dataSets.get(2).getDataPoints().size()>0)
                indicatorButtons[2].setText(Html.fromHtml(String.format(
                        "<big>%.2f</big><br/>км",
                        dataSets.get(2).getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat()/1000f
                        ))
                );
            if (dataSets.get(3).getDataPoints().size()>0)
                indicatorButtons[3].setText(Html.fromHtml(String.format(
                        "<big>%s</big><br/>Мин.акт.",
                        dataSets.get(3).getDataPoints().get(0).getValue(Field.FIELD_DURATION)
                        ))
                );
        }
    }

    private void setupLastMeasurementData(DataReadResponse dataReadResult){

        List<DataSet> dataSets = dataReadResult.getDataSets();
        DateFormat dateFormat = getDateTimeInstance();
        for (DataSet dataSet : dataSets) {
            Log.i("SET", "Data returned for Data type: " + dataSet.getDataType().getName());
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

        Date now = new Date();
        for (DataSet dataSet : dataSets) {
            if (dataSet.getDataType().equals(DataType.TYPE_HEART_RATE_BPM)&&dataSet.getDataPoints().size()>0){
                DataPoint dp = dataSet.getDataPoints().get(dataSet.getDataPoints().size()-1);
                long timeDifference = now.getTime() - dp.getEndTime(TimeUnit.MILLISECONDS);
                ((TextView)measurementLayouts[0].getChildAt(1)).setText(
                        createMeasurementString(dp.getValue(Field.FIELD_BPM), null, timeDifference/1000, 3)
                );
            }
            else if (dataSet.getDataType().equals(DataType.TYPE_WEIGHT)&&dataSet.getDataPoints().size()>0){
                DataPoint dp = dataSet.getDataPoints().get(dataSet.getDataPoints().size()-1);
                long timeDifference = now.getTime() - dp.getEndTime(TimeUnit.MILLISECONDS);
                ((TextView)measurementLayouts[1].getChildAt(1)).setText(
                        createMeasurementString(dp.getValue(Field.FIELD_WEIGHT), null, timeDifference/1000, 5)
                );

            }
            else if (dataSet.getDataType().equals(HealthDataTypes.TYPE_BLOOD_PRESSURE)&&dataSet.getDataPoints().size()>0){
                DataPoint dp = dataSet.getDataPoints().get(dataSet.getDataPoints().size()-1);
                long timeDifference = now.getTime() - dp.getEndTime(TimeUnit.MILLISECONDS);
                ((TextView)measurementLayouts[2].getChildAt(1)).setText(
                        createMeasurementString(dp.getValue(HealthFields.FIELD_BLOOD_PRESSURE_SYSTOLIC),
                                dp.getValue(HealthFields.FIELD_BLOOD_PRESSURE_DIASTOLIC), timeDifference/1000, 2)
                );
            }

        }
    }

    private String createMeasurementString(Value value1, Value value2, long endTime, int measType){
        String measurementString = "";
        String timeMeasure = "сек.";
        int timeReduction = 0;
        if (endTime/60==0)
            timeReduction = (int) endTime;
        else if (endTime/(60*60)==0){
            timeReduction = (int) endTime/60;
            timeMeasure = "мин.";
        }
        else {
            timeReduction = (int) endTime/(60*60);
            timeMeasure = "ч.";
        }
        String mvn = "";
        switch (measType){
            case 3:
                mvn = "уд/мин";
                measurementString = String.format("%.0f %s • %d %s назад", value1.asFloat(), mvn, timeReduction, timeMeasure);
                break;
            case 5:
                mvn = "кг";
                measurementString = String.format("%.1f %s • %d %s назад", value1.asFloat(), mvn, timeReduction, timeMeasure);
                break;
            case 2:
                mvn = "мм рт. ст.";
                measurementString = String.format("%.0f/%.0f %s • %d %s назад", value1.asFloat(), value2.asFloat(), mvn, timeReduction, timeMeasure);
                break;
        }

        return measurementString;
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

    private void login(){
        GoogleSignIn.requestPermissions(
                this,
                REQUEST_OAUTH_REQUEST_CODE,
                GoogleSignIn.getLastSignedInAccount(this.getContext()),
                fitnessOptions);
    }

    @Override
    public void onRefresh() {
        downloadData();
    }

}
