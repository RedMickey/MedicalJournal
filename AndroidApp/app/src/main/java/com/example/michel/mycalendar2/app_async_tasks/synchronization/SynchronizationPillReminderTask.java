package com.example.michel.mycalendar2.app_async_tasks.synchronization;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.michel.mycalendar2.app_async_tasks.UserLocalUpdateTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.models.synchronization.PillReminderReqModule;
import com.example.michel.mycalendar2.models.synchronization.WeekScheduleDB;
import com.example.michel.mycalendar2.utils.DateTypeAdapter;
import com.example.michel.mycalendar2.utils.utilModels.DataForDeletion;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SynchronizationPillReminderTask extends AsyncTask<Void, Void, Integer> {
    private Context context;
    private AccountManager accountManager;
    private int typeOfAction;
    private DataForDeletion dataForDeletion;

    public SynchronizationPillReminderTask(Context context, int typeOfAction){
        this.context = context;
        accountManager = AccountManager.get(context);
        this.typeOfAction = typeOfAction;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int resCode = 1;
        PillReminderReqModule pillReminderReqModule = new PillReminderReqModule();
        pillReminderReqModule.setType(typeOfAction);

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        CycleDao cycleDao = new CycleDao(databaseAdapter.open().getDatabase());
        PillReminderDao pillReminderDao = new PillReminderDao(databaseAdapter.getDatabase());
        ReminderTimeDao reminderTimeDao = new ReminderTimeDao(databaseAdapter.getDatabase());

        pillReminderReqModule.setCycleDBList(cycleDao
                .getCycleDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        pillReminderReqModule.setWeekScheduleDBList(cycleDao
                .getWeekScheduleDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        pillReminderReqModule.setPillDBList(pillReminderDao
                .getPillDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        pillReminderReqModule.setPillReminderDBList(pillReminderDao
                .getPillReminderDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        pillReminderReqModule.setPillReminderEntryDBList(pillReminderDao
                .getPillReminderEntryDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        pillReminderReqModule.setReminderTimeDBList(reminderTimeDao
                .getReminderTimeDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));

        databaseAdapter.close();


        String response = "";
        String JSONStr = new GsonBuilder().registerTypeAdapter(Date.class,new DateTypeAdapter())
                .create().toJson(pillReminderReqModule);
        int requestAttempts = 0;

        while (requestAttempts<2){
            try {
                URL url = new URL("http://192.168.0.181:8090/synchronization/synchronizePillReminderModules");
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
            if (typeOfAction == 2){
                AfterSynchronizationDeletionTask afterSynchronizationDeletionTask = new AfterSynchronizationDeletionTask(
                        1,
                        dataForDeletion.getReminderId(),
                        dataForDeletion.getCurDateStr()
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
