package com.example.michel.mycalendar2.activities;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michel.mycalendar2.app_async_tasks.PostSignInTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.models.User;

import java.util.ArrayList;
import java.util.List;

import static com.example.michel.mycalendar2.authentication.AccountGeneralUtils.sServerAuthenticate;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    public final static String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
    public final static String ARG_AUTH_TYPE = "AUTH_TYPE";
    public final static String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
    public final static String ARG_IS_ADDING_NEW_ACCOUNT = "IS_ADDING_ACCOUNT";

    public static final String KEY_ERROR_MESSAGE = "ERR_MSG";

    public final static String PARAM_USER_PASS = "USER_PASS";

    private final String TAG = this.getClass().getSimpleName();

    private int requestSignUpCode = 5533;

    private AccountAuthenticatorResponse accountAuthenticatorResponse = null;
    private Bundle resultBundle = null;
    private AccountManager mAccountManager;
    private String mAuthTokenType;

    //private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LoginActivity loginActivity;

    public final void setAccountAuthenticatorResult(Bundle result) {
        resultBundle = result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email_actv);

        mPasswordView = (EditText) findViewById(R.id.password_et);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        //mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        mAccountManager = AccountManager.get(getBaseContext());

        mAuthTokenType = getIntent().getStringExtra(ARG_AUTH_TYPE);
        if (mAuthTokenType == null)
            mAuthTokenType = AccountGeneralUtils.AUTHTOKEN_TYPE_USER_ACCESS;

        accountAuthenticatorResponse =
                getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);

        if (accountAuthenticatorResponse != null) {
            accountAuthenticatorResponse.onRequestContinued();
        }

        loginActivity = this;

        setUpEmailViewAdapter();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (password.equals("") || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            Account[] accounts = mAccountManager.getAccountsByType("com.red_mickey.medical_journal");

            /*for (int index = 0; index < accounts.length; index++) {
                mAccountManager.removeAccount(accounts[index], null, null);
            }*/

            /*final AccountManagerFuture<Bundle> future = mAccountManager.getAuthToken(accounts[0],  mAuthTokenType, null, this, null, null);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Bundle bnd = future.getResult();

                        final String authtoken = bnd.getString(AccountManager.KEY_AUTHTOKEN);

                        Log.d("MedicalJournal", "GetToken Bundle is " + bnd);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();*/


            submit(email, password);
            /*mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
            i++;*/
        }

        /*showProgress(true);
        mAuthTask = new UserLoginTask("", "");
        mAuthTask.execute((Void) null);*/

    }

    public void submit(final String email, final String password){
        final String accountType = getIntent().getStringExtra(ARG_ACCOUNT_TYPE)!=null?getIntent().getStringExtra(ARG_ACCOUNT_TYPE)
                : AccountGeneralUtils.ACCOUNT_TYPE;

        new AsyncTask<Void, Void, Intent>() {
            @Override
            protected Intent doInBackground(Void... params) {
                String authtoken = null;
                Bundle data = new Bundle();
                try {
                    authtoken = sServerAuthenticate.userSignIn(email, password);

                    data.putString(AccountManager.KEY_ACCOUNT_NAME, email);
                    data.putString(AccountManager.KEY_ACCOUNT_TYPE, accountType);
                    data.putString(AccountManager.KEY_AUTHTOKEN, authtoken);
                    data.putString(PARAM_USER_PASS, password);
                    if (authtoken == null)
                        data.putString(KEY_ERROR_MESSAGE, "Authentication failed");
                } catch (Exception e) {
                    data.putString(KEY_ERROR_MESSAGE, e.getMessage());
                }
                final Intent res = new Intent();
                res.putExtras(data);
                return res;
            }
            @Override
            protected void onPostExecute(Intent intent) {
                if (intent.hasExtra(KEY_ERROR_MESSAGE)) {
                    Toast.makeText(getBaseContext(), intent.getStringExtra(KEY_ERROR_MESSAGE), Toast.LENGTH_SHORT).show();
                    showProgress(false);
                } else {
                    finishLogin(intent);
                }
            }
        }.execute();
    }

    private void finishLogin(Intent intent) {
        Log.d("MedicalJournal", TAG + "> finishLogin");

        String accountName = intent.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = intent.getStringExtra(PARAM_USER_PASS);
        final Account account = new Account(accountName, intent.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE));

        if (getIntent().getBooleanExtra(ARG_IS_ADDING_NEW_ACCOUNT, true)) {
            Log.d("MedicalJournal", TAG + "> finishLogin > addAccountExplicitly");
            String authtoken = intent.getStringExtra(AccountManager.KEY_AUTHTOKEN);
            String authtokenType = mAuthTokenType;

            // Creating the account on the device and setting the auth token we got
            // (Not setting the auth token will cause another call to the server to authenticate the user)
            mAccountManager.addAccountExplicitly(account, accountPassword, null);
            mAccountManager.setAuthToken(account, authtokenType, authtoken);

        } else {
            Log.d("MedicalJournal", TAG + "> finishLogin > setPassword");
            mAccountManager.setPassword(account, accountPassword);
        }

        setAccountAuthenticatorResult(intent.getExtras());
        setResult(RESULT_OK, intent);

        finish();
    }

    public void finish() {
        if (accountAuthenticatorResponse != null) {
            // send the result bundle back if set, otherwise send an error.
            if (resultBundle != null) {
                accountAuthenticatorResponse.onResult(resultBundle);
            } else {
                accountAuthenticatorResponse.onError(AccountManager.ERROR_CODE_CANCELED,
                        "canceled");
            }
            accountAuthenticatorResponse = null;
        }
        //Account[] accounts = mAccountManager.getAccountsByType("com.red_mickey.medical_journal");
        super.finish();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void onSignUpButtonClick(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        //startActivity(intent);
        startActivityForResult(intent, requestSignUpCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == requestSignUpCode) {
            if (resultCode == RESULT_OK) {
                String userEmail = data.getStringExtra("user_email");
                setUpEmailViewAdapter();
                mEmailView.setText(userEmail);
            }
        }
    }

    private void setUpEmailViewAdapter(){
        new AsyncTask<Void, Void, List<String>>() {
            @Override
            protected List<String> doInBackground(Void... voids) {
                DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                databaseAdapter.open();
                List<User> users = databaseAdapter.getAllUsers();

                databaseAdapter.close();

                List<String> userEmails = new ArrayList<>();
                for (User user : users) {
                    if (!user.getEmail().equals(getResources().getString(R.string.def_user_email)))
                        userEmails.add(user.getEmail());
                }

                return userEmails;
            }

            @Override
            protected void onPostExecute(List<String> userEmails) {
                if (userEmails.size()>0)
                {
                    ArrayAdapter<String> emailViewAdapter = new ArrayAdapter<>(
                            loginActivity, android.R.layout.simple_dropdown_item_1line, userEmails
                    );
                    mEmailView.setAdapter(emailViewAdapter);
                }
            }
        }.execute();
    }

    /*public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected String doInBackground(Void... params) {

            String response = "";

            try {
                URL url = new URL("http://192.168.0.181:8090/user/test1");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestMethod("POST");
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setDoInput(true);
                conn.setDoOutput(true);

                JSONObject cred = new JSONObject();
                cred.put("s","test request "+ i.toString());
                //cred.put("password", "pwd");

                OutputStream os = conn.getOutputStream();
                os.write(cred.toString().getBytes("UTF-8"));
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
                    return new String("false : " + responseCode);
                }

            }
            catch(Exception e){
                Log.e("URL", e.getMessage());
                return new String("Exception: " + e.getMessage());
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            mAuthTask = null;
            showProgress(false);

            try {
                JSONObject jsonObject = new JSONObject(response);
                mEmailView.setText(jsonObject.getString("response"));
            }
            catch (Exception e){
                Log.e("JSONObject", e.getMessage());
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }*/
}

