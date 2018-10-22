package com.example.michel.mycalendar2.calendarview.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.DatabaseHelper;
import com.example.michel.mycalendar2.models.PillReminderEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseAdapter {
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    public static Context AppContext;

    public DatabaseAdapter(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
    }

    public DatabaseAdapter()
    {
        try{
            dbHelper = new DatabaseHelper(this.AppContext);
        }
        catch (NullPointerException ex){
            Log.e("EXCEPTION", ex.getMessage());
        }
    }

    public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }
/*
    private Cursor getAllEntries(){
        String[] columns = new String[] {"_id_pill", "pill_name", "time_of_drug_usage"};
        return  database.query("pills", columns, null, null, null, null, null);
    }
*/

    public Map<String, Integer> getDoseTypes(){
        Map<String, Integer> doseTypes = new HashMap<String, Integer>();
        Cursor cursor = database.rawQuery("select * from pill_count_types", null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id_pill_count_type"));
                String typeName = cursor.getString(cursor.getColumnIndex("type_name"));
                doseTypes.put(typeName, id);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return doseTypes;
    }

    public Map<String, Integer> getDateTypes(){
        Map<String, Integer> dateTypes = new HashMap<String, Integer>();
        Cursor cursor = database.rawQuery("select * from date_measurement_types", null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id_date_measurement_types"));
                String typeName = cursor.getString(cursor.getColumnIndex("type_name"));
                dateTypes.put(typeName, id);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return dateTypes;
    }

    public Map<String, Integer> getCycleTypes(){
        Map<String, Integer> cycleTypes = new HashMap<String, Integer>();
        Cursor cursor = database.rawQuery("select * from cycling_types", null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id_cycling_type"));
                String typeName = cursor.getString(cursor.getColumnIndex("cycling_type_name"));
                cycleTypes.put(typeName, id);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return cycleTypes;
    }

    public void getAllTables(){
        Cursor c = database.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        Log.i("table", "here1");
        if (c.moveToFirst()) {
            while ( !c.isAfterLast() ) {
                Log.i("table", c.getString(0));
                c.moveToNext();
            }
        }
        Log.i("table", "here2");
    }
/*
    public void inserReminderTimes(String time, @Nullable Integer idPillReminder,
                                   @Nullable Integer idMeasurementReminder){
        ContentValues reminderTimeTableValues = new ContentValues();
        reminderTimeTableValues.put("time", time);
        reminderTimeTableValues.put("_id_pill_reminder", idPillReminder);
        reminderTimeTableValues.put("_id_measurement_reminder", idMeasurementReminder);
        database.insert("reminder_times", null, reminderTimeTableValues);
    }
*/
    public void insertPillReminderEntries(String reminder_date, Integer idPillReminder, String reminderTime){
        ContentValues pillReminderEntryTableValues = new ContentValues();
        pillReminderEntryTableValues.put("is_done", 0);
        pillReminderEntryTableValues.put("reminder_date", reminder_date);
        pillReminderEntryTableValues.put("_id_pill_reminder", idPillReminder);
        pillReminderEntryTableValues.put("reminder_time", reminderTime);
        database.insert("pill_reminder_entries", null, pillReminderEntryTableValues);
    }

    public int insertPillReminder(String pillName, Integer pillCount, Integer idPillCountType,
                                   String startDate, Integer idCycle, @Nullable Integer idHavingMealsType,
                                   @Nullable Integer havingMealsTime, String annotation, Integer isActive){

        ContentValues pillTableValues = new ContentValues();
        pillTableValues.put("pill_name", pillName);
        pillTableValues.put("pill_description", "");
        long pillId = database.insert("pills", null, pillTableValues);

        ContentValues pillReminderTableValues = new ContentValues();
        pillReminderTableValues.put("_id_pill", pillId);
        pillReminderTableValues.put("pill_count", pillCount);
        pillReminderTableValues.put("_id_pill_count_type", idPillCountType);
        pillReminderTableValues.put("start_date", startDate);
        pillReminderTableValues.put("_id_cycle", idCycle);
        pillReminderTableValues.put("_id_having_meals_type", idHavingMealsType);
        pillReminderTableValues.put("having_meals_time", havingMealsTime);
        pillReminderTableValues.put("annotation", annotation);
        pillReminderTableValues.put("IsActive", isActive);
        long pillReminderId = database.insert("pill_reminders", null, pillReminderTableValues);

        //Log.i("pill_new_id", String.valueOf(pillId));
        return (int)pillReminderId;
    }

    public int insertCycle(Integer period, Integer periodDMType, @Nullable Integer once_aPeriod,
                            @Nullable Integer once_aPeriodDMType, Integer idCyclingType,
                            @Nullable int[] weekSchedule){
        long weekScheduleID = -1;
        if (weekSchedule!=null)
        {
            ContentValues weekScheduleTableValues = new ContentValues();
            weekScheduleTableValues.put("mon", weekSchedule[0]);
            weekScheduleTableValues.put("tue", weekSchedule[1]);
            weekScheduleTableValues.put("wed", weekSchedule[2]);
            weekScheduleTableValues.put("thu", weekSchedule[3]);
            weekScheduleTableValues.put("fri", weekSchedule[4]);
            weekScheduleTableValues.put("sat", weekSchedule[5]);
            weekScheduleTableValues.put("sun", weekSchedule[6]);
            weekScheduleID = database.insert("week_schedules", null, weekScheduleTableValues);
        }

        ContentValues cycleTableValues = new ContentValues();
        cycleTableValues.put("period", period);
        cycleTableValues.put("period_DM_type", periodDMType);
        cycleTableValues.put("once_a_period", once_aPeriod);
        cycleTableValues.put("once_a_period_DM_type", once_aPeriodDMType);
        cycleTableValues.put("_id_week_schedule", weekScheduleID==-1?null:weekScheduleID);
        cycleTableValues.put("_id_cycling_type", idCyclingType);
        long cycleId = database.insert("cycles", null, cycleTableValues);

        return (int)cycleId;
    }

    /*public PillReminderEntry(int id, String pillName, int pillCount, int pillCountType,
                             Date date, int havingMealsType, Date havingMealsTime, int isDone)*/

    public List<PillReminderEntry> getPillReminderEntriesByDate(DateData date){
        ArrayList<PillReminderEntry> pillReminderEntries = new ArrayList<>();
        String rawQuery = "select pre._id_pill_reminder, pre.is_done, pr._id_having_meals_type, pre.reminder_time, pr.having_meals_time, pr.pill_count, pr._id_pill_count_type, pre.reminder_date, pi.pill_name" +
                " from pill_reminder_entries pre inner join pill_reminders pr on pre._id_pill_reminder=pr._id_pill_reminder inner join pills pi on pi._id_pill=pr._id_pill where pre.reminder_date=?";
        //String rawQuery = "select * from pill_reminder_entries pre inner join pill_reminders pr on pre._id_pill_reminder=pr._id_pill_reminder where reminder_date=?";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{date.getDateString()});
        String g = date.getDateString();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id_pill_reminder"));
                String pillName = cursor.getString(cursor.getColumnIndex("pill_name"));
                int pillCount = cursor.getInt(cursor.getColumnIndex("pill_count"));
                int pillCountType = cursor.getInt(cursor.getColumnIndex("_id_pill_count_type"));
                String dateStr = cursor.getString(cursor.getColumnIndex("reminder_date"));
                String timeStr = cursor.getString(cursor.getColumnIndex("reminder_time"));
                int havingMealsType = cursor.getInt(cursor.getColumnIndex("_id_having_meals_type"));
                int havingMealsTimeStr = cursor.getInt(cursor.getColumnIndex("having_meals_time"));
                int isDone = cursor.getInt(cursor.getColumnIndex("is_done"));

                Date reminderDate;
                Date havingMealsTime = new Date();
                try {
                    reminderDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr+" "+timeStr);
                    havingMealsTime.setTime(reminderDate.getTime()+havingMealsTimeStr*60*1000);
                } catch (ParseException e) {
                    e.printStackTrace();
                    reminderDate = new Date();
                }
                Date f = reminderDate;
                pillReminderEntries.add(new PillReminderEntry(
                        id, pillName, pillCount, pillCountType, reminderDate, havingMealsType, havingMealsTime, isDone));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  pillReminderEntries;
    }


    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_pill_reminders);
    }

}
