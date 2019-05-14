package com.example.michel.mycalendar2.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.AddTreatmentActivity;
import com.example.michel.mycalendar2.activities.AlarmActivity;
import com.example.michel.mycalendar2.activities.MainActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.app_async_tasks.synchronization.SynchronizationReminderEntriesTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;
import com.example.michel.mycalendar2.services.AlarmService;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.HealthDataTypes;
import com.google.android.gms.fitness.data.HealthFields;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getDateTimeInstance;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //AlarmActivity alarmActivity = new AlarmActivity();

        //Intent intent2 = new Intent(context,  AlarmActivity.class);

        //context.startActivity(intent2);

        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "");
        wl.acquire();

        //Bundle extras= intent.getExtras();

        /*Intent intAlarm = new Intent(context.getApplicationContext(), AlarmActivity.class);
        intAlarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        intAlarm.putExtra("pre", intent.getParcelableExtra("pre"));
        intAlarm.putExtra("mre", intent.getParcelableExtra("mre"));
        context.startActivity(intAlarm);*/
        /*
        Intent intent2 = new Intent(new Intent(context, AlarmService.class));
        intent2.putExtra("isActual", 1);
        context.startService(intent2);
        */

        PillReminderEntry pre =  intent.getParcelableExtra("pre");
        MeasurementReminderEntry mre = intent.getParcelableExtra("mre");

        String pillReminderEntryID = pre==null?"":pre.getId().toString();
        String measurementReminderEntryID = mre==null?"":mre.getId().toString();

        Intent cancelIntent = new Intent(context, AlarmService.class);
        //notificationIntent2.putExtra("isActual", 1);
        cancelIntent.putExtra("notifId", 0);
        cancelIntent.putExtra("isCancel", 1);
        PendingIntent cancelPendingIntent = PendingIntent.getService(context,
                10000, cancelIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Intent acceptIntent = new Intent(context, AlarmService.class);
        //notificationIntent2.putExtra("isActual", 1);
        acceptIntent.putExtra("notifId", 0);
        acceptIntent.putExtra("isCancel", 0);
        acceptIntent.putExtra("pillReminderEntryID", pillReminderEntryID);
        acceptIntent.putExtra("measurementReminderEntryID", measurementReminderEntryID);
        PendingIntent acceptPendingIntent = PendingIntent.getService(context,
                10001, acceptIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //intent.addFlags()
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, d MMM");

        if (!pillReminderEntryID.equals("")){
            Notification.Builder  builder = new Notification.Builder(context);
            builder.setContentIntent(contentIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(sdf.format(pre.getDate()) + " " + pre.getPillName())
                    .setContentText("Не забудьте принять таблетки!")
                    .addAction(R.drawable.ic_confirm, "Принять", acceptPendingIntent)
                    .addAction(R.drawable.ic_clear, "Пропустить", cancelPendingIntent);
            showNotification(builder, context);
        }
        else {
            if (mre.getIsGfitListening() == 1){
                setupGFitConnection(mre, context, contentIntent,
                        acceptPendingIntent, cancelPendingIntent);
            }
            else{
                createMeasurementNotification(mre, context, contentIntent,
                        acceptPendingIntent, cancelPendingIntent);
            }
        }

        //setResultCode(Activity.RESULT_OK);
        wl.release();
    }

    private void setupGFitConnection(MeasurementReminderEntry mre, Context context, PendingIntent contentIntent,
                             PendingIntent acceptPendingIntent, PendingIntent cancelPendingIntent){
        FitnessOptions fitnessOptions =         fitnessOptions = FitnessOptions.builder()
                .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_HEART_RATE_BPM, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_CALORIES_EXPENDED, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                .addDataType(DataType.TYPE_MOVE_MINUTES, FitnessOptions.ACCESS_READ)
                .addDataType(HealthDataTypes.TYPE_BLOOD_PRESSURE, FitnessOptions.ACCESS_READ)
                .build();
        if (GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(context), fitnessOptions)){
            downloadGFitData(mre, context,  contentIntent,
                    acceptPendingIntent, cancelPendingIntent);
        }
        else {
            Toast.makeText(context, "Ошибка подключения к Google Fit", Toast.LENGTH_SHORT).show();
            createMeasurementNotification(mre, context, contentIntent,
                    acceptPendingIntent, cancelPendingIntent);
        }
    }

    private void downloadGFitData(final MeasurementReminderEntry mre, final Context context, final PendingIntent contentIntent,
                                  final PendingIntent acceptPendingIntent, final PendingIntent cancelPendingIntent){
        Calendar cal = Calendar.getInstance();
        cal.setTime(mre.getDate());
        long endTime = cal.getTimeInMillis();
        int timeAmount = 10;

        DataType dataType = null;
        DataType dataAggregate = null;
        switch (mre.getIdMeasurementType()){
            case 8:
                dataType = DataType.TYPE_STEP_COUNT_DELTA;
                dataAggregate = DataType.AGGREGATE_STEP_COUNT_DELTA;
                timeAmount = 60;
                break;
            case 6:
                dataType = DataType.TYPE_CALORIES_EXPENDED;
                dataAggregate = DataType.AGGREGATE_CALORIES_EXPENDED;
                timeAmount = 60;
                break;
            case 3:
                dataType = DataType.TYPE_HEART_RATE_BPM;
                dataAggregate = DataType.AGGREGATE_HEART_RATE_SUMMARY;
                break;
            case 5:
                dataType = DataType.TYPE_WEIGHT;
                dataAggregate = DataType.AGGREGATE_WEIGHT_SUMMARY;
                break;
            case 2:
                dataType = HealthDataTypes.TYPE_BLOOD_PRESSURE;
                dataAggregate = HealthDataTypes.AGGREGATE_BLOOD_PRESSURE_SUMMARY;
                break;
        }

        cal.add(Calendar.MINUTE, -timeAmount);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = getDateTimeInstance();
        Log.i("TAG", "Range Start: " + dateFormat.format(startTime));
        Log.i("TAG", "Range End: " + dateFormat.format(endTime));

        Fitness.getHistoryClient(context, GoogleSignIn.getLastSignedInAccount(context))
                .readData(queryFitnessData(startTime, endTime, timeAmount, dataType, dataAggregate))
                .addOnSuccessListener(
                        new OnSuccessListener<DataReadResponse>() {
                            @Override
                            public void onSuccess(DataReadResponse dataReadResponse) {
                                operateDownloadedData(dataReadResponse, mre, context, contentIntent,
                                        acceptPendingIntent, cancelPendingIntent);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("ERR", "There was a problem reading the data.", e);
                                Toast.makeText(context,"Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
                                createMeasurementNotification(mre, context, contentIntent,
                                        acceptPendingIntent, cancelPendingIntent);
                            }
                        });
    }

    private DataReadRequest queryFitnessData(long startTime, long endTime, int timeAmount,
                                             DataType dataType, DataType dataAggregate){
        DataReadRequest readRequest = null;

        readRequest = new DataReadRequest.Builder()
                //.read(dataType)
                .aggregate(dataType, dataAggregate)
                .bucketByTime(timeAmount, TimeUnit.MINUTES)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        return readRequest;
    }

    private void operateDownloadedData(DataReadResponse dataReadResult, MeasurementReminderEntry mre, Context context,
                                       PendingIntent contentIntent, PendingIntent acceptPendingIntent, PendingIntent cancelPendingIntent){
        boolean empty = false;
        if (dataReadResult.getBuckets().get(0).getDataSets().size() > 0){
            if (dataReadResult.getBuckets().get(0).getDataSets().get(0).getDataPoints().size()>0){
                DataPoint dp = dataReadResult.getBuckets().get(0).getDataSets().get(0).getDataPoints().get(0);
                double value1 = 0, value2 = -10000;
                String notifMessage = "";
                switch (mre.getIdMeasurementType()){
                    case 8:
                        value1 = dp.getValue(Field.FIELD_STEPS).asInt();
                        notifMessage = String.format("%s: %.0f %s %n Запись успешно добавлена!", mre.getMeasurementTypeName(), value1,
                                ConvertingUtils.smartEnding((int)value1, new String[]{"", "а", "ов"}, mre.getMeasurementValueTypeName()));
                        break;
                    case 6:
                        value1 = (int)dp.getValue(Field.FIELD_CALORIES).asFloat();
                        notifMessage = String.format("%s: %.1f %s %n Запись успешно добавлена!", mre.getMeasurementTypeName(), value1,
                                mre.getMeasurementValueTypeName());
                        break;
                    case 3:
                        value1 = (int)dp.getValue(Field.FIELD_AVERAGE).asFloat();
                        notifMessage = String.format("%s: %.0f %s %n Запись успешно добавлена!", mre.getMeasurementTypeName(), value1,
                                mre.getMeasurementValueTypeName());
                        break;
                    case 5:
                        value1 = dp.getValue(Field.FIELD_AVERAGE).asFloat();
                        notifMessage = String.format("%s: %.1f %s %n Запись успешно добавлена!", mre.getMeasurementTypeName(), value1,
                                mre.getMeasurementValueTypeName());
                        break;
                    case 2:
                        value1 = (int)dp.getValue(HealthFields.FIELD_BLOOD_PRESSURE_SYSTOLIC_AVERAGE).asFloat();
                        value2 = (int)dp.getValue(HealthFields.FIELD_BLOOD_PRESSURE_DIASTOLIC_AVERAGE).asFloat();
                        notifMessage = String.format("%s: %.0f - %.0f %s %n Запись успешно добавлена!", mre.getMeasurementTypeName(), value1,
                                value2, mre.getMeasurementValueTypeName());
                        break;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.open().getDatabase());
                measurementReminderDao.updateIsDoneMeasurementReminderEntry(1, mre.getId(), sdf.format(mre.getDate())+":00",
                        value1, value2, 0);
                databaseAdapter.close();
                if (AccountGeneralUtils.curUser.getId()!=1) {
                    SynchronizationReminderEntriesTask synchronizationReminderEntriesTask = new SynchronizationReminderEntriesTask(
                            context, mre.getId(), 2
                    );
                    synchronizationReminderEntriesTask.execute();
                }
                SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm, d MMM");
                Notification.Builder  builder = new Notification.Builder(context);
                builder.setContentIntent(contentIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(sdf2.format(mre.getDate()) + " " + mre.getMeasurementTypeName())
                        .addAction(R.drawable.ic_confirm, "OK", cancelPendingIntent);

                Notification nBuilder = new Notification.BigTextStyle(builder)
                        .bigText(notifMessage).build();
                nBuilder.flags|= Notification.FLAG_AUTO_CANCEL;
                nBuilder.defaults = Notification.DEFAULT_SOUND |
                        Notification.DEFAULT_VIBRATE;
                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, nBuilder);
                //Toast.makeText(context,"Запись добавлена", Toast.LENGTH_SHORT).show();
            }
            else
                empty = true;
        }
        else
            empty = true;
        if (empty){
            Toast.makeText(context,"Нет данных Google Fit", Toast.LENGTH_SHORT).show();
            createMeasurementNotification(mre, context, contentIntent,
                    acceptPendingIntent, cancelPendingIntent);
        }

        printData(dataReadResult);
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

    private void createMeasurementNotification(MeasurementReminderEntry mre, Context context, PendingIntent contentIntent,
                                               PendingIntent acceptPendingIntent, PendingIntent cancelPendingIntent){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, d MMM");
        Notification.Builder  builder = new Notification.Builder(context);
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(sdf.format(mre.getDate()) + " " + mre.getMeasurementTypeName())
                .setContentText("Не забудьте произвести измерение!")
                .addAction(R.drawable.ic_confirm, "Принять", acceptPendingIntent)
                .addAction(R.drawable.ic_clear, "Пропустить", cancelPendingIntent);
        showNotification(builder, context);
    }

    private void showNotification(Notification.Builder  builder, Context context){
        Notification nBuilder = builder.build();

        nBuilder.flags|= Notification.FLAG_AUTO_CANCEL;

        nBuilder.defaults = Notification.DEFAULT_SOUND |
                Notification.DEFAULT_VIBRATE;

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, nBuilder);
    }
}
