package com.example.michel.mycalendar2.calendarview.adapters;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.michel.mycalendar2.calendarview.utils.DatabaseHelper;

public class DatabaseAdapter {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }


}
