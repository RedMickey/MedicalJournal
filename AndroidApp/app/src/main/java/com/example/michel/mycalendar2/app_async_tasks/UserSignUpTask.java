package com.example.michel.mycalendar2.app_async_tasks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.activities.RegistrationActivity;
import com.example.michel.mycalendar2.models.User;
import com.example.michel.mycalendar2.utils.DateTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;

import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;

public class UserSignUpTask extends AsyncTask<User, Void, User> {
    private AlertDialog progressDialog;
    private RegistrationActivity registrationActivity;

    public UserSignUpTask(RegistrationActivity registrationActivity){
        this.registrationActivity = registrationActivity;
        AlertDialog.Builder builder = new AlertDialog.Builder(this.registrationActivity);
        LayoutInflater inflater = LayoutInflater.from(this.registrationActivity);
        View dialogView = inflater.inflate(R.layout.progress_dialog_layout, null, false);
        builder.setView(dialogView)
                .setCancelable(false);
        progressDialog = builder.create();
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected User doInBackground(User... users) {
        /*try {
            // Simulate network access.
            Thread.sleep(3000);
        } catch (InterruptedException e) {

        }*/

        String response = "";

        try {
            URL url = new URL("http://192.168.0.181:8090/user/sign-up");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            String JSONStr = new GsonBuilder().registerTypeAdapter(Date.class,new DateTypeAdapter())
                    .create().toJson(users[0]);

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

            }
            else {
                Log.e("RC", "Exception: " + responseCode);
                users[0].setId(-2);
                return users[0];
                //return new String("\"Exception\": \"" + responseCode+"\"");
            }

        }
        catch(Exception e){
            Log.e("URL", e.getMessage());
            users[0].setId(-1);
            return users[0];
            //return new String("\"Exception\": \"" + e.getMessage()+"\"");
        }

        try {
            JSONObject jsonObject = new JSONObject(response);
            users[0].setId(jsonObject.getInt("id"));
        }
        catch (Exception e){
            Log.e("JSONObject", e.getMessage());
            users[0].setId(-3);
            return users[0];
        }

        return users[0];
    }

    @Override
    protected void onPostExecute(User updatedUser) {
        progressDialog.dismiss();
        if (updatedUser.getId()>0){
            Toast.makeText(registrationActivity.getApplicationContext(), "Регистрация успешно завершена", Toast.LENGTH_LONG).show();
            UserInsertionTask uit = new UserInsertionTask();
            uit.execute(updatedUser);
            Intent data = new Intent();
            data.putExtra("user_email",updatedUser.getEmail());
            registrationActivity.setResult(RESULT_OK, data);
            registrationActivity.finish();
        }
        else {
            Toast.makeText(registrationActivity, "Ошибка", Toast.LENGTH_LONG).show();
        }
    }
}
