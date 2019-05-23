package com.example.michel.mycalendar2.app_async_tasks.synchronization;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.MainActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.app_async_tasks.MeasurementNotificationsCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.PillNotificationsCreationTask;
import com.example.michel.mycalendar2.app_async_tasks.UserLocalUpdateTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.models.synchronization.ReminderSynchronizationReqModule;
import com.example.michel.mycalendar2.utils.DateTypeAdapter;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

import lombok.Data;

public class GettingDataFromServerTask extends AsyncTask<Void, Void, Integer> {
    private Context context;
    private AccountManager accountManager;
    private Date synchronizationTimestamp;
    private MainActivity mainActivity;

    @Data
    class SynchronizationReq {
        private final int userId;
        private final Date synchronizationTimestamp;
    }

    public GettingDataFromServerTask(Context context){
        this.context = context;
        accountManager = AccountManager.get(context);
    }

    public GettingDataFromServerTask(MainActivity mainActivity){
        this.context = mainActivity.getBaseContext();
        this.mainActivity = mainActivity;
        accountManager = AccountManager.get(context);
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        int resCode = 1;
        DatabaseAdapter dbAdapter = new DatabaseAdapter();

        ReminderTimeDao reminderTimeDao = new ReminderTimeDao(dbAdapter.open().getDatabase());
        PillReminderDao pillReminderDao = new PillReminderDao(dbAdapter.getDatabase());
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(dbAdapter.getDatabase());
        CycleDao cycleDao = new CycleDao(dbAdapter.getDatabase());

        /*SynchronizationReq synchronizationReq = new SynchronizationReq(
                AccountGeneralUtils.curUser.getId(),
                //cal.getTime()
                AccountGeneralUtils.curUser.getSynchronizationTime()
        );*/

        ReminderSynchronizationReqModule startReminderSynchronizationReqModule = new ReminderSynchronizationReqModule();
        startReminderSynchronizationReqModule.setUserId(AccountGeneralUtils.curUser.getId());
        startReminderSynchronizationReqModule.setSynchronizationTimestamp(AccountGeneralUtils.curUser.getSynchronizationTime());

        startReminderSynchronizationReqModule.setCycleDBList(cycleDao
                .getCycleDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        startReminderSynchronizationReqModule.setWeekScheduleDBList(cycleDao
                .getWeekScheduleDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        startReminderSynchronizationReqModule.setMeasurementReminderDBList(measurementReminderDao
                .getMeasurementReminderDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        startReminderSynchronizationReqModule.setMeasurementReminderEntryDBList(measurementReminderDao
                .getMeasurementReminderEntryDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        startReminderSynchronizationReqModule.setPillDBList(pillReminderDao
                .getPillDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        startReminderSynchronizationReqModule.setPillReminderDBList(pillReminderDao
                .getPillReminderDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        startReminderSynchronizationReqModule.setPillReminderEntryDBList(pillReminderDao
                .getPillReminderEntryDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));
        startReminderSynchronizationReqModule.setReminderTimeDBList(reminderTimeDao
                .getReminderTimeDBEntriesForSynchronization(AccountGeneralUtils.curUser.getSynchronizationTime()));

        String response = "";
        /*String JSONStr = new GsonBuilder().registerTypeAdapter(Date.class,new DateTypeAdapter())
                .create().toJson(synchronizationReq);*/
        String JSONStr = new GsonBuilder().registerTypeAdapter(Date.class,new DateTypeAdapter())
                .create().toJson(startReminderSynchronizationReqModule);
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
                requestAttempts = 2;
                Log.e("URL", e.toString() + " " + this.getClass());
            }
        }

        if (resCode>0){
            ReminderSynchronizationReqModule[] reminderSynchronizationReqModule = new GsonBuilder()
                    .registerTypeAdapter(Date.class,new DateTypeAdapter()).create()
                    .fromJson(response, ReminderSynchronizationReqModule[].class);

            synchronizationTimestamp = reminderSynchronizationReqModule[0].getSynchronizationTimestamp();

            pillReminderDao.deletePillReminderEntries(reminderSynchronizationReqModule[1].getPillReminderEntryDBList());
            measurementReminderDao.deleteMeasurementReminderEntries(
                    reminderSynchronizationReqModule[1].getMeasurementReminderEntryDBList());
            reminderTimeDao.deleteReminderTimeEntries(reminderSynchronizationReqModule[1].getReminderTimeDBList());
            cycleDao.deleteWeekSchedules(reminderSynchronizationReqModule[1].getWeekScheduleDBList());
            cycleDao.deleteCycles(reminderSynchronizationReqModule[1].getCycleDBList());
            pillReminderDao.deletePillReminders(reminderSynchronizationReqModule[1].getPillReminderDBList());
            measurementReminderDao.deleteMeasurementReminders(
                    reminderSynchronizationReqModule[1].getMeasurementReminderDBList());

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


            if (reminderSynchronizationReqModule[0].getMeasurementReminderEntryDBList().size()>0){
                MeasurementNotificationsCreationTask mnct = new MeasurementNotificationsCreationTask();
                mnct.execute(context);
            }
            if (reminderSynchronizationReqModule[0].getPillReminderEntryDBList().size()>0){
                PillNotificationsCreationTask pnct = new PillNotificationsCreationTask();
                pnct.execute(context);
            }
        }

        dbAdapter.close();

        return resCode;
    }

    @Override
    protected void onPostExecute(Integer resCode) {
        if (resCode>0){
            Timestamp d = new Timestamp(synchronizationTimestamp.getTime());
            AccountGeneralUtils.curUser.setSynchronizationTime(synchronizationTimestamp);
            UserLocalUpdateTask userLocalUpdateTask = new UserLocalUpdateTask(2);
            userLocalUpdateTask.execute(AccountGeneralUtils.curUser);
            Toast.makeText(context, "Синхронизовано", Toast.LENGTH_SHORT).show();
            if (mainActivity != null){
                mainActivity.updateCurFragment(0);
            }
        }
        else {
            Toast.makeText(context, "Ошибка синхронизации", Toast.LENGTH_SHORT).show();
        }

    }
}
