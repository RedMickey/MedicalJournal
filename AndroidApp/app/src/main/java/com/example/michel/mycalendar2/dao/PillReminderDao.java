package com.example.michel.mycalendar2.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.models.pill.PillReminder;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;
import com.example.michel.mycalendar2.utils.ConvertingUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class PillReminderDao {
    private SQLiteDatabase database;

    public PillReminderDao(SQLiteDatabase database){
        this.database = database;
    }

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
        pillReminderTableValues.put("_id_user", AccountGeneralUtils.curUser.getId());
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
        pillReminderTableValues.put("change_type", 2);
        database.update("pill_reminders", pillReminderTableValues, "lower(hex(_id_pill_reminder)) = ?",
                new String[]{idPillReminder.toString().replace("-", "")});

        //Log.i("pill_new_id", String.valueOf(pillId));
        return idPillReminder;
    }

    public List<PillReminder> getAllPillReminders(){
        List<PillReminder> pillReminders = new ArrayList<>();
        String userIdStr = String.valueOf(AccountGeneralUtils.curUser.getId());
        String rawQuery = "select pr._id_pill_reminder, pr._id_having_meals_type, pr.pill_count, pct.type_name, pi.pill_name, pr.start_date, pr.IsActive, cl.period, pr.times_a_day, "+
                "cl.period_DM_type,(select COUNT(*) from pill_reminder_entries pre where pre._id_pill_reminder=pr._id_pill_reminder and pre.is_done=0 and pr._id_user=?) as count_left "+
                "from pill_reminders pr inner join pills pi on pi._id_pill=pr._id_pill inner join pill_count_types pct on pr._id_pill_count_type=pct._id_pill_count_type "+
                "inner join cycles cl on pr._id_cycle=cl._id_cycle where pr._id_user=? ORDER BY pr.IsActive DESC, pi.pill_name";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{userIdStr, userIdStr});
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

    public PillReminderDBInsertEntry getPillReminderDBInsertEntryByID(UUID prID){
        PillReminderDBInsertEntry prdbie = null;
        String uuidStr = prID.toString().replace("-", "");
        String rawQuery = "select pr._id_pill_reminder, pi.pill_name, pr.pill_count, pr._id_pill_count_type, pr.start_date, pr._id_having_meals_type, pr.having_meals_time," +
                " pr._id_cycle, pr.IsActive, pr.times_a_day, pr.annotation" +
                " from pill_reminders pr inner join pills pi on pi._id_pill=pr._id_pill where pr._id_user=? and pr._id_pill_reminder=X'"+uuidStr+"'";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{String.valueOf(AccountGeneralUtils.curUser.getId())});
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
                " from pill_reminders pr inner join pills pi on pi._id_pill=pr._id_pill inner join cycles cl on pr._id_cycle=cl._id_cycle  where pr._id_user=? and pr._id_pill_reminder=X'"+uuidStr+"'";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{String.valueOf(AccountGeneralUtils.curUser.getId())});
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
                ReminderTimeDao reminderTimeDao = new ReminderTimeDao(database);
                ReminderTime[] reminderTimes = reminderTimeDao.getReminderEntriesTime(idPillReminder, startDateStr,0);

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
                    CycleDao cycleDao = new CycleDao(database);
                    idWeekSchedule = ConvertingUtils.convertBytesToUUID(
                            cursor.getBlob(cursor.getColumnIndex("_id_week_schedule")));
                    weekSchedule = cycleDao.getWeekSchedule(idWeekSchedule);
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
                " pr._id_pill_count_type=pct._id_pill_count_type where pre.reminder_date=? and (pr.IsActive=1 or pre.is_done=1) and pr._id_user=? ORDER BY pre.is_done";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{date.getDateString(), String.valueOf(AccountGeneralUtils.curUser.getId())});
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
                " pr._id_pill_count_type=pct._id_pill_count_type where pre.reminder_date=? and pre._id_pill_reminder =X'" + uuidStr + "' and (pr.IsActive=1 or pre.is_done=1) and pr._id_user=? ORDER BY pre.is_done";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{date.getDateString(), String.valueOf(AccountGeneralUtils.curUser.getId())});
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
        String userIdStr = String.valueOf(AccountGeneralUtils.curUser.getId());
        Cursor cursor;
        if (param==0){
            String rawQuery = "select pre._id_pill_reminder_entry, pre.is_done, pr._id_having_meals_type, pre.reminder_time, pr.having_meals_time, pr.pill_count, pct.type_name, pre.reminder_date, pi.pill_name" +
                    " from pill_reminder_entries pre inner join pill_reminders pr on pre._id_pill_reminder=pr._id_pill_reminder inner join pills pi on pi._id_pill=pr._id_pill inner join pill_count_types pct on" +
                    " pr._id_pill_count_type=pct._id_pill_count_type where pre.reminder_date between ? and ? and (pr.IsActive=1 or pre.is_done=1) and pr._id_user=?" +
                    " ORDER BY pre.reminder_date DESC, pre.reminder_time DESC";
            cursor = database.rawQuery(rawQuery, new String[]{date1.getDateString(), date2.getDateString(), userIdStr});
        }
        else {
            String rawQuery = "select pre._id_pill_reminder_entry, pre.is_done, pr._id_having_meals_type, pre.reminder_time, pr.having_meals_time, pr.pill_count, pct.type_name, pre.reminder_date, pi.pill_name" +
                    " from pill_reminder_entries pre inner join pill_reminders pr on pre._id_pill_reminder=pr._id_pill_reminder inner join pills pi on pi._id_pill=pr._id_pill inner join pill_count_types pct on" +
                    " pr._id_pill_count_type=pct._id_pill_count_type where pre.reminder_date <= ? and (pr.IsActive=1 or pre.is_done=1) and pr._id_user=?" +
                    " ORDER BY pre.reminder_date DESC, pre.reminder_time DESC";
            cursor = database.rawQuery(rawQuery, new String[]{date2.getDateString(), userIdStr});
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
        pillReminderEntryTableValues.put("change_type", 2);
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
}
