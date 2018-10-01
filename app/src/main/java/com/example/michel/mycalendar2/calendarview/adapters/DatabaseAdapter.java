package com.example.michel.mycalendar2.calendarview.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.DatabaseHelper;
import com.example.michel.mycalendar2.models.TakingMedicine;

import java.util.ArrayList;
import java.util.List;

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

    private Cursor getAllEntries(){
        String[] columns = new String[] {"_id_pill", "pill_name", "time_of_drug_usage"};
        return  database.query("pills", columns, null, null, null, null, null);
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

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_PILLS);
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
