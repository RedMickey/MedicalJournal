package com.example.michel.mycalendar2.app_async_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.MainActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.UserDao;
import com.example.michel.mycalendar2.models.User;
import com.example.michel.mycalendar2.utils.TimestampTypeAdapter;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

import javax.net.ssl.HttpsURLConnection;

public class PostSignInTask extends AsyncTask<String, Void, Integer> {
    private Context context;
    private MainActivity mainActivity = null;
    private User user = null;

    public PostSignInTask(Context context){
        this.context = context;
    }

    public PostSignInTask(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.context = mainActivity.getBaseContext();
    }

    @Override
    protected Integer doInBackground(String... strings) {

        String response = "";

        try {
            URL url = new URL("http://192.168.0.181:8090/user/getUserByEmail");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.addRequestProperty("Authorization", AccountGeneralUtils.JWT_PREFIX + strings[0]);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userEmail", strings[1]);

            OutputStream os = conn.getOutputStream();
            os.write(jsonObject.toString().getBytes("UTF-8"));
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

            }
            else {
                Log.e("RC", "Exception: " + responseCode);
                return -2;
                //return new String("\"Exception\": \"" + responseCode+"\"");
            }

        }
        catch(Exception e){
            Log.e("URL", e.getMessage());
            return -1;
            //return new String("\"Exception\": \"" + e.getMessage()+"\"");
        }

        try {
            user = new GsonBuilder().registerTypeAdapter(Timestamp.class,new TimestampTypeAdapter())
                    .create().fromJson(response, User.class);
            user.setIsCurrent(1);
        }
        catch (Exception e){
            Log.e("JSONObject", e.getMessage());
            return -3;
        }

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        UserDao userDao = new UserDao(databaseAdapter.open().getDatabase());
        if (!userDao.ifUserExists(user.getEmail())){
            userDao.insertUser(user);
        }
        else {
            userDao.updateUser(user, 0);
        }
        databaseAdapter.close();

        SetUpCurrentUserTask setUpCurrentUserTask = new SetUpCurrentUserTask(context);
        setUpCurrentUserTask.setAuthToken(strings[0]);
        setUpCurrentUserTask.setUser(user);
        setUpCurrentUserTask.execute();

        return 1;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (mainActivity!=null){
            ((TextView) mainActivity.getNavigationView().findViewById(R.id.username_tv)).setText(user.getName());
            ((TextView) mainActivity.getNavigationView().findViewById(R.id.profile_config_tv)).setText("Редактировать профиль");
        }
    }
}
