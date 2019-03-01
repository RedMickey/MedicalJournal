package com.example.michel.mycalendar2.app_async_tasks;

import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.UserActivity;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.models.User;
import com.example.michel.mycalendar2.utils.DateTypeAdapter;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;

public class UserGlobalUpdateTask extends AsyncTask<User, Void, User> {
    private Context context;
    private AccountManager accountManager;

    public UserGlobalUpdateTask(Context context){
        this.context = context;
        accountManager = AccountManager.get(context);
    }

    @Override
    protected User doInBackground(User... users) {
        String response = "";
        String JSONStr = new GsonBuilder().registerTypeAdapter(Date.class,new DateTypeAdapter())
                .create().toJson(users[0]);
        int requestAttempts = 0;

        while (requestAttempts<2){
            try {
                URL url = new URL("http://192.168.0.181:8090/user/updateUser");
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
                        users[0].setId(-3);
                        return users[0];
                    }
                }
                else {
                    Log.e("RC", "Exception: " + responseCode);
                    requestAttempts = 2;
                    users[0].setId(-1);
                    return users[0];
                }

            }
            catch(Exception e){
                Log.e("URL", e.getMessage());
                users[0].setId(-2);
                return users[0];
                //return new String("\"Exception\": \"" + e.getMessage()+"\"");
            }
        }

        /*try {
            JSONObject jsonObject = new JSONObject(response);

        }
        catch (Exception e){
            Log.e("JSONObject", e.getMessage());
        }*/

        return users[0];
    }

    @Override
    protected void onPostExecute(User user) {
        if (user.getId()>0){
            AccountGeneralUtils.curUser.setName(user.getName());
            AccountGeneralUtils.curUser.setSurname(user.getSurname());
            AccountGeneralUtils.curUser.setGenderId(user.getGenderId());
            AccountGeneralUtils.curUser.setBirthdayYear(user.getBirthdayYear());
            AccountGeneralUtils.curUser.setEmail(user.getEmail());
            AccountGeneralUtils.curUser.setSynchronizationTime(new Date());

            accountManager.setPassword(AccountGeneralUtils.curAccount, user.getPassword());

            UserLocalUpdateTask userLocalUpdateTask = new UserLocalUpdateTask(0);
            userLocalUpdateTask.execute(user);
        }
        else if (user.getId()==-1){
            Toast.makeText(context, "Данный Email уже используется", Toast.LENGTH_LONG).show();
        }
    }
}
