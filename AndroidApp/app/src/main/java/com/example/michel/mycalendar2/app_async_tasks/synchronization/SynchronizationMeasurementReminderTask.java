package com.example.michel.mycalendar2.app_async_tasks.synchronization;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.app_async_tasks.UserLocalUpdateTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.models.synchronization.MeasurementReminderReqModule;
import com.example.michel.mycalendar2.models.synchronization.PillReminderReqModule;
import com.example.michel.mycalendar2.utils.DateTypeAdapter;
import com.example.michel.mycalendar2.utils.SynchronizationUtils;
import com.example.michel.mycalendar2.utils.utilModels.DataForDeletion;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SynchronizationMeasurementReminderTask extends AsyncTask<Void, Void, Integer> {
    private Context context;
    private AccountManager accountManager;
    private int typeOfAction;
    private DataForDeletion dataForDeletion;
    private List<Integer> deletionTypes;

    public SynchronizationMeasurementReminderTask(Context context){
        this.context = context;
        accountManager = AccountManager.get(context);
        this.typeOfAction = 2;
        deletionTypes = new ArrayList<>();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int resCode = 1;
        MeasurementReminderReqModule measurementReminderReqModule = new MeasurementReminderReqModule();
        measurementReminderReqModule.setType(typeOfAction);

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        CycleDao cycleDao = new CycleDao(databaseAdapter.open().getDatabase());
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.getDatabase());
        ReminderTimeDao reminderTimeDao = new ReminderTimeDao(databaseAdapter.getDatabase());

        measurementReminderReqModule.setCycleDBList(cycleDao
                .getCycleDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        measurementReminderReqModule.setWeekScheduleDBList(cycleDao
                .getWeekScheduleDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        measurementReminderReqModule.setMeasurementReminderDBList(measurementReminderDao
                .getMeasurementReminderDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        measurementReminderReqModule.setMeasurementReminderEntryDBList(measurementReminderDao
                .getMeasurementReminderEntryDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        measurementReminderReqModule.setReminderTimeDBList(reminderTimeDao
                .getReminderTimeDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));

        databaseAdapter.close();

        if (SynchronizationUtils.containsDeletionMarks(measurementReminderReqModule.getMeasurementReminderEntryDBList()))
            deletionTypes.add(2);
        if (SynchronizationUtils.containsDeletionMarks(measurementReminderReqModule.getReminderTimeDBList()))
            deletionTypes.add(3);
        if (SynchronizationUtils.containsDeletionMarks(measurementReminderReqModule.getWeekScheduleDBList()))
            deletionTypes.add(4);
        if (SynchronizationUtils.containsDeletionMarks(measurementReminderReqModule.getCycleDBList()))
            deletionTypes.add(5);
        if (SynchronizationUtils.containsDeletionMarks(measurementReminderReqModule.getMeasurementReminderDBList()))
            deletionTypes.add(7);

        String response = "";
        String JSONStr = new GsonBuilder().registerTypeAdapter(Date.class,new DateTypeAdapter())
                .create().toJson(measurementReminderReqModule);
        int requestAttempts = 0;

        while (requestAttempts<2){
            try {
                URL url = new URL(context.getResources().getString(R.string.server_address) +
                        "/synchronization/synchronizeMeasurementReminderReqModules");
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

        try {
            JSONObject jsonObject = new JSONObject(response);
            boolean hasDeletion = jsonObject.getBoolean("hasDeletion");
            if (hasDeletion)
                resCode = 2;
        }
        catch (Exception e){
            Log.e("JSONObject", e.getMessage());
        }

        return resCode;
    }

    @Override
    protected void onPostExecute(Integer resCode) {
        if (resCode>0){
            AccountGeneralUtils.curUser.setSynchronizationTime(new Date());
            UserLocalUpdateTask userLocalUpdateTask = new UserLocalUpdateTask(2);
            userLocalUpdateTask.execute(AccountGeneralUtils.curUser);
            if (deletionTypes.size()>0){
                AfterSynchronizationDeletionTask afterSynchronizationDeletionTask = new AfterSynchronizationDeletionTask(
                        deletionTypes, dataForDeletion.getReminderId()
                );
                afterSynchronizationDeletionTask.execute();
            }
        }
    }

    public DataForDeletion getDataForDeletion() {
        return dataForDeletion;
    }

    public void setDataForDeletion(DataForDeletion dataForDeletion) {
        this.dataForDeletion = dataForDeletion;
    }
}
