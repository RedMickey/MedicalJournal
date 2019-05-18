package com.example.michel.mycalendar2.app_async_tasks.synchronization;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.activities.UserActivity;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.models.User;
import com.example.michel.mycalendar2.utils.DateTypeAdapter;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class DownloadUserDataTask extends AsyncTask<Void, Void, User> {
    private UserActivity userActivity;
    private Context context;
    private AccountManager accountManager;

    public DownloadUserDataTask(UserActivity userActivity){
        super();
        this.userActivity = userActivity;
        this.context = userActivity.getBaseContext();
        accountManager = AccountManager.get(context);
    }

    @Override
    protected User doInBackground(Void... voids) {
        int resCode = 1;

        String response = "";
        Map<String, Integer> queryMap = new HashMap<>();
        queryMap.put("userId", AccountGeneralUtils.curUser.getId());
        String JSONStr = new GsonBuilder().create().toJson(queryMap);
        int requestAttempts = 0;

        while (requestAttempts<2){
            try {
                URL url = new URL(context.getResources().getString(R.string.server_address) +
                        "/user/getUserById");
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

        User newUser = null;
        if (resCode>0){
            newUser = new GsonBuilder().registerTypeAdapter(Date.class,new DateTypeAdapter()).create()
                    .fromJson(response, User.class);
        }

        return newUser;
    }

    @Override
    protected void onPostExecute(User user) {
        if (user!=null){
            userActivity.getUsernameRegEt().setText(user.getName());
            userActivity.getUserSurnameRegEt().setText(user.getSurname());
            userActivity.getGendersSpinner().setSelection(user.getGenderId()-1);
            int birthdayYearPosition = ((ArrayAdapter)  userActivity.getBirthdayYearSpinner().getAdapter())
                    .getPosition(user.getBirthdayYear());
            userActivity.getBirthdayYearSpinner().setSelection(birthdayYearPosition);
        }
        else
            Toast.makeText(context, "Ошибка синхронизации", Toast.LENGTH_SHORT).show();

        userActivity.getSwipeRefreshLayout().setRefreshing(false);
    }
}
