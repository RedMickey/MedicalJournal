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
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.PillReminder;
import com.example.michel.mycalendar2.models.PillReminderDBInsertEntry;
import com.example.michel.mycalendar2.models.PillReminderEntry;
import com.example.michel.mycalendar2.models.ReminderTime;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
    public void updateIsDonePillReminderEntry(int isDone, int pillReminderEntryID, String newTime){
        ContentValues pillReminderEntryTableValues = new ContentValues();
        pillReminderEntryTableValues.put("is_done", isDone);
        if (!newTime.equals(""))
            pillReminderEntryTableValues.put("reminder_time", newTime);
        database.update("pill_reminder_entries", pillReminderEntryTableValues,
                "_id_pill_reminder_entry=" + String.valueOf(pillReminderEntryID), null);
    }

    public void deletePillReminderEntriesAfterDate(int idPillReminder, String date){
        database.delete("pill_reminder_entries", "reminder_date >= ? and _id_pill_reminder = ? and is_done = 0",
                new String[]{date, String.valueOf(idPillReminder)});
    }

    public void deletePillReminderEntriesByPillReminderId(int idPillReminder){
        database.delete("pill_reminder_entries", "_id_pill_reminder = ?",
                new String[]{String.valueOf(idPillReminder)});
    }

    public void deletePillReminderById(int idPillReminder){
        database.delete("pill_reminders", "_id_pill_reminder = ?",
                new String[]{String.valueOf(idPillReminder)});
    }
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
    public void deleteWeekScheduleByIdCascade(int idWeekSchedule){
        database.delete("week_schedules", "_id_week_schedule = ?",
                new String[]{String.valueOf(idWeekSchedule)});
    }

    public void deleteCycleByIdCascade(int idCycle){
        database.delete("cycles", "_id_cycle = ?",
                new String[]{String.valueOf(idCycle)});
    }

    public void deleteReminderTimeByPillReminderId(int idPillReminder){
        /*String idPillReminderStr = String.valueOf(idPillReminder);

        database.delete("reminder_time",
                "_id_reminder_time in (" +
                        " select rt._id_reminder_time from reminder_time rt inner join pill_reminders pr on rt._id_pill_reminder=pr._id_pill_reminder" +
                        " where rt._id_pill_reminder = ? and pr.start_date=? and" +
                        " (select count(*) from pill_reminder_entries pre2 where pre2._id_pill_reminder=? and pre2.is_done=1 and pre2._id_reminder_time=rt._id_reminder_time)=0)",
                new String[]{idPillReminderStr, date, idPillReminderStr}
                );*/
        database.delete("reminder_time", "_id_pill_reminder = ?", new String[]{String.valueOf(idPillReminder)});
    }

    public void insertPillReminderEntries(String reminder_date, Integer idPillReminder, String reminderTime){
        ContentValues pillReminderEntryTableValues = new ContentValues();
        pillReminderEntryTableValues.put("is_done", 0);
        pillReminderEntryTableValues.put("reminder_date", reminder_date);
        pillReminderEntryTableValues.put("_id_pill_reminder", idPillReminder);
        pillReminderEntryTableValues.put("reminder_time", reminderTime);
        database.insert("pill_reminder_entries", null, pillReminderEntryTableValues);
    }

    public int insertReminderTime(String reminderTime, Integer reminderId, Integer reminderType){
        int reminderTimeId = 0;
        ContentValues reminderTimeValues = new ContentValues();
        reminderTimeValues.put("reminder_time", reminderTime);
        switch (reminderType){
            case 0:
                reminderTimeValues.put("_id_pill_reminder", reminderId);
                break;
            case 1:
                reminderTimeValues.put("_id_measurement_reminder", reminderId);
                break;
        }
        reminderTimeId = (int)database.insert("reminder_time", null, reminderTimeValues);

        return reminderTimeId;
    }

    public int insertPillReminder(String pillName, Integer pillCount, Integer idPillCountType,
                                   String startDate, Integer idCycle, @Nullable Integer idHavingMealsType,
                                   @Nullable Integer havingMealsTime, String annotation, Integer isActive, Integer times_aDay){

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
        pillReminderTableValues.put("times_a_day", times_aDay);
        long pillReminderId = database.insert("pill_reminders", null, pillReminderTableValues);

        //Log.i("pill_new_id", String.valueOf(pillId));
        return (int)pillReminderId;
    }

    public int updatePillReminder(Integer idPillReminder, String pillName, Integer pillCount, Integer idPillCountType,
                                  String startDate, Integer idCycle, @Nullable Integer idHavingMealsType,
                                  @Nullable Integer havingMealsTime, String annotation, Integer isActive, Integer times_aDay,
                                  boolean needUpdatePill){
        long pillId = -1;
        if (needUpdatePill){
            ContentValues pillTableValues = new ContentValues();
            pillTableValues.put("pill_name", pillName);
            pillTableValues.put("pill_description", "");
            pillId = database.insert("pills", null, pillTableValues);
        }

        ContentValues pillReminderTableValues = new ContentValues();
        if (needUpdatePill)
            pillReminderTableValues.put("_id_pill", pillId);
        pillReminderTableValues.put("pill_count", pillCount);
        pillReminderTableValues.put("_id_pill_count_type", idPillCountType);
        pillReminderTableValues.put("start_date", startDate);
        pillReminderTableValues.put("_id_cycle", idCycle);
        pillReminderTableValues.put("_id_having_meals_type", idHavingMealsType);
        pillReminderTableValues.put("having_meals_time", havingMealsTime);
        pillReminderTableValues.put("annotation", annotation);
        pillReminderTableValues.put("IsActive", isActive);
        pillReminderTableValues.put("times_a_day", times_aDay);
        long pillReminderId = database.update("pill_reminders", pillReminderTableValues,
                "_id_pill_reminder = ?", new String[]{String.valueOf(idPillReminder)});

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
            weekScheduleTableValues.put("mon", weekSchedule[1]);
            weekScheduleTableValues.put("tue", weekSchedule[2]);
            weekScheduleTableValues.put("wed", weekSchedule[3]);
            weekScheduleTableValues.put("thu", weekSchedule[4]);
            weekScheduleTableValues.put("fri", weekSchedule[5]);
            weekScheduleTableValues.put("sat", weekSchedule[6]);
            weekScheduleTableValues.put("sun", weekSchedule[0]);
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

    public int updateCycle(Integer inCycleId, Integer weekScheduleID, Integer period, Integer periodDMType, @Nullable Integer once_aPeriod,
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
            if (weekScheduleID!=0) {
                database.update("week_schedules", weekScheduleTableValues,
                        "_id_week_schedule = ?", new String[]{String.valueOf(weekScheduleID)});
            }
            else
                weekScheduleID = (int)database.insert("week_schedules", null, weekScheduleTableValues);
        }

        ContentValues cycleTableValues = new ContentValues();
        cycleTableValues.put("period", period);
        cycleTableValues.put("period_DM_type", periodDMType);
        cycleTableValues.put("once_a_period", once_aPeriod);
        cycleTableValues.put("once_a_period_DM_type", once_aPeriodDMType);
        cycleTableValues.put("_id_week_schedule", weekScheduleID==0?null:weekScheduleID);
        cycleTableValues.put("_id_cycling_type", idCyclingType);
        long cycleId = database.update("cycles", cycleTableValues,
                "_id_cycle = ?", new String[]{String.valueOf(inCycleId)});

        return (int)cycleId;
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

                int id = cursor.getInt(cursor.getColumnIndex("_id_pill_reminder"));
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

    private int[] getWeekSchedule(int idWeekSchedule){
        int[] weekSchedule = new int[7];
        Cursor cursor = database.query("week_schedules", null, "_id_week_schedule=?", new String[]{String.valueOf(idWeekSchedule)}, null, null, null);
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

    private ReminderTime[] getPillReminderEntriesTime(int idPillReminder, String startDate){
        List<ReminderTime> pillReminderEntriesTime = new ArrayList<>();
        String rawQuery = "select rt._id_reminder_time, rt.reminder_time from reminder_time rt where rt._id_pill_reminder=?";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{String.valueOf(idPillReminder)});
        /*String idStr = String.valueOf(idPillReminder);
        String rawQuery = "select rt._id_reminder_time, rt.reminder_time," +
                " (select count(*) from pill_reminder_entries pre2 where pre2._id_pill_reminder=? and pre2.is_done=1 and pre2._id_reminder_time=rt._id_reminder_time) as is_used" +
                " from reminder_time rt where rt._id_pill_reminder=?";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{idStr, idStr});*/

        if(cursor.moveToFirst()){
            do{
                pillReminderEntriesTime.add(
                        new ReminderTime(
                                cursor.getInt(cursor.getColumnIndex("_id_reminder_time")),
                                (cursor.getString(cursor.getColumnIndex("reminder_time"))).substring(0,5)
                                )
                                //cursor.getInt(cursor.getColumnIndex("is_used"))>0?true:false)
                );
                //pillReminderEntriesTime.add((cursor.getString(cursor.getColumnIndex("reminder_time"))).substring(0,5));
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        return pillReminderEntriesTime.toArray(new ReminderTime[pillReminderEntriesTime.size()]);
    }

    public CycleAndPillComby getCycleAndPillCombyByID(int prID){
        String rawQuery = "select pr._id_pill_reminder, pi.pill_name, pr.pill_count, pr._id_pill_count_type, pr.start_date, pr._id_having_meals_type, pr.having_meals_time, pr.annotation, pr.IsActive, pr.times_a_day," +
                " pr._id_cycle, cl.period, cl.period_DM_type, cl.once_a_period, cl.once_a_period_DM_type, cl._id_cycling_type, cl._id_week_schedule" +
                " from pill_reminders pr inner join pills pi on pi._id_pill=pr._id_pill inner join cycles cl on pr._id_cycle=cl._id_cycle  where pr._id_pill_reminder=?";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{String.valueOf(prID)});
        CycleAndPillComby cycleAndPillComby = new CycleAndPillComby();
        if(cursor.moveToFirst()){
            do{
                int idPillReminder = cursor.getInt(cursor.getColumnIndex("_id_pill_reminder"));
                String pillName = cursor.getString(cursor.getColumnIndex("pill_name"));
                int pillCount = cursor.getInt(cursor.getColumnIndex("pill_count"));
                int idPillCountType = cursor.getInt(cursor.getColumnIndex("_id_pill_count_type"));
                int idCycle = cursor.getInt(cursor.getColumnIndex("_id_cycle"));
                String startDateStr = cursor.getString(cursor.getColumnIndex("start_date"));
                int idHavingMealsType = cursor.getInt(cursor.getColumnIndex("_id_having_meals_type"));
                int havingMealsTime = cursor.getInt(cursor.getColumnIndex("having_meals_time"));
                String annotation = cursor.getString(cursor.getColumnIndex("annotation"));
                int isActive = cursor.getInt(cursor.getColumnIndex("IsActive"));
                int times_aDay = cursor.getInt(cursor.getColumnIndex("times_a_day"));  // may be deleted
                ReminderTime[] reminderTimes = getPillReminderEntriesTime(idPillReminder, startDateStr);

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
                int idWeekSchedule = cursor.getInt(cursor.getColumnIndex("_id_week_schedule"));
                int[] weekSchedule = getWeekSchedule(idWeekSchedule);

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
                int id = cursor.getInt(cursor.getColumnIndex("_id_pill_reminder_entry"));
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

    public long getCount(){
        return DatabaseUtils.queryNumEntries(database, DatabaseHelper.TABLE_pill_reminders);
    }

}
