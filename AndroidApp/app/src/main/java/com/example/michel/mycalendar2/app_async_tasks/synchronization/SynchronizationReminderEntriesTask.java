package com.example.michel.mycalendar2.app_async_tasks.synchronization;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.app_async_tasks.UserLocalUpdateTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.models.synchronization.MeasurementReminderEntryDB;
import com.example.michel.mycalendar2.models.synchronization.PillReminderEntryDB;
import com.example.michel.mycalendar2.utils.DateTypeAdapter;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class SynchronizationReminderEntriesTask extends AsyncTask<Void, Void, Integer> {
    private Context context;
    private AccountManager accountManager;
    private int type;
    private UUID id;

    public SynchronizationReminderEntriesTask(Context context, int type){
        this.context = context;
        accountManager = AccountManager.get(context);
        this.type = type;
    }

    public SynchronizationReminderEntriesTask(Context context, UUID id, int type){
        this.context = context;
        accountManager = AccountManager.get(context);
        this.type = type;
        this.id = id;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        List<PillReminderEntryDB> pillReminderEntryDBList = new ArrayList<>();
        List<MeasurementReminderEntryDB> measurementReminderEntryDBList = new ArrayList<>();

        int resCode = 1;
        String controllerURL = "";
        String JSONStr = "";

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(
                databaseAdapter.open().getDatabase());
        PillReminderDao pillReminderDao = new PillReminderDao(databaseAdapter.getDatabase());

        switch (type){
            case 1:
                controllerURL = "synchronizePillReminderEntry";
                PillReminderEntryDB pillReminderEntryDBBuf = pillReminderDao.getPillReminderEntryDBForSynchronizationById(id);
                if (pillReminderEntryDBBuf != null)
                    pillReminderEntryDBList.add(pillReminderEntryDBBuf);

                JSONStr = new GsonBuilder().registerTypeAdapter(Date.class,new DateTypeAdapter())
                        .create().toJson(pillReminderEntryDBBuf);
                break;
            case 2:
                controllerURL = "synchronizeMeasurementReminderEntry";
                MeasurementReminderEntryDB measurementReminderEntryDBBuf = measurementReminderDao.getMeasurementReminderEntryDBForSynchronizationById(id);
                if (measurementReminderEntryDBBuf != null)
                    measurementReminderEntryDBList.add(measurementReminderEntryDBBuf);

                JSONStr = new GsonBuilder().registerTypeAdapter(Date.class,new DateTypeAdapter())
                        .create().toJson(measurementReminderEntryDBBuf);
                break;
            /*default:
                pillReminderEntryDBList = new ArrayList<>();
                measurementReminderEntryDBList = new ArrayList<>();
                break;*/
        }

        databaseAdapter.close();

        String response = "";

        if (pillReminderEntryDBList.size() > 0 || measurementReminderEntryDBList.size() > 0){
            int requestAttempts = 0;

            while (requestAttempts<2){
                try {
                    URL url = new URL(context.getResources().getString(R.string.server_address) +
                            "/synchronization/" + controllerURL);
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
        }
        else {
            resCode = -4;
        }

        return resCode;
    }

    @Override
    protected void onPostExecute(Integer resCode) {
        if (resCode>0){
            AccountGeneralUtils.curUser.setSynchronizationTime(new Date());
            UserLocalUpdateTask userLocalUpdateTask = new UserLocalUpdateTask(2);
            userLocalUpdateTask.execute(AccountGeneralUtils.curUser);
        }
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
