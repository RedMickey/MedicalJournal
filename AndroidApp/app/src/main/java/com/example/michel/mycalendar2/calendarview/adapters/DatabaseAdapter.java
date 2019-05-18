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
import com.example.michel.mycalendar2.models.CycleAndMeasurementComby;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.User;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminder;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderDBEntry;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.models.measurement.MeasurementStatEntry;
import com.example.michel.mycalendar2.models.pill.PillReminder;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.utils.ConvertingUtils;
import com.example.michel.mycalendar2.utils.DBStaticEntries;
import com.example.michel.mycalendar2.utils.utilModels.MeasurementType;

import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

    public void setDatabaseHelper(DatabaseHelper databaseHelper){
        this.dbHelper = databaseHelper;
    }

    public DatabaseAdapter open(){
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public List<String> getTestEntries(){
        List<String> testEntries = new ArrayList<String>();
        Cursor cursor = database.rawQuery("select * from test_table", null);
        if(cursor.moveToFirst()){
            do{
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                testEntries.add(id);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return testEntries;
    }

    /*public void insertTestTable(){
        ContentValues pillReminderEntryTableValues = new ContentValues();
        UUID uuid = UUID.randomUUID();

        byte[] blob = ByteBuffer.allocate(16).putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits()).array();

        pillReminderEntryTableValues.put("_id", blob);


        Long p = database.insert("test_table", null, pillReminderEntryTableValues);

        byte[] selectedBlob = new byte[16];
        Cursor cursor = database.rawQuery("select * from test_table", null);
        if(cursor.moveToFirst()){
            do{
                selectedBlob = cursor.getBlob(cursor.getColumnIndex("_id"));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        ByteBuffer bbr = ByteBuffer.wrap(selectedBlob);
        long high = bbr.getLong();
        long low = bbr.getLong();

        UUID resultUUID = new UUID(high, low);

        UUID uuid2 = UUID.randomUUID();
    }*/

//***********************************get static data************************************************************
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

    public List<MeasurementType> getMeasurementTypes(){
        List<MeasurementType> measurementTypes = new ArrayList<>();

        Cursor cursor = database.rawQuery("select mt._id_measurement_type, mt.type_name, mt._id_measur_value_type, mvt.type_value_name, mt.standard_min_value, " +
                        " mt.standard_max_value " +
                        " from measurement_types mt inner join measurement_value_types mvt on mt._id_measur_value_type=mvt._id_measur_value_type", null);
        if(cursor.moveToFirst()){
            do{
                double[] standardValues = new double[2];
                int id = cursor.getInt(cursor.getColumnIndex("_id_measurement_type"));
                String typeName = cursor.getString(cursor.getColumnIndex("type_name"));
                int idMeasurementValueType = cursor.getInt(cursor.getColumnIndex("_id_measur_value_type"));
                String typeValueName = cursor.getString(cursor.getColumnIndex("type_value_name"));
                standardValues[0] = cursor.getDouble(cursor.getColumnIndex("standard_min_value"));
                standardValues[1] = cursor.getDouble(cursor.getColumnIndex("standard_max_value"));
                measurementTypes.add(new MeasurementType(id, typeName, idMeasurementValueType, typeValueName, standardValues));
            }
            while (cursor.moveToNext());
        }

        /*Cursor cursor = database.rawQuery("select * from measurement_types", null);
        if(cursor.moveToFirst()){
            do{
                double[] standardValues = new double[4];
                int id = cursor.getInt(cursor.getColumnIndex("_id_measurement_type"));
                String typeName = cursor.getString(cursor.getColumnIndex("type_name"));
                int idMeasurementValueType = cursor.getInt(cursor.getColumnIndex("_id_measur_value_type"));
                standardValues[0] = cursor.getDouble(cursor.getColumnIndex("standard_min_value1"));
                standardValues[1] = cursor.getDouble(cursor.getColumnIndex("standard_max_value1"));
                standardValues[2] = cursor.getDouble(cursor.getColumnIndex("standard_min_value2"));
                standardValues[3] = cursor.getDouble(cursor.getColumnIndex("standard_max_value2"));
                measurementTypes.add(new MeasurementType(id, typeName, idMeasurementValueType, standardValues));
            }
            while (cursor.moveToNext());
        }*/
        cursor.close();
        return measurementTypes;
    }
//***********************************end************************************************************

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

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_pill_reminders);
    }

}
