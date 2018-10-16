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

import java.util.ArrayList;
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

    public void insertPillReminder(String pillName, int pillCount, int idPillCountType,
                                   String startDate, int idCycle, @Nullable int idHavingMealsType,
                                   @Nullable String havingMealsTime, String annotation, int isActive){

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
        database.insert("pill_reminders", null, pillReminderTableValues);

        //Log.i("pill_new_id", String.valueOf(pillId));
    }

    public void insertCycle(int period, int periodDMType, @Nullable int once_aPeriod,
                            @Nullable int once_aPeriodDMType, int idCyclingType,
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
        database.insert("cycles", null, cycleTableValues);
    }
/*
    public List<TakingMedicine> getTakingMedicineByDate(DateData date){
        ArrayList<TakingMedicine> takingMedicines = new ArrayList<>();
        Cursor cursor = database.query("pills", new String[]{"_id_pill", "pill_name", "time_of_drug_usage"}, "time_of_drug_usage = ?", new String[]{date.getDateString()}, null,null,null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id_pill"));
                String name = cursor.getString(cursor.getColumnIndex("pill_name"));
                String dateStr = cursor.getString(cursor.getColumnIndex("time_of_drug_usage"));
                takingMedicines.add(new TakingMedicine(id, name));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  takingMedicines;
    }

    public List<TakingMedicine> getTakingMedicine(){
        ArrayList<TakingMedicine> takingMedicines = new ArrayList<>();
        Cursor cursor = getAllEntries();
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id_pill"));
                String name = cursor.getString(cursor.getColumnIndex("pill_name"));
                String dateStr = cursor.getString(cursor.getColumnIndex("time_of_drug_usage"));
                takingMedicines.add(new TakingMedicine(id, name));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  takingMedicines;
    }
*/

    public List<PillReminderEntry> getPillReminderEntriesByDate(DateData date){
        ArrayList<PillReminderEntry> pillReminderEntries = new ArrayList<>();
        String rawQuery = "select pre._id_pill_reminder, pre.is_done, rt.time, pi.pill_name from pill_reminder_entries pre inner join pill_reminders pr on pre._id_pill_reminder=pr._id_pill_reminder inner join" +
                " reminder_times rt on pr._id_pill_reminder=rt._id_pill_reminder inner join pills pi on pi._id_pill=pr._id_pill where pre.reminder_date=?";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{date.getDateString()});
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(cursor.getColumnIndex("_id_pill_reminder"));
                String name = cursor.getString(cursor.getColumnIndex("pill_name"));
                String dateStr = cursor.getString(cursor.getColumnIndex("time"));
                pillReminderEntries.add(new PillReminderEntry(id, name, dateStr));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  pillReminderEntries;
    }


    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_pill_reminders);
    }

  /*  public User getUser(long id){
        User user = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE, DatabaseHelper.COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_NAME));
            int year = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_YEAR));
            user = new User(id, name, year);
        }
        cursor.close();
        return  user;
    }*/

}
