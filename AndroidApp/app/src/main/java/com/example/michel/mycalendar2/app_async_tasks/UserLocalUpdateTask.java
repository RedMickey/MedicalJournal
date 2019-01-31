package com.example.michel.mycalendar2.app_async_tasks;

import android.os.AsyncTask;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.models.User;

public class UserLocalUpdateTask extends AsyncTask<User, Void, Void> {
    private int type;

    public UserLocalUpdateTask(int type){
        this.type = type;
    }

    @Override
    protected Void doInBackground(User... users) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        databaseAdapter.updateUser(users[0], type);

        databaseAdapter.close();

        return null;
    }
}
