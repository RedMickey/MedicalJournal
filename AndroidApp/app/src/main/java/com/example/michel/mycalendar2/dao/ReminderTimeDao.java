package com.example.michel.mycalendar2.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;

import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.models.synchronization.ReminderTimeDB;
import com.example.michel.mycalendar2.utils.ConvertingUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class ReminderTimeDao {
    private SQLiteDatabase database;

    public ReminderTimeDao(SQLiteDatabase database){
        this.database = database;
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
        /*String idStr = String.valueOf(idPillReminder);
        String rawQuery = "select rt._id_reminder_time, rt.reminder_time," +
                " (select count(*) from pill_reminder_entries pre2 where pre2._id_pill_reminder=? and pre2.is_done=1 and pre2._id_reminder_time=rt._id_reminder_time) as is_used" +
                " from reminder_time rt where rt._id_pill_reminder=?";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{idStr, idStr});*/

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

    public void deleteReminderTimeByReminderId(UUID idReminder, int type){
        /*String idPillReminderStr = String.valueOf(idPillReminder);

        database.delete("reminder_time",
                "_id_reminder_time in (" +
                        " select rt._id_reminder_time from reminder_time rt inner join pill_reminders pr on rt._id_pill_reminder=pr._id_pill_reminder" +
                        " where rt._id_pill_reminder = ? and pr.start_date=? and" +
                        " (select count(*) from pill_reminder_entries pre2 where pre2._id_pill_reminder=? and pre2.is_done=1 and pre2._id_reminder_time=rt._id_reminder_time)=0)",
                new String[]{idPillReminderStr, date, idPillReminderStr}
                );*/
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

    public List<ReminderTimeDB> getReminderTimeDBEntriesForSynchronization(Date date){
        ArrayList<ReminderTimeDB> reminderTimeDBList = new ArrayList<>();
        String dateStr = ConvertingUtils.convertDateToString(date);
        String userIdStr = String.valueOf(AccountGeneralUtils.curUser.getId());
        String rawQuery = "select rt._id_reminder_time, rt.reminder_time, rt._id_pill_reminder, rt._id_measurement_reminder, rt.synch_time, rt.change_type " +
                "from reminder_time rt inner join pill_reminders pr on rt._id_pill_reminder=pr._id_pill_reminder " +
                "where rt.synch_time >= ? and pr._id_user=? " +
                "union " +
                "select rt._id_reminder_time, rt.reminder_time, rt._id_pill_reminder, rt._id_measurement_reminder, rt.synch_time, rt.change_type " +
                "from reminder_time rt inner join measurement_reminders mr on rt._id_measurement_reminder=mr._id_measurement_reminder " +
                "where rt.synch_time >= ? and mr._id_user=?";

        Cursor cursor = database.rawQuery(rawQuery, new String[]{dateStr, userIdStr, dateStr, userIdStr});
        if(cursor.moveToFirst()){
            do{
                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(0));
                String reminderTimeStr = cursor.getString(1);
                UUID idPillReminder = null;
                UUID idMeasurementReminder = null;
                try {
                    idPillReminder = ConvertingUtils.convertBytesToUUID(cursor.getBlob(2));
                }
                catch (NullPointerException ex){

                }
                try {
                    idMeasurementReminder = ConvertingUtils.convertBytesToUUID(cursor.getBlob(3));
                }
                catch (NullPointerException ex){

                }
                String synchTimeStr = cursor.getString(4);
                int changeType = cursor.getInt(5);

                reminderTimeDBList.add(new ReminderTimeDB(ConvertingUtils.convertStringToDate(synchTimeStr),
                        changeType, id, ConvertingUtils.convertStringToTime(reminderTimeStr),
                        idPillReminder, idMeasurementReminder));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return reminderTimeDBList;
    }

    //************************************************************Bebore_deletion************************************************************
    public void updateReminderTimeBeforeDeletionByReminderId(UUID idReminder, int type){
        ContentValues reminderTimeTableValues = new ContentValues();
        reminderTimeTableValues.put("change_type", 3);
        if (type==0)
            database.update("reminder_time", reminderTimeTableValues,"lower(hex(_id_pill_reminder)) = ?",
                    new String[]{idReminder.toString().replace("-", "")});
        else
            database.update("reminder_time", reminderTimeTableValues,"lower(hex(_id_measurement_reminder)) = ?",
                    new String[]{idReminder.toString().replace("-", "")});

    }

}
