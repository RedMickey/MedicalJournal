package com.example.michel.mycalendar2.app_async_tasks.synchronization;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.app_async_tasks.UserLocalUpdateTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.models.synchronization.ReminderSynchronizationReqModule;
import com.example.michel.mycalendar2.utils.DateTypeAdapter;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import lombok.Data;

public class GettingDataFromServerTask extends AsyncTask<Void, Void, Integer> {
    private Context context;
    private AccountManager accountManager;
    private Date synchronizationTimestamp;

    @Data
    class SynchronizationReq {
        private final int userId;
        private final Date synchronizationTimestamp;
    }

    public GettingDataFromServerTask(Context context){
        this.context = context;
        accountManager = AccountManager.get(context);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int resCode = 1;

        //Calendar cal = Calendar.getInstance();
        //cal.set(2019, 2, 16, 18, 00);

        SynchronizationReq synchronizationReq = new SynchronizationReq(
                AccountGeneralUtils.curUser.getId(),
                //cal.getTime()
                AccountGeneralUtils.curUser.getSynchronizationTime()
        );

        String response = "";
        String JSONStr = new GsonBuilder().registerTypeAdapter(Date.class,new DateTypeAdapter())
                .create().toJson(synchronizationReq);
        int requestAttempts = 0;

        while (requestAttempts<2){
            try {
                URL url = new URL(context.getResources().getString(R.string.server_address) +
                        "/synchronization/getAllDataForSynchronization");
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
                //return new String("\"Exception\": \"" + e.getMessage()+"\"");
            }
        }

        if (resCode>0){
            ReminderSynchronizationReqModule[] reminderSynchronizationReqModule = new GsonBuilder()
                    .registerTypeAdapter(Date.class,new DateTypeAdapter()).create()
                    .fromJson(response, ReminderSynchronizationReqModule[].class);

            synchronizationTimestamp = reminderSynchronizationReqModule[0].getSynchronizationTimestamp();

            DatabaseAdapter dbAdapter = new DatabaseAdapter();
            ReminderTimeDao reminderTimeDao = new ReminderTimeDao(dbAdapter.open().getDatabase());
            PillReminderDao pillReminderDao = new PillReminderDao(dbAdapter.getDatabase());;
            MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(dbAdapter.getDatabase());;
            CycleDao cycleDao = new CycleDao(dbAdapter.getDatabase());

            cycleDao.insertOrReplaceWeekSchedulesAfterSynchronization(
                    reminderSynchronizationReqModule[0].getWeekScheduleDBList());
            cycleDao.insertOrReplaceCyclesAfterSynchronization(
                    reminderSynchronizationReqModule[0].getCycleDBList());
            pillReminderDao.insertOrReplacePillsAfterSynchronization(
                    reminderSynchronizationReqModule[0].getPillDBList());
            pillReminderDao.insertOrReplacePillRemindersAfterSynchronization(
                    reminderSynchronizationReqModule[0].getPillReminderDBList());
            measurementReminderDao.insertOrReplaceMeasurementRemindersAfterSynchronization(
                    reminderSynchronizationReqModule[0].getMeasurementReminderDBList());
            reminderTimeDao.insertOrReplaceReminderTimeAfterSynchronization(
                    reminderSynchronizationReqModule[0].getReminderTimeDBList());
            pillReminderDao.insertOrReplacePillReminderEntriesAfterSynchronization(
                    reminderSynchronizationReqModule[0].getPillReminderEntryDBList());
            measurementReminderDao.insertOrReplaceMeasurementReminderEntriesAfterSynchronization(
                    reminderSynchronizationReqModule[0].getMeasurementReminderEntryDBList());

            dbAdapter.close();
        }

        return resCode;
    }

    @Override
    protected void onPostExecute(Integer resCode) {
        if (resCode>0){
            AccountGeneralUtils.curUser.setSynchronizationTime(synchronizationTimestamp);
            UserLocalUpdateTask userLocalUpdateTask = new UserLocalUpdateTask(2);
            userLocalUpdateTask.execute(AccountGeneralUtils.curUser);
            Toast.makeText(context, "Синхронизовано", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, "Ошибка синхронизации", Toast.LENGTH_SHORT).show();
        }

    }
}
