package com.example.michel.mycalendar2.authentication;

import android.accounts.Account;

import com.example.michel.mycalendar2.models.User;

public class AccountGeneralUtils {

    public static final String JWT_PREFIX = "Bearer ";
    /**
     * Account type id
     */
    public static final String ACCOUNT_TYPE = "com.red_mickey.medical_journal";

    /**
     * Account name
     */
    public static final String ACCOUNT_NAME = "medical_journal_user";

    /**
     * Auth token types
     */

    public static final String AUTHTOKEN_TYPE_USER_ACCESS = "User access";
    public static final String AUTHTOKEN_TYPE_USER_ACCESS_LABEL = "User access to an medical journal user's account";

    public static final ServerAuthenticate sServerAuthenticate = new ParseComServerAuthenticate();

    public static String curToken = "";

    public static Account curAccount = null;

    public static User curUser = null;

    public static String[] parseJWT(String token){
        String[] nameAndGender = new String[2];

        return nameAndGender;
    }
}
