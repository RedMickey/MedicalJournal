package com.example.michel.mycalendar2.services.authentication;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ParseComIServerAuthenticate implements IServerAuthenticate {
    @Override
    public String userSignUp(String name, String email, String pass, String authType) throws Exception {
        return null;
    }

    @Override
    public String userSignIn(String email, String pass) throws Exception {
        String authtoken = null;

        try {
            URL url = new URL("http://185.178.46.244:8090/login");
            //URL url = new URL("http://10.0.2.2:8090/login");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            JSONObject cred = new JSONObject();
            cred.put("username", email);
            cred.put("password", pass);

            OutputStream os = conn.getOutputStream();
            os.write(cred.toString().getBytes("UTF-8"));
            os.close();

            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                authtoken = conn.getHeaderField("Authorization").replace(AccountGeneralUtils.JWT_PREFIX, "");
            }
            else {
                Log.e("RC", "Exception: " + responseCode);
                //return authtoken =  String.valueOf(responseCode);
            }

        }
        catch(Exception e){
            Log.e("URL", e.getMessage() + " ParseComIServerAuthenticate");
            //return new String("\"Exception\": \"" + e.getMessage()+"\"");
        }

        return authtoken;
    }

    private void updatePassword(){

    }
}
