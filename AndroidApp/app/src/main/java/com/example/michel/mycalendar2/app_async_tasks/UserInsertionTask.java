package com.example.michel.mycalendar2.app_async_tasks;

import android.os.AsyncTask;

import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.UserDao;
import com.example.michel.mycalendar2.models.User;

public class UserInsertionTask extends AsyncTask<User, Void, String> {

    @Override
    protected String doInBackground(User... users) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        UserDao userDao = new UserDao(databaseAdapter.open().getDatabase());
        userDao.insertUser(users[0]);

        databaseAdapter.close();

        return users[0].getEmail();
    }

    @Override
    protected void onPostExecute(String email) {
        super.onPostExecute(email);
    }
}
