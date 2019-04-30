package com.example.michel.mycalendar2.app_async_tasks;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.MainActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.app_async_tasks.synchronization.GettingDataFromServerTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.UserDao;
import com.example.michel.mycalendar2.models.User;

public class SetUpCurrentUserTask extends AsyncTask<Void, Void, Integer> {
    private Context context;
    private AccountManager accountManager;
    private Account account = null;
    private String authToken = null;
    private User user = null;
    private MainActivity mainActivity = null;
    private boolean notificationsCreationWorkable = false;

    public SetUpCurrentUserTask(){

    }

    public SetUpCurrentUserTask(MainActivity mainActivity){
        this.mainActivity = mainActivity;
        this.context = mainActivity.getBaseContext();
    }

    public SetUpCurrentUserTask(Context context){
        this.context =context;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        /*databaseAdapter.open();


        databaseAdapter.close();*/

        if (user == null){
            UserDao userDao = new UserDao(databaseAdapter.open().getDatabase());
            user = userDao.getCurrentUser();
            databaseAdapter.close();
            if (user == null){
                Log.e("UERR", "There is not the current user!");
                return -1;
            }
        }

        if (account == null){
            accountManager = AccountManager.get(context);
            Account[] accounts = accountManager.getAccountsByType(AccountGeneralUtils.ACCOUNT_TYPE);
            for(Account bufAccount : accounts){
                if (bufAccount.name.equals(user.getEmail())){
                    account = bufAccount;
                    break;
                }
            }
        }

        if (authToken == null){
            if (accountManager == null)
                accountManager = AccountManager.get(context);
            try {
                authToken = accountManager.blockingGetAuthToken(account, AccountGeneralUtils.AUTHTOKEN_TYPE_USER_ACCESS, true);
            }catch (Exception e){
                Log.e("GetAuthToken", e.getMessage());
                return -2;
            }
        }

        AccountGeneralUtils.curUser = user;
        AccountGeneralUtils.curAccount = account;
        AccountGeneralUtils.curToken = authToken;

        return 1;
    }

    @Override
    protected void onPostExecute(Integer status) {
        if (status > 0){
            Toast.makeText(context, user.getEmail() + " " + account.name + "\n" + authToken, Toast.LENGTH_LONG).show();
            if (mainActivity!=null){
                ((TextView) mainActivity.getNavigationView().findViewById(R.id.username_tv)).setText(user.getName());
                ((TextView) mainActivity.getNavigationView().findViewById(R.id.profile_config_tv)).setText("Редактировать профиль");
                GettingDataFromServerTask gettingDataFromServerTask = new GettingDataFromServerTask(
                        mainActivity
                );
                gettingDataFromServerTask.execute();
            }
        }
        else{
            AccountGeneralUtils.curUser = new User();
            Toast.makeText(context, "An error has occurred", Toast.LENGTH_LONG).show();
        }
        if (notificationsCreationWorkable){
            PillNotificationsCreationTask pnct = new PillNotificationsCreationTask();
            pnct.execute(mainActivity.getApplicationContext());

            MeasurementNotificationsCreationTask mnct = new MeasurementNotificationsCreationTask();
            mnct.execute(mainActivity.getApplicationContext());
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setNotificationsCreationWorkable(boolean notificationsCreationWorkable) {
        this.notificationsCreationWorkable = notificationsCreationWorkable;
    }
}
