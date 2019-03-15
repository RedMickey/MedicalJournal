package com.example.michel.mycalendar2.app_async_tasks.synchronization;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.michel.mycalendar2.app_async_tasks.UserLocalUpdateTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.models.synchronization.BeforeDeletionReqModule;
import com.example.michel.mycalendar2.models.synchronization.MeasurementReminderReqModule;
import com.example.michel.mycalendar2.utils.DateTypeAdapter;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class BeforeDeletionSynchronizationTask extends AsyncTask<Void, Void, Integer> {
    private Context context;
    private AccountManager accountManager;
    private int typeOfReminder;
    private UUID reminderId;

    public BeforeDeletionSynchronizationTask(Context context, int typeOfReminder){
        this.context = context;
        accountManager = AccountManager.get(context);
        this.typeOfReminder = typeOfReminder;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int resCode = 1;
        BeforeDeletionReqModule beforeDeletionReqModule = new BeforeDeletionReqModule();
        beforeDeletionReqModule.setType(typeOfReminder);

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        CycleDao cycleDao = new CycleDao(databaseAdapter.open().getDatabase());
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.getDatabase());
        PillReminderDao pillReminderDao = new PillReminderDao(databaseAdapter.getDatabase());
        ReminderTimeDao reminderTimeDao = new ReminderTimeDao(databaseAdapter.getDatabase());

        beforeDeletionReqModule.setWeekScheduleIds(cycleDao.getMarkedForDeletionWeekScheduleIds());
        beforeDeletionReqModule.setCycleIds(cycleDao.getMarkedForDeletionCycleIds());
        beforeDeletionReqModule.setReminderTimeIds(reminderTimeDao.getMarkedForDeletionReminderTimeIds());
        if (typeOfReminder==1){
            beforeDeletionReqModule.setReminderIds(pillReminderDao.getMarkedForDeletionPillReminderIds());
            beforeDeletionReqModule.setReminderEntriesIds(pillReminderDao.getMarkedForDeletionPillReminderEntryIds());
        }
        else{
            beforeDeletionReqModule.setReminderIds(measurementReminderDao.getMarkedForDeletionMeasurementReminderIds());
            beforeDeletionReqModule.setReminderEntriesIds(measurementReminderDao.getMarkedForDeletionMeasurementReminderEntryIds());
        }

        databaseAdapter.close();

        String response = "";

        String JSONStr = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create().toJson(beforeDeletionReqModule);
        int requestAttempts = 0;

        while (requestAttempts<2){
            try {
                URL url = new URL("http://192.168.0.181:8090/synchronization/synchronizeDeletion");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestMethod("POST");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.addRequestProperty("Authorization", AccountGeneralUtils.JWT_PREFIX + AccountGeneralUtils.curToken);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(JSONStr.getBytes("UTF-8"));
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    response = sb.toString();
                    requestAttempts = 2;
                }
                else if(responseCode == HttpsURLConnection.HTTP_UNAUTHORIZED) {
                    Log.e("RC", "Exception: " + responseCode);
                    requestAttempts++;
                    AccountGeneralUtils.updateTokenSync(accountManager);
                    if (requestAttempts==2){
                        resCode = -3;
                        //return users[0];
                    }
                }
                else {
                    Log.e("RC", "Exception: " + responseCode);
                    requestAttempts = 2;
                    resCode = -1;
                    //return users[0];
                }

            }
            catch(Exception e){

                resCode = -2;
                //Log.e("URL", e.getMessage());
                Log.e("URL", String.valueOf(-2));
                //return users[0];
                //return new String("\"Exception\": \"" + e.getMessage()+"\"");
            }
        }

        return resCode;
    }

    @Override
    protected void onPostExecute(Integer resCode) {
        if (resCode>0){
            AfterSynchronizationDeletionTask afterSynchronizationDeletionTask = new AfterSynchronizationDeletionTask(
                    typeOfReminder==1?3:4
            );
            afterSynchronizationDeletionTask.setReminderId(reminderId);
            afterSynchronizationDeletionTask.execute();
        }
    }

    public UUID getReminderId() {
        return reminderId;
    }

    public void setReminderId(UUID reminderId) {
        this.reminderId = reminderId;
    }
}
