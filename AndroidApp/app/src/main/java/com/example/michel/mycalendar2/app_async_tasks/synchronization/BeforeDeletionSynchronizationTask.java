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
import com.example.michel.mycalendar2.models.synchronization.BeforeDeletionReqModule;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class BeforeDeletionSynchronizationTask extends AsyncTask<Void, Void, Integer> {
    private Context context;
    private AccountManager accountManager;
    private int typeOfReminder;
    private UUID reminderId;
    private List<Integer> deletionTypes;
    private Date synchronizationTimestamp;

    public BeforeDeletionSynchronizationTask(Context context, int typeOfReminder){
        this.context = context;
        accountManager = AccountManager.get(context);
        this.typeOfReminder = typeOfReminder;
        deletionTypes = new ArrayList<>();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int resCode = 1;
        BeforeDeletionReqModule beforeDeletionReqModule = new BeforeDeletionReqModule();
        beforeDeletionReqModule.setType(typeOfReminder);
        beforeDeletionReqModule.setUserId(AccountGeneralUtils.curUser.getId());

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

            if (beforeDeletionReqModule.getReminderEntriesIds().size()>0)
                deletionTypes.add(1);
            if (beforeDeletionReqModule.getReminderTimeIds().size()>0)
                deletionTypes.add(3);
            if (beforeDeletionReqModule.getWeekScheduleIds().size()>0)
                deletionTypes.add(4);
            if (beforeDeletionReqModule.getCycleIds().size()>0)
                deletionTypes.add(5);
            if (beforeDeletionReqModule.getReminderIds().size()>0)
                deletionTypes.add(6);
        }
        else{
            beforeDeletionReqModule.setReminderIds(measurementReminderDao.getMarkedForDeletionMeasurementReminderIds());
            beforeDeletionReqModule.setReminderEntriesIds(measurementReminderDao.getMarkedForDeletionMeasurementReminderEntryIds());

            if (beforeDeletionReqModule.getReminderEntriesIds().size()>0)
                deletionTypes.add(2);
            if (beforeDeletionReqModule.getReminderTimeIds().size()>0)
                deletionTypes.add(3);
            if (beforeDeletionReqModule.getWeekScheduleIds().size()>0)
                deletionTypes.add(4);
            if (beforeDeletionReqModule.getCycleIds().size()>0)
                deletionTypes.add(5);
            if (beforeDeletionReqModule.getReminderIds().size()>0)
                deletionTypes.add(7);
        }

        databaseAdapter.close();

        String response = "";

        String JSONStr = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter())
                .create().toJson(beforeDeletionReqModule);
        int requestAttempts = 0;

        while (requestAttempts<2){
            try {
                URL url = new URL(  context.getResources().getString(R.string.server_address) +
                        "/synchronization/synchronizeDeletion");
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

        try {
            JSONObject jsonObject = new JSONObject(response);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            try {
                synchronizationTimestamp = dateFormat.parse(
                        jsonObject.getString("synchronizationTimestamp")
                );
            } catch (ParseException e) {
                e.printStackTrace();
                synchronizationTimestamp = new Date();
            }
        }
        catch (Exception e){
            Log.e("JSONObject", e.getMessage());
        }

        return resCode;
    }

    @Override
    protected void onPostExecute(Integer resCode) {
        if (resCode>0){
            AccountGeneralUtils.curUser.setSynchronizationTime(synchronizationTimestamp);
            UserLocalUpdateTask userLocalUpdateTask = new UserLocalUpdateTask(2);
            userLocalUpdateTask.execute(AccountGeneralUtils.curUser);

            AfterSynchronizationDeletionTask afterSynchronizationDeletionTask = new AfterSynchronizationDeletionTask(
                    deletionTypes
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
