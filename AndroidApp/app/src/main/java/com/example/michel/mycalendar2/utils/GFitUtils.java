package com.example.michel.mycalendar2.utils;

import android.content.Context;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class GFitUtils {
    public static boolean checkSignedIn(Context context){
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(context);
        return googleSignInAccount!=null?true:false;
    }
}
