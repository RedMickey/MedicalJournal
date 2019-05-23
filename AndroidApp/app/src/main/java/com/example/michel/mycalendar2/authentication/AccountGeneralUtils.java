package com.example.michel.mycalendar2.authentication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;

import com.example.michel.mycalendar2.models.User;

import java.io.IOException;

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

    public static final IServerAuthenticate S_I_SERVER_AUTHENTICATE = new ParseComIServerAuthenticate();

    public static String curToken = "";

    public static Account curAccount = null;

    public static User curUser = null;

    public static void updateTokenSync(AccountManager accountManager) throws AuthenticatorException,
            OperationCanceledException, IOException {
        accountManager.invalidateAuthToken(AccountGeneralUtils.ACCOUNT_TYPE, AccountGeneralUtils.curToken);

        /*accountManager.getAuthToken(curAccount, AccountGeneralUtils.AUTHTOKEN_TYPE_USER_ACCESS, null, false,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        try {
                            Bundle bundle = future.getResult();
                            curToken = future.getResult().getString(KEY_AUTHTOKEN);
                            int i = 5;
                        }
                        catch (Exception e){
                            Log.e("AEX", e.toString());
                        }

                        int i = 5;
                    }
                }, null);*/
        curToken = accountManager.blockingGetAuthToken(curAccount, AUTHTOKEN_TYPE_USER_ACCESS, true);
    }
}
