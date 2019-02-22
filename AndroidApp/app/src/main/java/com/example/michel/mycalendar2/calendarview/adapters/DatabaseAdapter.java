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
/*
    public void ttt(int idPillReminder){
        Cursor cursor = database.rawQuery("select * from cycles where _id_cycle = ?", new String[]{String.valueOf(idPillReminder)});
        if(cursor.moveToFirst()){
            do {

                int id = cursor.getInt(cursor.getColumnIndex("period"));
                int id2 = cursor.getInt(cursor.getColumnIndex("period"));
            }
            while (cursor.moveToNext());
        }

        cursor.close();
    }
*/

    /*public void deleteReminderTimeByReminderId(UUID idReminder, int type){*/

        /*String idPillReminderStr = String.valueOf(idPillReminder);

        database.delete("reminder_time",
                "_id_reminder_time in (" +
                        " select rt._id_reminder_time from reminder_time rt inner join pill_reminders pr on rt._id_pill_reminder=pr._id_pill_reminder" +
                        " where rt._id_pill_reminder = ? and pr.start_date=? and" +
                        " (select count(*) from pill_reminder_entries pre2 where pre2._id_pill_reminder=? and pre2.is_done=1 and pre2._id_reminder_time=rt._id_reminder_time)=0)",
                new String[]{idPillReminderStr, date, idPillReminderStr}
                );*/
        /*
        if (type==0)
            database.delete("reminder_time", "lower(hex(_id_pill_reminder)) = ?",
                    new String[]{idReminder.toString().replace("-", "")});
        else
            database.delete("reminder_time", "lower(hex(_id_measurement_reminder)) = ?",
                    new String[]{idReminder.toString().replace("-", "")});

    }

    public int getCountOfPillReminderEntries(UUID idPillReminder){
        long count = DatabaseUtils.queryNumEntries(database, "pill_reminder_entries", "lower(hex(_id_pill_reminder)) = ?",
                new String[]{idPillReminder.toString().replace("-", "")});

        return (int)count;
    }*/

    //***********************************work with User************************************************************

    /*public int insertUser(User user){
        ContentValues userValues = new ContentValues();
        userValues.put("_id_user", user.getId());
        userValues.put("synchronization_time", user.getSynchronizationTime().getTime()/1000);
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
                Integer synchronizationTime = cursor.getInt(cursor.getColumnIndex("synchronization_time"))*1000;
                Integer isCurrent = cursor.getInt(cursor.getColumnIndex("is_current"));

                User newUser = new User(id, name, surname, genderId, birthdayYear, email,
                        "", new Timestamp(synchronizationTime));
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
        if (type == 0){
            userValues.put("_id_user", user.getId());
            userValues.put("synchronization_time", user.getSynchronizationTime().getTime()/1000);
            userValues.put("name", user.getName());
            userValues.put("surname", user.getSurname());
            userValues.put("email", user.getEmail());
            userValues.put("_id_gender", user.getGenderId());
            userValues.put("birthday_year", user.getBirthdayYear());
            userValues.put("role_id", user.getRoleId());
            userValues.put("is_current", user.getIsCurrent());
        }
        if (type == 1)
            userValues.put("is_current", user.getIsCurrent());

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
                Integer synchronizationTime = cursor.getInt(cursor.getColumnIndex("synchronization_time"))*1000;
                Integer isCurrent = cursor.getInt(cursor.getColumnIndex("is_current"));

                user = new User(id, name, surname, genderId, birthdayYear, email,
                        "", new Timestamp(synchronizationTime));
                user.setIsCurrent(isCurrent);

            }
            while (cursor.moveToNext());
        }

        cursor.close();

        return user;
    }
    */
    //***********************************end*****************************************************************************

    //***********************************work with PillReminder************************************************************
    /*
    public void insertPillReminderEntry(String reminder_date, UUID idPillReminder, String reminderTime, Integer isOneTime){
        ContentValues pillReminderEntryTableValues = new ContentValues();
        if (isOneTime == 1)
            pillReminderEntryTableValues.put("is_done", 1);
        else
            pillReminderEntryTableValues.put("is_done", 0);
        pillReminderEntryTableValues.put("reminder_date", reminder_date);
        pillReminderEntryTableValues.put("_id_pill_reminder", ConvertingUtils.convertUUIDToBytes(idPillReminder));
        pillReminderEntryTableValues.put("reminder_time", reminderTime);
        pillReminderEntryTableValues.put("_id_pill_reminder_entry", ConvertingUtils.convertUUIDToBytes(UUID.randomUUID()));
        database.insert("pill_reminder_entries", null, pillReminderEntryTableValues);
    }

    public UUID insertReminderTime(String reminderTime, UUID reminderId, Integer reminderType){
        UUID reminderTimeId = UUID.randomUUID();
        ContentValues reminderTimeValues = new ContentValues();
        reminderTimeValues.put("reminder_time", reminderTime);
        reminderTimeValues.put("_id_reminder_time", ConvertingUtils.convertUUIDToBytes(reminderTimeId));
        switch (reminderType){
            case 0:
                reminderTimeValues.put("_id_pill_reminder", ConvertingUtils.convertUUIDToBytes(reminderId));
                break;
            case 1:
                reminderTimeValues.put("_id_measurement_reminder", ConvertingUtils.convertUUIDToBytes(reminderId));
                break;
        }
        database.insert("reminder_time", null, reminderTimeValues);

        return reminderTimeId;
    }

    public UUID insertPillReminder(String pillName, Integer pillCount, Integer idPillCountType,
                                   String startDate, @Nullable UUID idCycle, @Nullable Integer idHavingMealsType,
                                   @Nullable Integer havingMealsTime, String annotation, Integer isActive, Integer times_aDay,
                                   Integer isOneTime){

        UUID pillId = UUID.randomUUID();
        ContentValues pillTableValues = new ContentValues();
        pillTableValues.put("pill_name", pillName);
        pillTableValues.put("_id_pill", ConvertingUtils.convertUUIDToBytes(pillId));
        pillTableValues.put("pill_description", "");
        database.insert("pills", null, pillTableValues);

        UUID pillReminderId = UUID.randomUUID();
        ContentValues pillReminderTableValues = new ContentValues();
        pillReminderTableValues.put("_id_pill_reminder", ConvertingUtils.convertUUIDToBytes(pillReminderId));
        pillReminderTableValues.put("_id_pill", ConvertingUtils.convertUUIDToBytes(pillId));
        pillReminderTableValues.put("pill_count", pillCount);
        pillReminderTableValues.put("_id_pill_count_type", idPillCountType);
        pillReminderTableValues.put("start_date", startDate);
        pillReminderTableValues.put("_id_cycle", idCycle==null?null:ConvertingUtils.convertUUIDToBytes(idCycle));
        pillReminderTableValues.put("_id_having_meals_type", idHavingMealsType);
        pillReminderTableValues.put("having_meals_time", havingMealsTime);
        pillReminderTableValues.put("annotation", annotation);
        pillReminderTableValues.put("IsActive", isActive);
        pillReminderTableValues.put("times_a_day", times_aDay);
        if (isOneTime==1)
            pillReminderTableValues.put("is_one_time", isOneTime);
        database.insert("pill_reminders", null, pillReminderTableValues);

        //Log.i("pill_new_id", String.valueOf(pillId));
        return pillReminderId;
    }

    public UUID updatePillReminder(UUID idPillReminder, String pillName, Integer pillCount, Integer idPillCountType,
                                  String startDate, UUID idCycle, @Nullable Integer idHavingMealsType,
                                  @Nullable Integer havingMealsTime, String annotation, Integer isActive, Integer times_aDay,
                                  boolean needUpdatePill){
        UUID pillId = null;
        if (needUpdatePill){
            pillId = UUID.randomUUID();
            ContentValues pillTableValues = new ContentValues();
            pillTableValues.put("pill_name", pillName);
            pillTableValues.put("_id_pill", ConvertingUtils.convertUUIDToBytes(pillId));
            pillTableValues.put("pill_description", "");
            database.insert("pills", null, pillTableValues);
        }

        ContentValues pillReminderTableValues = new ContentValues();
        if (needUpdatePill)
            pillReminderTableValues.put("_id_pill", ConvertingUtils.convertUUIDToBytes(pillId));
        pillReminderTableValues.put("pill_count", pillCount);
        pillReminderTableValues.put("_id_pill_count_type", idPillCountType);
        pillReminderTableValues.put("start_date", startDate);
        pillReminderTableValues.put("_id_cycle", ConvertingUtils.convertUUIDToBytes(idCycle));
        pillReminderTableValues.put("_id_having_meals_type", idHavingMealsType);
        pillReminderTableValues.put("having_meals_time", havingMealsTime);
        pillReminderTableValues.put("annotation", annotation);
        pillReminderTableValues.put("IsActive", isActive);
        pillReminderTableValues.put("times_a_day", times_aDay);
        database.update("pill_reminders", pillReminderTableValues, "lower(hex(_id_pill_reminder)) = ?",
                new String[]{idPillReminder.toString().replace("-", "")});

        //Log.i("pill_new_id", String.valueOf(pillId));
        return idPillReminder;
    }

    public List<PillReminder> getAllPillReminders(){
        List<PillReminder> pillReminders = new ArrayList<>();
        String rawQuery = "select pr._id_pill_reminder, pr._id_having_meals_type, pr.pill_count, pct.type_name, pi.pill_name, pr.start_date, pr.IsActive, cl.period, pr.times_a_day, "+
                "cl.period_DM_type,(select COUNT(*) from pill_reminder_entries pre where pre._id_pill_reminder=pr._id_pill_reminder and pre.is_done=0 ) as count_left "+
                "from pill_reminders pr inner join pills pi on pi._id_pill=pr._id_pill inner join pill_count_types pct on pr._id_pill_count_type=pct._id_pill_count_type "+
                "inner join cycles cl on pr._id_cycle=cl._id_cycle ORDER BY pr.IsActive DESC, pi.pill_name";
        Cursor cursor = database.rawQuery(rawQuery, null);
        if(cursor.moveToFirst()){
            do{
                Calendar calendar = Calendar.getInstance();

                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_pill_reminder")));
                String pillName = cursor.getString(cursor.getColumnIndex("pill_name"));
                int pillCount = cursor.getInt(cursor.getColumnIndex("pill_count"));
                String pillCountType = cursor.getString(cursor.getColumnIndex("type_name"));
                String startDateStr = cursor.getString(cursor.getColumnIndex("start_date"));
                int havingMealsType = cursor.getInt(cursor.getColumnIndex("_id_having_meals_type"));
                int period = cursor.getInt(cursor.getColumnIndex("period"));
                int isActive = cursor.getInt(cursor.getColumnIndex("IsActive"));
                int countLeft = cursor.getInt(cursor.getColumnIndex("count_left"));
                int times_aDay = cursor.getInt(cursor.getColumnIndex("times_a_day"));
                int periodDM_Type = cursor.getInt(cursor.getColumnIndex("period_DM_type"));

                Date startDate;
                SimpleDateFormat dateFormatOld = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    startDate = dateFormatOld.parse(startDateStr);
                    startDateStr = dateFormatNew.format(startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    startDate = new Date();
                }
                String endDateStr = "0";
                calendar.setTime(startDate);
                switch (periodDM_Type){
                    case 1:
                        calendar.add(Calendar.DATE, period);
                        break;
                    case 2:
                        calendar.add(Calendar.DATE, period*7);
                        break;
                    case 3:
                        calendar.add(Calendar.DATE, period*30);
                        break;
                }
                endDateStr = dateFormatNew.format(calendar.getTime());

                pillReminders.add(new PillReminder(id, pillName, pillCount, pillCountType,
                        havingMealsType, isActive, times_aDay, startDateStr, endDateStr, countLeft));
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return pillReminders;
    }

    public ReminderTime[] getReminderEntriesTime(UUID idReminder, String startDate, int type){
        List<ReminderTime> pillReminderEntriesTime = new ArrayList<>();
        String rawQuery = "";
        String uuidStr = idReminder.toString().replace("-", "");
        if (type==0)
            rawQuery ="select rt._id_reminder_time, rt.reminder_time from reminder_time rt where rt._id_pill_reminder= X'"+uuidStr+"'";
        else
            rawQuery = "select rt._id_reminder_time, rt.reminder_time from reminder_time rt where rt._id_measurement_reminder= X'"+uuidStr+"'";
        Cursor cursor = database.rawQuery(rawQuery, null);
        */

        /*String idStr = String.valueOf(idPillReminder);
        String rawQuery = "select rt._id_reminder_time, rt.reminder_time," +
                " (select count(*) from pill_reminder_entries pre2 where pre2._id_pill_reminder=? and pre2.is_done=1 and pre2._id_reminder_time=rt._id_reminder_time) as is_used" +
                " from reminder_time rt where rt._id_pill_reminder=?";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{idStr, idStr});*/

        /*
        if(cursor.moveToFirst()){
            do{
                pillReminderEntriesTime.add(
                        new ReminderTime(
                                ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_reminder_time"))),
                                (cursor.getString(cursor.getColumnIndex("reminder_time"))).substring(0,5)
                                )
                );
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return pillReminderEntriesTime.toArray(new ReminderTime[pillReminderEntriesTime.size()]);
    }

    public PillReminderDBInsertEntry getPillReminderDBInsertEntryByID(UUID prID){
        PillReminderDBInsertEntry prdbie = null;
        String uuidStr = prID.toString().replace("-", "");
        String rawQuery = "select pr._id_pill_reminder, pi.pill_name, pr.pill_count, pr._id_pill_count_type, pr.start_date, pr._id_having_meals_type, pr.having_meals_time," +
                " pr._id_cycle, pr.IsActive, pr.times_a_day, pr.annotation" +
                " from pill_reminders pr inner join pills pi on pi._id_pill=pr._id_pill where pr._id_pill_reminder=X'"+uuidStr+"'";
        Cursor cursor = database.rawQuery(rawQuery, null);
        if(cursor.moveToFirst()){
            do{
                UUID idPillReminder = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_pill_reminder")));
                String pillName = cursor.getString(cursor.getColumnIndex("pill_name"));
                int pillCount = cursor.getInt(cursor.getColumnIndex("pill_count"));
                int idPillCountType = cursor.getInt(cursor.getColumnIndex("_id_pill_count_type"));
                UUID idCycle = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_cycle")));
                String startDateStr = cursor.getString(cursor.getColumnIndex("start_date"));
                int idHavingMealsType = cursor.getInt(cursor.getColumnIndex("_id_having_meals_type"));
                int havingMealsTime = Math.abs(cursor.getInt(cursor.getColumnIndex("having_meals_time")));
                String annotation = cursor.getString(cursor.getColumnIndex("annotation"));
                int isActive = cursor.getInt(cursor.getColumnIndex("IsActive"));
                int times_aDay = cursor.getInt(cursor.getColumnIndex("times_a_day"));  // may be deleted
                //ReminderTime[] reminderTimes = getReminderEntriesTime(idPillReminder, startDateStr,0);

                DateData startDate = new DateData();
                try {
                    Date bufDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(bufDate);
                    startDate.setYear(cal.get(Calendar.YEAR)).setMonth(cal.get(Calendar.MONTH)+1).setDay(cal.get(Calendar.DAY_OF_MONTH));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                prdbie = new PillReminderDBInsertEntry(
                        idPillReminder, pillName, pillCount, idPillCountType, startDate, idCycle,
                        idHavingMealsType, havingMealsTime, annotation, isActive, null
                );
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return prdbie;
    }

    public CycleAndPillComby getCycleAndPillCombyByID(UUID prID){
        String uuidStr = prID.toString().replace("-", "");
        String rawQuery = "select pr._id_pill_reminder, pi.pill_name, pr.pill_count, pr._id_pill_count_type, pr.start_date, pr._id_having_meals_type, pr.having_meals_time, pr.annotation, pr.IsActive, pr.times_a_day," +
                " pr._id_cycle, cl.period, cl.period_DM_type, cl.once_a_period, cl.once_a_period_DM_type, cl._id_cycling_type, cl._id_week_schedule" +
                " from pill_reminders pr inner join pills pi on pi._id_pill=pr._id_pill inner join cycles cl on pr._id_cycle=cl._id_cycle  where pr._id_pill_reminder=X'"+uuidStr+"'";
        Cursor cursor = database.rawQuery(rawQuery, null);
        CycleAndPillComby cycleAndPillComby = new CycleAndPillComby();
        if(cursor.moveToFirst()){
            do{
                UUID idPillReminder = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_pill_reminder")));
                String pillName = cursor.getString(cursor.getColumnIndex("pill_name"));
                int pillCount = cursor.getInt(cursor.getColumnIndex("pill_count"));
                int idPillCountType = cursor.getInt(cursor.getColumnIndex("_id_pill_count_type"));
                UUID idCycle =  ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_cycle")));
                String startDateStr = cursor.getString(cursor.getColumnIndex("start_date"));
                int idHavingMealsType = cursor.getInt(cursor.getColumnIndex("_id_having_meals_type"));
                int havingMealsTime = Math.abs(cursor.getInt(cursor.getColumnIndex("having_meals_time")));
                String annotation = cursor.getString(cursor.getColumnIndex("annotation"));
                int isActive = cursor.getInt(cursor.getColumnIndex("IsActive"));
                int times_aDay = cursor.getInt(cursor.getColumnIndex("times_a_day"));  // may be deleted
                ReminderTime[] reminderTimes = getReminderEntriesTime(idPillReminder, startDateStr,0);

                DateData startDate = new DateData();
                try {
                    Date bufDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(bufDate);
                    startDate.setYear(cal.get(Calendar.YEAR)).setMonth(cal.get(Calendar.MONTH)+1).setDay(cal.get(Calendar.DAY_OF_MONTH));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                PillReminderDBInsertEntry prdbie = new PillReminderDBInsertEntry(
                        idPillReminder, pillName, pillCount, idPillCountType, startDate, idCycle,
                        idHavingMealsType, havingMealsTime, annotation, isActive, reminderTimes
                );

                int period = cursor.getInt(cursor.getColumnIndex("period"));
                int periodDMtype = cursor.getInt(cursor.getColumnIndex("period_DM_type"));
                int once_aPeriod = cursor.getInt(cursor.getColumnIndex("once_a_period"));
                int once_aPeriodDMtype = cursor.getInt(cursor.getColumnIndex("once_a_period_DM_type"));
                int idCyclingType = cursor.getInt(cursor.getColumnIndex("_id_cycling_type"));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex("_id_week_schedule"));
                int[] weekSchedule = null;
                UUID idWeekSchedule = null;
                if (blob!=null)
                {
                    idWeekSchedule = ConvertingUtils.convertBytesToUUID(
                            cursor.getBlob(cursor.getColumnIndex("_id_week_schedule")));
                    weekSchedule = getWeekSchedule(idWeekSchedule);
                }
                else
                    weekSchedule = new int[7];

                CycleDBInsertEntry cdbie = new CycleDBInsertEntry(
                        period, periodDMtype, once_aPeriod, once_aPeriodDMtype, idCyclingType,
                        weekSchedule, 0
                );
                cdbie.setIdCycle(idCycle);
                cdbie.setIdWeekSchedule(idWeekSchedule);
                cycleAndPillComby.pillReminderDBInsertEntry=prdbie;
                cycleAndPillComby.cycleDBInsertEntry=cdbie;
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return cycleAndPillComby;
    }

    public List<PillReminderEntry> getPillReminderEntriesByDate(DateData date){
        ArrayList<PillReminderEntry> pillReminderEntries = new ArrayList<>();
        String rawQuery = "select pre._id_pill_reminder_entry, pre.is_done, pr._id_having_meals_type, pre.reminder_time, pr.having_meals_time, pr.pill_count, pct.type_name, pre.reminder_date, pi.pill_name" +
                " from pill_reminder_entries pre inner join pill_reminders pr on pre._id_pill_reminder=pr._id_pill_reminder inner join pills pi on pi._id_pill=pr._id_pill inner join pill_count_types pct on" +
                " pr._id_pill_count_type=pct._id_pill_count_type where pre.reminder_date=? and (pr.IsActive=1 or pre.is_done=1) ORDER BY pre.is_done";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{date.getDateString()});
        Calendar calendar = Calendar.getInstance();
        if(cursor.moveToFirst()){
            do{
                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_pill_reminder_entry")));
                String pillName = cursor.getString(cursor.getColumnIndex("pill_name"));
                int pillCount = cursor.getInt(cursor.getColumnIndex("pill_count"));
                String pillCountType = cursor.getString(cursor.getColumnIndex("type_name"));
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
                boolean isLate = false;
                if (isDone==0)
                    isLate = calendar.getTime().compareTo(reminderDate)>0?true:false;

                pillReminderEntries.add(new PillReminderEntry(
                        id, pillName, pillCount, pillCountType, reminderDate, havingMealsType, havingMealsTime, isDone, isLate));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  pillReminderEntries;
    }

    public List<PillReminderEntry> getPillReminderEntriesByDateAndPillReminder(UUID idPillReminder, DateData date){
        String uuidStr = idPillReminder.toString().replace("-", "");
        ArrayList<PillReminderEntry> pillReminderEntries = new ArrayList<>();
        String rawQuery = "select pre._id_pill_reminder_entry, pre.is_done, pr._id_having_meals_type, pre.reminder_time, pr.having_meals_time, pr.pill_count, pct.type_name, pre.reminder_date, pi.pill_name" +
                " from pill_reminder_entries pre inner join pill_reminders pr on pre._id_pill_reminder=pr._id_pill_reminder inner join pills pi on pi._id_pill=pr._id_pill inner join pill_count_types pct on" +
                " pr._id_pill_count_type=pct._id_pill_count_type where pre.reminder_date=? and pre._id_pill_reminder =X'" + uuidStr + "' and (pr.IsActive=1 or pre.is_done=1) ORDER BY pre.is_done";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{date.getDateString()});
        Calendar calendar = Calendar.getInstance();
        if(cursor.moveToFirst()){
            do{
                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_pill_reminder_entry")));
                String pillName = cursor.getString(cursor.getColumnIndex("pill_name"));
                int pillCount = cursor.getInt(cursor.getColumnIndex("pill_count"));
                String pillCountType = cursor.getString(cursor.getColumnIndex("type_name"));
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
                boolean isLate = false;
                if (isDone==0)
                    isLate = calendar.getTime().compareTo(reminderDate)>0?true:false;

                pillReminderEntries.add(new PillReminderEntry(
                        id, pillName, pillCount, pillCountType, reminderDate, havingMealsType, havingMealsTime, isDone, isLate));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  pillReminderEntries;
    }

    public List<PillReminderEntry> getPillReminderEntriesBetweenDates(DateData date1, DateData date2, int param){
        ArrayList<PillReminderEntry> pillReminderEntries = new ArrayList<>();
        Cursor cursor;
        if (param==0){
            String rawQuery = "select pre._id_pill_reminder_entry, pre.is_done, pr._id_having_meals_type, pre.reminder_time, pr.having_meals_time, pr.pill_count, pct.type_name, pre.reminder_date, pi.pill_name" +
                    " from pill_reminder_entries pre inner join pill_reminders pr on pre._id_pill_reminder=pr._id_pill_reminder inner join pills pi on pi._id_pill=pr._id_pill inner join pill_count_types pct on" +
                    " pr._id_pill_count_type=pct._id_pill_count_type where pre.reminder_date between ? and ? and (pr.IsActive=1 or pre.is_done=1) ORDER BY pre.reminder_date DESC, pre.reminder_time DESC";
            cursor = database.rawQuery(rawQuery, new String[]{date1.getDateString(), date2.getDateString()});
        }
        else {
            String rawQuery = "select pre._id_pill_reminder_entry, pre.is_done, pr._id_having_meals_type, pre.reminder_time, pr.having_meals_time, pr.pill_count, pct.type_name, pre.reminder_date, pi.pill_name" +
                    " from pill_reminder_entries pre inner join pill_reminders pr on pre._id_pill_reminder=pr._id_pill_reminder inner join pills pi on pi._id_pill=pr._id_pill inner join pill_count_types pct on" +
                    " pr._id_pill_count_type=pct._id_pill_count_type where pre.reminder_date <= ? and (pr.IsActive=1 or pre.is_done=1) ORDER BY pre.reminder_date DESC, pre.reminder_time DESC";
            cursor = database.rawQuery(rawQuery, new String[]{date2.getDateString()});
        }

        Calendar calendar = Calendar.getInstance();

        if(cursor.moveToFirst()){
            do{
                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_pill_reminder_entry")));
                String pillName = cursor.getString(cursor.getColumnIndex("pill_name"));
                int pillCount = cursor.getInt(cursor.getColumnIndex("pill_count"));
                String pillCountType = cursor.getString(cursor.getColumnIndex("type_name"));
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
                boolean isLate = false;
                if (isDone==0)
                    isLate = calendar.getTime().compareTo(reminderDate)>0?true:false;

                pillReminderEntries.add(new PillReminderEntry(
                        id, pillName, pillCount, pillCountType, reminderDate, havingMealsType, havingMealsTime, isDone, isLate));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return  pillReminderEntries;
    }

    public void updateIsDonePillReminderEntry(int isDone, UUID pillReminderEntryID, String newTime){
        ContentValues pillReminderEntryTableValues = new ContentValues();
        pillReminderEntryTableValues.put("is_done", isDone);
        if (!newTime.equals(""))
            pillReminderEntryTableValues.put("reminder_time", newTime);
        database.update("pill_reminder_entries", pillReminderEntryTableValues,
                "_id_pill_reminder_entry=X'" + pillReminderEntryID.toString().replace("-", "") + "'", null);
    }

    public void deletePillReminderEntriesAfterDate(UUID idPillReminder, String date){
        database.delete("pill_reminder_entries", "reminder_date >= ? and lower(hex(_id_pill_reminder)) = ? and is_done = 0",
                new String[]{date, idPillReminder.toString().replace("-", "")});
        List<PillReminderEntry> pillReminderEntries = getPillReminderEntriesByDate(new DateData(2019, 2, 10));
        int f = 10;
    }

    public void deletePillReminderEntriesByPillReminderId(UUID idPillReminder){
        database.delete("pill_reminder_entries", "lower(hex(_id_pill_reminder)) = ?",
                new String[]{idPillReminder.toString().replace("-", "")});
    }

    public void deletePillReminderById(UUID idPillReminder){
        database.delete("pill_reminders", "lower(hex(_id_pill_reminder)) = ?",
                new String[]{idPillReminder.toString().replace("-", "")});
    }
    */
    //***********************************end************************************************************************

    //***********************************work with Cycle************************************************************

    /*private int[] getWeekSchedule(UUID idWeekSchedule){
        int[] weekSchedule = new int[7];
        String uuidStr = idWeekSchedule.toString().replace("-", "");

        String rawQuery = "select * from week_schedules where _id_week_schedule = X'"+uuidStr+"'";
        Cursor cursor = database.rawQuery(rawQuery, null);
        */
        /*String rawQuery = "select * from week_schedules where lower(hex(_id_week_schedule)) = ?";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{uuidStr});*/

        /*Cursor cursor = database.query("week_schedules", null, "_id_week_schedule=?",
                new String[]{"X'" + sb.toString()+"'"}, null, null, null);*/
        /*
        if(cursor.moveToFirst()){
            do{
                weekSchedule[0] = cursor.getInt(cursor.getColumnIndex("mon"));
                weekSchedule[1] = cursor.getInt(cursor.getColumnIndex("tue"));
                weekSchedule[2] = cursor.getInt(cursor.getColumnIndex("wed"));
                weekSchedule[3] = cursor.getInt(cursor.getColumnIndex("thu"));
                weekSchedule[4] = cursor.getInt(cursor.getColumnIndex("fri"));
                weekSchedule[5] = cursor.getInt(cursor.getColumnIndex("sat"));
                weekSchedule[6] = cursor.getInt(cursor.getColumnIndex("sun"));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return weekSchedule;
    }

    public UUID insertCycle(Integer period, Integer periodDMType, @Nullable Integer once_aPeriod,
                           @Nullable Integer once_aPeriodDMType, Integer idCyclingType,
                           @Nullable int[] weekSchedule){
        UUID weekScheduleID = null;
        if (weekSchedule!=null)
        {
            weekScheduleID = UUID.randomUUID();
            ContentValues weekScheduleTableValues = new ContentValues();
            weekScheduleTableValues.put("mon", weekSchedule[1]);
            weekScheduleTableValues.put("tue", weekSchedule[2]);
            weekScheduleTableValues.put("wed", weekSchedule[3]);
            weekScheduleTableValues.put("thu", weekSchedule[4]);
            weekScheduleTableValues.put("fri", weekSchedule[5]);
            weekScheduleTableValues.put("sat", weekSchedule[6]);
            weekScheduleTableValues.put("sun", weekSchedule[0]);
            weekScheduleTableValues.put("_id_week_schedule", ConvertingUtils.convertUUIDToBytes(weekScheduleID));
            database.insert("week_schedules", null, weekScheduleTableValues);
        }

        UUID cycleId = UUID.randomUUID();
        ContentValues cycleTableValues = new ContentValues();
        cycleTableValues.put("period", period);
        cycleTableValues.put("_id_cycle", ConvertingUtils.convertUUIDToBytes(cycleId));
        cycleTableValues.put("period_DM_type", periodDMType);
        cycleTableValues.put("once_a_period", once_aPeriod);
        cycleTableValues.put("once_a_period_DM_type", once_aPeriodDMType);
        cycleTableValues.put("_id_week_schedule", weekScheduleID==null?null:ConvertingUtils.convertUUIDToBytes(weekScheduleID));
        cycleTableValues.put("_id_cycling_type", idCyclingType);
        database.insert("cycles", null, cycleTableValues);

        return cycleId;
    }

    public UUID updateCycle(UUID inCycleId, UUID weekScheduleID, Integer period, Integer periodDMType, @Nullable Integer once_aPeriod,
                           @Nullable Integer once_aPeriodDMType, Integer idCyclingType,
                           @Nullable int[] weekSchedule){
        if (weekSchedule!=null)
        {
            ContentValues weekScheduleTableValues = new ContentValues();
            weekScheduleTableValues.put("mon", weekSchedule[1]);
            weekScheduleTableValues.put("tue", weekSchedule[2]);
            weekScheduleTableValues.put("wed", weekSchedule[3]);
            weekScheduleTableValues.put("thu", weekSchedule[4]);
            weekScheduleTableValues.put("fri", weekSchedule[5]);
            weekScheduleTableValues.put("sat", weekSchedule[6]);
            weekScheduleTableValues.put("sun", weekSchedule[0]);
            if (weekScheduleID!=null) {
                database.update("week_schedules", weekScheduleTableValues,
                        "lower(hex(_id_week_schedule)) = ?", new String[]{weekScheduleID.toString().replace("-", "")});
            }
            else{
                weekScheduleID = UUID.randomUUID();
                weekScheduleTableValues.put("_id_week_schedule", ConvertingUtils.convertUUIDToBytes(weekScheduleID));
                database.insert("week_schedules", null, weekScheduleTableValues);
            }
        }

        ContentValues cycleTableValues = new ContentValues();
        cycleTableValues.put("period", period);
        cycleTableValues.put("period_DM_type", periodDMType);
        cycleTableValues.put("once_a_period", once_aPeriod);
        cycleTableValues.put("once_a_period_DM_type", once_aPeriodDMType);
        cycleTableValues.put("_id_week_schedule", weekScheduleID==null?null:ConvertingUtils.convertUUIDToBytes(weekScheduleID));
        cycleTableValues.put("_id_cycling_type", idCyclingType);
         database.update("cycles", cycleTableValues, "lower(hex(_id_cycle)) = ?",
                 new String[]{inCycleId.toString().replace("-", "")});

        return inCycleId;
    }

    public void deleteWeekScheduleByIdCascade(UUID idWeekSchedule){
        database.delete("week_schedules", "lower(hex(_id_week_schedule)) = ?",
                new String[]{idWeekSchedule.toString().replace("-", "")});
    }

    public void deleteCycleByIdCascade(UUID idCycle){
        database.delete("cycles", "lower(hex(_id_cycle)) = ?",
                new String[]{idCycle.toString().replace("-", "")});
    }
    //***********************************end************************************************************

    //***********************************work with measurement reminder************************************************************
    public UUID insertMeasurementReminder(int idMeasurementType,
                                         String startDate, @Nullable UUID idCycle, @Nullable Integer idHavingMealsType,
                                         @Nullable Integer havingMealsTime, String annotation, Integer isActive, Integer times_aDay,
                                         Integer isOneTime){

        UUID measurementReminderId = UUID.randomUUID();
        ContentValues measurementReminderTableValues = new ContentValues();
        measurementReminderTableValues.put("_id_measurement_reminder", ConvertingUtils.convertUUIDToBytes(measurementReminderId));
        measurementReminderTableValues.put("_id_measurement_type", idMeasurementType);
        measurementReminderTableValues.put("start_date", startDate);
        measurementReminderTableValues.put("_id_cycle", idCycle==null?null:ConvertingUtils.convertUUIDToBytes(idCycle));
        measurementReminderTableValues.put("_id_having_meals_type", idHavingMealsType);
        measurementReminderTableValues.put("having_meals_time", havingMealsTime);
        measurementReminderTableValues.put("annotation", annotation);
        measurementReminderTableValues.put("IsActive", isActive);
        measurementReminderTableValues.put("times_a_day", times_aDay);
        if (isOneTime==1)
            measurementReminderTableValues.put("is_one_time", isOneTime);
        database.insert("measurement_reminders", null, measurementReminderTableValues);

        //Log.i("pill_new_id", String.valueOf(pillId));
        return measurementReminderId;
    }

    public List<MeasurementReminder> getAllMeasurementReminders(){
        List<MeasurementReminder> measurementReminders = new ArrayList<>();
        */
        /*String rawQuery = "select  mr.start_date, mr.isActive, cl.period, mr.times_a_day, mr._id_measurement_type, mr._id_having_meals_type, cl.period_DM_type, " +
                "      CASE mr._id_measurement_type  " +
                "         WHEN 1 THEN (select COUNT(*) from temperature_measurements tm where tm._id_measurement_reminder=mr._id_measurement_reminder and tm.is_done=0 ) " +
                "         WHEN 2 THEN (select COUNT(*) from blood_pressure_measurements bpm where bpm._id_measurement_reminder=mr._id_measurement_reminder and bpm.is_done=0 ) " +
                "      END as count_left, mr._id_measurement_reminder " +
                "    from measurement_reminders mr inner join cycles cl on mr._id_cycle=cl._id_cycle ORDER BY mr.IsActive";*/
        /*
        String rawQuery = "select  mr.start_date, mr.isActive, cl.period, mr.times_a_day, mr._id_measurement_type, mr._id_having_meals_type, cl.period_DM_type, " +
                "(select COUNT(*) from measurement_reminder_entries mre where mre._id_measurement_reminder=mr._id_measurement_reminder and mre.is_done=0 ) " +
                "as count_left, mr._id_measurement_reminder, mt._id_measur_value_type " +
                "from measurement_reminders mr inner join cycles cl on mr._id_cycle=cl._id_cycle inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type ORDER BY mr.isActive DESC";
        Cursor cursor = database.rawQuery(rawQuery, null);
        if(cursor.moveToFirst()){
            do{
                Calendar calendar = Calendar.getInstance();

                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_measurement_reminder")));
                String startDateStr = cursor.getString(cursor.getColumnIndex("start_date"));
                int havingMealsType = cursor.getInt(cursor.getColumnIndex("_id_having_meals_type"));
                int period = cursor.getInt(cursor.getColumnIndex("period"));
                int isActive = cursor.getInt(cursor.getColumnIndex("isActive"));
                int countLeft = cursor.getInt(cursor.getColumnIndex("count_left"));
                int times_aDay = cursor.getInt(cursor.getColumnIndex("times_a_day"));
                int periodDM_Type = cursor.getInt(cursor.getColumnIndex("period_DM_type"));
                int idMeasurementType = cursor.getInt(cursor.getColumnIndex("_id_measurement_type"));
                int idMeasurementValueType = cursor.getInt(cursor.getColumnIndex("_id_measur_value_type"));

                Date startDate;
                SimpleDateFormat dateFormatOld = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    startDate = dateFormatOld.parse(startDateStr);
                    startDateStr = dateFormatNew.format(startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    startDate = new Date();
                }
                String endDateStr = "0";
                calendar.setTime(startDate);
                switch (periodDM_Type){
                    case 1:
                        calendar.add(Calendar.DATE, period);
                        break;
                    case 2:
                        calendar.add(Calendar.DATE, period*7);
                        break;
                    case 3:
                        calendar.add(Calendar.DATE, period*30);
                        break;
                }
                endDateStr = dateFormatNew.format(calendar.getTime());

                measurementReminders.add(new MeasurementReminder(id, idMeasurementType, havingMealsType,
                        isActive, times_aDay, startDateStr, endDateStr, countLeft, idMeasurementValueType));

            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return measurementReminders;
    }

    public List<float[]> getMeasurementReminderEntriesPerMonth(UUID idMeasurementReminder, int month, int year, int type,
                                                               String timeStr1, String timeStr2){
        List<float[]> measurementReminderEntryValues = new ArrayList<float[]>();
        String uuidStr = idMeasurementReminder.toString().replace("-", "");
        Cursor cursor;
        if (type == 0){
            String rawQuery = "select AVG(mre.value1) as avg_value1, AVG(mre.value2) as avg_value2, mre.reminder_date" +
                    " from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder" +
                    " where mre._id_measurement_reminder = X'"+uuidStr+"' and mre.is_done = 1 and strftime('%m', mre.reminder_date) = ? and strftime('%Y', mre.reminder_date) = ?" +
                    " and mre.reminder_time between ? and ? GROUP BY mre.reminder_date";
            cursor = database.rawQuery(rawQuery, new String[]{String.format("%02d",month), String.valueOf(year),
                                        timeStr1, timeStr2});
        }
        else if (type == 1){
            String rawQuery = "select AVG(mre.value1) as avg_value1, AVG(mre.value2) as avg_value2, mre.reminder_date" +
                    " from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder" +
                    " where mre._id_measurement_reminder = X'"+uuidStr+"' and mre.is_done = 1 and strftime('%m', mre.reminder_date) = ? and strftime('%Y', mre.reminder_date) = ?" +
                    " GROUP BY mre.reminder_date";
            cursor = database.rawQuery(rawQuery, new String[]{String.format("%02d",month), String.valueOf(year)});
        }
        else {
            String rawQuery = "select AVG(mre.value1) as avg_value1, AVG(mre.value2) as avg_value2, mre.reminder_date" +
                    " from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder" +
                    " where mre._id_measurement_reminder = X'"+uuidStr+"' and mre.is_done = 1 and strftime('%m', mre.reminder_date) = ? and strftime('%Y', mre.reminder_date) = ?" +
                    " and (mre.reminder_time between ? and '00:00:00' or mre.reminder_time between '00:00:00' and ?) GROUP BY mre.reminder_date";
            cursor = database.rawQuery(rawQuery, new String[]{String.valueOf(month), String.valueOf(year)});
        }
        if(cursor.moveToFirst()){
            do{
                float avgValue1 = cursor.getFloat(cursor.getColumnIndex("avg_value1"));
                float avgValue2 = cursor.getFloat(cursor.getColumnIndex("avg_value2"));
                String reminderDateStr = cursor.getString(cursor.getColumnIndex("reminder_date"));

                float dayOfMonth = Float.valueOf(reminderDateStr.split("-")[2]);
                float[] curMeasurementReminderEntryValuesArr = new float[]{
                        avgValue1, avgValue2, dayOfMonth
                };
                measurementReminderEntryValues.add(curMeasurementReminderEntryValuesArr);
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return measurementReminderEntryValues;
    }

    public List<MeasurementStatEntry> getAllMeasurementStatEntries(int limit){
        List<MeasurementStatEntry> measurementStatEntries = new ArrayList<MeasurementStatEntry>();
        Cursor cursor;
        if (limit!=-1) {
            String rawQuery = "select mr.start_date, mr.isActive, cl.period, mr.times_a_day, mr._id_measurement_type, mr._id_having_meals_type, cl.period_DM_type, mr._id_measurement_reminder, mt._id_measur_value_type," +
                    " COUNT(sub.id) as count_done, SUM(sub.value1) as sum_value1, SUM(sub.value2) as sum_value2" +
                    " from measurement_reminders mr inner join cycles cl on mr._id_cycle=cl._id_cycle inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type" +
                    " join (" +
                    "    select" +
                    "        mre._id_measurement_reminder as id," +
                    "        mre.value1," +
                    "        mre.value2" +
                    "    from measurement_reminder_entries mre where mre.is_done=1" +
                    " ) sub on sub.id=mr._id_measurement_reminder GROUP BY mr._id_measurement_reminder ORDER BY mr.start_date DESC LIMIT ?";
            cursor = database.rawQuery(rawQuery, new String[]{String.valueOf(limit)});
        }
        else {
            String rawQuery = "select mr.start_date, mr.isActive, cl.period, mr.times_a_day, mr._id_measurement_type, mr._id_having_meals_type, cl.period_DM_type, mr._id_measurement_reminder, mt._id_measur_value_type," +
                    " COUNT(sub.id) as count_done, SUM(sub.value1) as sum_value1, SUM(sub.value2) as sum_value2" +
                    " from measurement_reminders mr inner join cycles cl on mr._id_cycle=cl._id_cycle inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type" +
                    " join (" +
                    "    select" +
                    "        mre._id_measurement_reminder as id," +
                    "        mre.value1," +
                    "        mre.value2" +
                    "    from measurement_reminder_entries mre where mre.is_done=1" +
                    " ) sub on sub.id=mr._id_measurement_reminder GROUP BY mr._id_measurement_reminder ORDER BY mr.start_date DESC";
            cursor = database.rawQuery(rawQuery, null);
        }
        if(cursor.moveToFirst()){
            do{
                Calendar calendar = Calendar.getInstance();

                String startDateStr = cursor.getString(0);
                int isActive = cursor.getInt(1);
                int period = cursor.getInt(2);
                int times_aDay = cursor.getInt(3);
                int idMeasurementType = cursor.getInt(4);
                int havingMealsType = cursor.getInt(5);
                int periodDM_Type = cursor.getInt(6);
                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(7));
                int idMeasurementValueType = cursor.getInt(8);
                int countDone = cursor.getInt(9);
                double sumValue1 = cursor.getDouble(10);
                double sumValue2 = cursor.getDouble(11);
                */

                /*int id = cursor.getInt(cursor.getColumnIndex("_id_measurement_reminder"));
                String startDateStr = cursor.getString(cursor.getColumnIndex("start_date"));
                int havingMealsType = cursor.getInt(cursor.getColumnIndex("_id_having_meals_type"));
                int period = cursor.getInt(cursor.getColumnIndex("period"));
                int isActive = cursor.getInt(cursor.getColumnIndex("isActive"));
                int countDone = cursor.getInt(cursor.getColumnIndex("count_done"));
                int times_aDay = cursor.getInt(cursor.getColumnIndex("times_a_day"));
                int periodDM_Type = cursor.getInt(cursor.getColumnIndex("period_DM_type"));
                int idMeasurementType = cursor.getInt(cursor.getColumnIndex("_id_measurement_type"));
                int idMeasurementValueType = cursor.getInt(cursor.getColumnIndex("_id_measur_value_type"));
                double sumValue1 = cursor.getDouble(cursor.getColumnIndex("sum_value1"));
                double sumValue2 = cursor.getDouble(cursor.getColumnIndex("sum_value2"));*/
                /*

                double[] averageCurValues = new double[2];
                averageCurValues[0] = sumValue1>-10000?(double)(sumValue1/countDone):-10000;
                averageCurValues[1] = sumValue2>-10000?(double)(sumValue2/countDone):-10000;

                Date startDate;
                SimpleDateFormat dateFormatOld = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd.MM.yyyy");
                try {
                    startDate = dateFormatOld.parse(startDateStr);
                    startDateStr = dateFormatNew.format(startDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                    startDate = new Date();
                }
                String endDateStr = "0";
                calendar.setTime(startDate);
                switch (periodDM_Type){
                    case 1:
                        calendar.add(Calendar.DATE, period);
                        break;
                    case 2:
                        calendar.add(Calendar.DATE, period*7);
                        break;
                    case 3:
                        calendar.add(Calendar.DATE, period*30);
                        break;
                }
                endDateStr = dateFormatNew.format(calendar.getTime());

                MeasurementType measurementType = DBStaticEntries.getMeasurementTypeById(idMeasurementType);
                double[] standardValues = measurementType.getStandardValues();
                String measurementValueTypeStr = measurementType.getMeasurementValueTypeName();

                measurementStatEntries.add(new MeasurementStatEntry(id, idMeasurementType, havingMealsType,
                        isActive, times_aDay, startDateStr, endDateStr, countDone, idMeasurementValueType,
                        averageCurValues, standardValues, measurementValueTypeStr));

            }
            while (cursor.moveToNext());
        }

        cursor.close();
        return measurementStatEntries;
    }

    public void insertMeasurementReminderEntry(String reminder_date, UUID idMeasurementReminder, String reminderTime){
        UUID idMeasurRemindEntry = UUID.randomUUID();
        ContentValues measurementReminderEntryTableValues = new ContentValues();
        measurementReminderEntryTableValues.put("_id_measur_remind_entry", ConvertingUtils.convertUUIDToBytes(idMeasurRemindEntry));
        measurementReminderEntryTableValues.put("is_done", 0);
        measurementReminderEntryTableValues.put("value1", -10000);
        measurementReminderEntryTableValues.put("value2", -10000);
        measurementReminderEntryTableValues.put("reminder_date", reminder_date);
        measurementReminderEntryTableValues.put("_id_measurement_reminder", ConvertingUtils.convertUUIDToBytes(idMeasurementReminder));
        measurementReminderEntryTableValues.put("reminder_time", reminderTime);
        database.insert("measurement_reminder_entries", null, measurementReminderEntryTableValues);
    }

    public void insertMeasurementReminderEntry(String reminder_date, UUID idMeasurementReminder, String reminderTime, double value1, double value2){
        UUID idMeasurRemindEntry = UUID.randomUUID();
        ContentValues measurementReminderEntryTableValues = new ContentValues();
        measurementReminderEntryTableValues.put("_id_measur_remind_entry", ConvertingUtils.convertUUIDToBytes(idMeasurRemindEntry));
        measurementReminderEntryTableValues.put("is_done", 1);
        measurementReminderEntryTableValues.put("value1", value1);
        measurementReminderEntryTableValues.put("value2", value2);
        measurementReminderEntryTableValues.put("reminder_date", reminder_date);
        measurementReminderEntryTableValues.put("_id_measurement_reminder", ConvertingUtils.convertUUIDToBytes(idMeasurementReminder));
        measurementReminderEntryTableValues.put("reminder_time", reminderTime);
        database.insert("measurement_reminder_entries", null, measurementReminderEntryTableValues);
    }

    public CycleAndMeasurementComby getCycleAndMeasurementCombyById(UUID prID){
        String uuidStr = prID.toString().replace("-", "");
        String rawQuery = "select mr._id_measurement_reminder, mr._id_measurement_type, mt._id_measur_value_type, mr.start_date, mr._id_having_meals_type, mr.having_meals_time, mr.annotation, mr.isActive, mr.times_a_day, " +
                " mr._id_cycle, cl.period, cl.period_DM_type, cl.once_a_period, cl.once_a_period_DM_type, cl._id_cycling_type, cl._id_week_schedule " +
                " from measurement_reminders mr inner join cycles cl on mr._id_cycle=cl._id_cycle inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type " +
                " where mr._id_measurement_reminder=X'"+uuidStr+"'";
        Cursor cursor = database.rawQuery(rawQuery, null);
        CycleAndMeasurementComby cycleAndMeasurementComby = new CycleAndMeasurementComby();
        if(cursor.moveToFirst()){
            do{
                UUID idMeasurementReminder = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_measurement_reminder")));
                int idMeasurementType = cursor.getInt(cursor.getColumnIndex("_id_measurement_type"));
                int idMeasurementValueType = cursor.getInt(cursor.getColumnIndex("_id_measur_value_type"));

                UUID idCycle = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_cycle")));
                String startDateStr = cursor.getString(cursor.getColumnIndex("start_date"));
                int idHavingMealsType = cursor.getInt(cursor.getColumnIndex("_id_having_meals_type"));
                int havingMealsTime = Math.abs(cursor.getInt(cursor.getColumnIndex("having_meals_time")));
                String annotation = cursor.getString(cursor.getColumnIndex("annotation"));
                int isActive = cursor.getInt(cursor.getColumnIndex("isActive"));
                int times_aDay = cursor.getInt(cursor.getColumnIndex("times_a_day"));  // may be deleted
                ReminderTime[] reminderTimes = getReminderEntriesTime(idMeasurementReminder, startDateStr, 1);

                DateData startDate = new DateData();
                try {
                    Date bufDate = new SimpleDateFormat("yyyy-MM-dd").parse(startDateStr);
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(bufDate);
                    startDate.setYear(cal.get(Calendar.YEAR)).setMonth(cal.get(Calendar.MONTH)+1).setDay(cal.get(Calendar.DAY_OF_MONTH));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                MeasurementReminderDBEntry mrdbe = new MeasurementReminderDBEntry(
                        idMeasurementType, idMeasurementReminder, startDate, idCycle,
                        idHavingMealsType, havingMealsTime, annotation, isActive, reminderTimes,
                        idMeasurementValueType
                );

                int period = cursor.getInt(cursor.getColumnIndex("period"));
                int periodDMtype = cursor.getInt(cursor.getColumnIndex("period_DM_type"));
                int once_aPeriod = cursor.getInt(cursor.getColumnIndex("once_a_period"));
                int once_aPeriodDMtype = cursor.getInt(cursor.getColumnIndex("once_a_period_DM_type"));
                int idCyclingType = cursor.getInt(cursor.getColumnIndex("_id_cycling_type"));
                byte[] blob = cursor.getBlob(cursor.getColumnIndex("_id_week_schedule"));
                int[] weekSchedule = null;
                UUID idWeekSchedule = null;
                if (blob!=null)
                {
                    idWeekSchedule = ConvertingUtils.convertBytesToUUID(
                            cursor.getBlob(cursor.getColumnIndex("_id_week_schedule")));
                    weekSchedule = getWeekSchedule(idWeekSchedule);
                }
                else
                    weekSchedule = new int[7];

                CycleDBInsertEntry cdbie = new CycleDBInsertEntry(
                        period, periodDMtype, once_aPeriod, once_aPeriodDMtype, idCyclingType,
                        weekSchedule, 0
                );
                cdbie.setIdCycle(idCycle);
                cdbie.setIdWeekSchedule(idWeekSchedule);
                cycleAndMeasurementComby.measurementReminderDBEntry=mrdbe;
                cycleAndMeasurementComby.cycleDBInsertEntry=cdbie;
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return cycleAndMeasurementComby;
    }

    public void deleteMeasurementReminderEntriesByMeasurementReminderId(UUID idMeasurementReminder){
        database.delete("measurement_reminder_entries", "lower(hex(_id_measurement_reminder)) = ?",
                new String[]{idMeasurementReminder.toString().replace("-", "")});
    }

    public UUID updateMeasurementReminder(UUID idMeasurementReminder,
                                  String startDate, UUID idCycle, @Nullable Integer idHavingMealsType,
                                  @Nullable Integer havingMealsTime, String annotation, Integer isActive, Integer times_aDay
                                  ){

        ContentValues measurementReminderTableValues = new ContentValues();
        measurementReminderTableValues.put("start_date", startDate);
        measurementReminderTableValues.put("_id_cycle", ConvertingUtils.convertUUIDToBytes(idCycle));
        measurementReminderTableValues.put("_id_having_meals_type", idHavingMealsType);
        measurementReminderTableValues.put("having_meals_time", havingMealsTime);
        measurementReminderTableValues.put("annotation", annotation);
        measurementReminderTableValues.put("IsActive", isActive);
        measurementReminderTableValues.put("times_a_day", times_aDay);
        database.update("measurement_reminders", measurementReminderTableValues,
                "lower(hex(_id_measurement_reminder)) = ?", new String[]{idMeasurementReminder.toString().replace("-", "")});

        return idMeasurementReminder;
    }

    public void deleteMeasurementReminderEntriesAfterDate(UUID idMeasurementReminder, String date){
        database.delete("measurement_reminder_entries", "reminder_date >= ? and lower(hex(_id_measurement_reminder)) = ? and is_done = 0",
                new String[]{date, idMeasurementReminder.toString().replace("-", "")});
    }

    public List<MeasurementReminderEntry> getMeasurementReminderEntriesByDate(DateData date){
        ArrayList<MeasurementReminderEntry> measurementReminderEntry = new ArrayList<>();
        String rawQuery = "select mre._id_measur_remind_entry, mr._id_measurement_type, mvt.type_value_name, mt.type_name, mre.value1, mre.value2, mre.is_done, mr._id_having_meals_type, mre.reminder_time, mr.having_meals_time, mre.reminder_date " +
                " from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type " +
                " inner join measurement_value_types mvt on mt._id_measur_value_type=mvt._id_measur_value_type where mre.reminder_date=? and (mr.IsActive=1 or mre.is_done=1) ORDER BY mre.is_done";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{date.getDateString()});
        Calendar calendar = Calendar.getInstance();
        if(cursor.moveToFirst()){
            do{
                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_measur_remind_entry")));
                int idMeasurementType = cursor.getInt(cursor.getColumnIndex("_id_measurement_type"));
                String measurementValueTypeName = cursor.getString(cursor.getColumnIndex("type_value_name"));
                double value1 = cursor.getDouble(cursor.getColumnIndex("value1"));
                double value2 = cursor.getDouble(cursor.getColumnIndex("value2"));
                String dateStr = cursor.getString(cursor.getColumnIndex("reminder_date"));
                String timeStr = cursor.getString(cursor.getColumnIndex("reminder_time"));
                int havingMealsType = cursor.getInt(cursor.getColumnIndex("_id_having_meals_type"));
                int havingMealsTimeStr = cursor.getInt(cursor.getColumnIndex("having_meals_time"));
                int isDone = cursor.getInt(cursor.getColumnIndex("is_done"));
                String measurementTypeName = cursor.getString(cursor.getColumnIndex("type_name"));

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
                boolean isLate = false;
                if (isDone==0)
                    isLate = calendar.getTime().compareTo(reminderDate)>0?true:false;

                measurementReminderEntry.add(new MeasurementReminderEntry(
                        id, havingMealsType, idMeasurementType, measurementValueTypeName,
                        reminderDate, havingMealsTime, isDone, isLate, value1, value2, measurementTypeName));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return measurementReminderEntry;
    }

    public List<MeasurementReminderEntry> getMeasurementReminderEntriesByDateAndMeasurementReminder(UUID idMeasurementReminder, DateData date){
        String uuidStr = idMeasurementReminder.toString().replace("-", "");
        ArrayList<MeasurementReminderEntry> measurementReminderEntry = new ArrayList<>();
        String rawQuery = "select mre._id_measur_remind_entry, mr._id_measurement_type, mvt.type_value_name, mt.type_name, mre.value1, mre.value2, mre.is_done, mr._id_having_meals_type, mre.reminder_time, mr.having_meals_time, mre.reminder_date " +
                " from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type " +
                " inner join measurement_value_types mvt on mt._id_measur_value_type=mvt._id_measur_value_type where mre.reminder_date=? and mre._id_measurement_reminder =X'" + uuidStr + "'" +
                " and (mr.IsActive=1 or mre.is_done=1) ORDER BY mre.is_done";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{date.getDateString()});
        Calendar calendar = Calendar.getInstance();
        if(cursor.moveToFirst()){
            do{
                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_measur_remind_entry")));
                int idMeasurementType = cursor.getInt(cursor.getColumnIndex("_id_measurement_type"));
                String measurementValueTypeName = cursor.getString(cursor.getColumnIndex("type_value_name"));
                double value1 = cursor.getDouble(cursor.getColumnIndex("value1"));
                double value2 = cursor.getDouble(cursor.getColumnIndex("value2"));
                String dateStr = cursor.getString(cursor.getColumnIndex("reminder_date"));
                String timeStr = cursor.getString(cursor.getColumnIndex("reminder_time"));
                int havingMealsType = cursor.getInt(cursor.getColumnIndex("_id_having_meals_type"));
                int havingMealsTimeStr = cursor.getInt(cursor.getColumnIndex("having_meals_time"));
                int isDone = cursor.getInt(cursor.getColumnIndex("is_done"));
                String measurementTypeName = cursor.getString(cursor.getColumnIndex("type_name"));

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
                boolean isLate = false;
                if (isDone==0)
                    isLate = calendar.getTime().compareTo(reminderDate)>0?true:false;

                measurementReminderEntry.add(new MeasurementReminderEntry(
                        id, havingMealsType, idMeasurementType, measurementValueTypeName,
                        reminderDate, havingMealsTime, isDone, isLate, value1, value2, measurementTypeName));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return measurementReminderEntry;
    }

    public List<MeasurementReminderEntry> getMeasurementReminderEntriesBetweenDates(DateData date1, DateData date2, int param){
        ArrayList<MeasurementReminderEntry> measurementReminderEntry = new ArrayList<>();
        Cursor cursor;
        if (param==0){
            String rawQuery = "select mre._id_measur_remind_entry, mr._id_measurement_type, mvt.type_value_name, mt.type_name, mre.value1, mre.value2, mre.is_done, mr._id_having_meals_type, mre.reminder_time, mr.having_meals_time, mre.reminder_date" +
                    " from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type" +
                    " inner join measurement_value_types mvt on mt._id_measur_value_type=mvt._id_measur_value_type where mre.reminder_date between ? and ? and (mr.IsActive=1 or mre.is_done=1) ORDER BY mre.reminder_date DESC, mre.reminder_time DESC";
            cursor = database.rawQuery(rawQuery, new String[]{date1.getDateString(), date2.getDateString()});
        }
        else {
            String rawQuery = "select mre._id_measur_remind_entry, mr._id_measurement_type, mvt.type_value_name, mt.type_name, mre.value1, mre.value2, mre.is_done, mr._id_having_meals_type, mre.reminder_time, mr.having_meals_time, mre.reminder_date" +
                    " from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type" +
                    " inner join measurement_value_types mvt on mt._id_measur_value_type=mvt._id_measur_value_type where mre.reminder_date <= ? and (mr.IsActive=1 or mre.is_done=1) ORDER BY mre.reminder_date DESC, mre.reminder_time DESC";
            cursor = database.rawQuery(rawQuery, new String[]{date2.getDateString()});
        }

        Calendar calendar = Calendar.getInstance();

        if(cursor.moveToFirst()){
            do{
                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_measur_remind_entry")));
                int idMeasurementType = cursor.getInt(cursor.getColumnIndex("_id_measurement_type"));
                String measurementValueTypeName = cursor.getString(cursor.getColumnIndex("type_value_name"));
                double value1 = cursor.getDouble(cursor.getColumnIndex("value1"));
                double value2 = cursor.getDouble(cursor.getColumnIndex("value2"));
                String dateStr = cursor.getString(cursor.getColumnIndex("reminder_date"));
                String timeStr = cursor.getString(cursor.getColumnIndex("reminder_time"));
                int havingMealsType = cursor.getInt(cursor.getColumnIndex("_id_having_meals_type"));
                int havingMealsTimeStr = cursor.getInt(cursor.getColumnIndex("having_meals_time"));
                int isDone = cursor.getInt(cursor.getColumnIndex("is_done"));
                String measurementTypeName = cursor.getString(cursor.getColumnIndex("type_name"));

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
                boolean isLate = false;
                if (isDone==0)
                    isLate = calendar.getTime().compareTo(reminderDate)>0?true:false;

                measurementReminderEntry.add(new MeasurementReminderEntry(
                        id, havingMealsType, idMeasurementType, measurementValueTypeName,
                        reminderDate, havingMealsTime, isDone, isLate, value1, value2, measurementTypeName));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return measurementReminderEntry;
    }

    public void updateIsDoneMeasurementReminderEntry(int isDone, UUID measurementReminderEntryID, String newTime, double value1, double value2, int type){
        ContentValues measurementReminderEntryTableValues = new ContentValues();
        measurementReminderEntryTableValues.put("is_done", isDone);
        if (!newTime.equals(""))
            measurementReminderEntryTableValues.put("reminder_time", newTime);
        if (type==0){
            measurementReminderEntryTableValues.put("value1", value1);
            measurementReminderEntryTableValues.put("value2", value2);
        }
        database.update("measurement_reminder_entries", measurementReminderEntryTableValues,
                "_id_measur_remind_entry=X'" + measurementReminderEntryID.toString().replace("-", "") + "'", null);
    }
    */
    //***********************************end************************************************************

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_pill_reminders);
    }

}
