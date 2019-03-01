package com.example.michel.mycalendar2.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.michel.mycalendar2.models.User;
import com.example.michel.mycalendar2.utils.ConvertingUtils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class UserDao {
    private SQLiteDatabase database;

    public UserDao(SQLiteDatabase database){
        this.database = database;
    }

    public int insertUser(User user){
        ContentValues userValues = new ContentValues();
        userValues.put("_id_user", user.getId());
        userValues.put("synchronization_time", ConvertingUtils.convertDateToString(user.getSynchronizationTime()));
        userValues.put("name", user.getName());
        userValues.put("surname", user.getSurname());
        userValues.put("email", user.getEmail());
        userValues.put("_id_gender", user.getGenderId());
        userValues.put("birthday_year", user.getBirthdayYear());
        userValues.put("role_id", user.getRoleId());
        userValues.put("is_current", user.getIsCurrent());
        int userId = (int) database.insert("user", null, userValues);
        return userId;
    }

    public List<User> getAllUsers(){
        List<User> users = new ArrayList<>();
        String rawQuery = "select * FROM user";
        Cursor cursor = database.rawQuery(rawQuery, null);
        if(cursor.moveToFirst()){
            do{
                Calendar calendar = Calendar.getInstance();

                int id = cursor.getInt(cursor.getColumnIndex("_id_user"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String surname = cursor.getString(cursor.getColumnIndex("surname"));
                Integer genderId = cursor.getInt(cursor.getColumnIndex("_id_gender"));
                Integer birthdayYear = cursor.getInt(cursor.getColumnIndex("birthday_year"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String synchronizationTimeStr = cursor.getString(cursor.getColumnIndex("synchronization_time"));
                Integer isCurrent = cursor.getInt(cursor.getColumnIndex("is_current"));

                Date synchronizationTime;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    synchronizationTime = dateFormat.parse(synchronizationTimeStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                    synchronizationTime = new Date();
                }

                User newUser = new User(id, name, surname, genderId, birthdayYear, email,
                        "", synchronizationTime);
                newUser.setIsCurrent(isCurrent);

                users.add(newUser);

            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return users;
    }

    public boolean ifUserExists(String userEmail){
        boolean ifExists = true;

        String rawQuery = "select * FROM user WHERE email = ?";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{userEmail});
        if(cursor.getCount()<=0){
            ifExists = false;
        }
        cursor.close();

        return ifExists;
    }

    public void updateUser(User user, int type){
        ContentValues userValues = new ContentValues();
        switch (type){
            case 0:
                userValues.put("_id_user", user.getId());
                userValues.put("synchronization_time", ConvertingUtils.convertDateToString(user.getSynchronizationTime()));
                userValues.put("name", user.getName());
                userValues.put("surname", user.getSurname());
                userValues.put("email", user.getEmail());
                userValues.put("_id_gender", user.getGenderId());
                userValues.put("birthday_year", user.getBirthdayYear());
                userValues.put("role_id", user.getRoleId());
                userValues.put("is_current", user.getIsCurrent());
                userValues.put("synchronization_time", ConvertingUtils.convertDateToString(
                        user.getSynchronizationTime()));
                break;
            case 1:
                userValues.put("is_current", user.getIsCurrent());
                break;
            case 2:
                userValues.put("synchronization_time", ConvertingUtils.convertDateToString(
                        user.getSynchronizationTime()));
                break;
        }

        long pillReminderId = database.update("user", userValues,
                "_id_user = ?", new String[]{String.valueOf(String.valueOf(user.getId()))});
    }

    public User getCurrentUser(){
        User user = null;
        String rawQuery = "select * FROM user WHERE is_current = 1";
        Cursor cursor = database.rawQuery(rawQuery, null);
        if(cursor.moveToFirst()){
            do{
                Calendar calendar = Calendar.getInstance();

                int id = cursor.getInt(cursor.getColumnIndex("_id_user"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String surname = cursor.getString(cursor.getColumnIndex("surname"));
                Integer genderId = cursor.getInt(cursor.getColumnIndex("_id_gender"));
                Integer birthdayYear = cursor.getInt(cursor.getColumnIndex("birthday_year"));
                String email = cursor.getString(cursor.getColumnIndex("email"));
                String synchronizationTimeStr = cursor.getString(cursor.getColumnIndex("synchronization_time"));
                Integer isCurrent = cursor.getInt(cursor.getColumnIndex("is_current"));

                Date synchronizationTime;
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    synchronizationTime = dateFormat.parse(synchronizationTimeStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                    synchronizationTime = new Date();
                }

                user = new User(id, name, surname, genderId, birthdayYear, email,
                        "", synchronizationTime);
                user.setIsCurrent(isCurrent);

            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return user;
    }
}
