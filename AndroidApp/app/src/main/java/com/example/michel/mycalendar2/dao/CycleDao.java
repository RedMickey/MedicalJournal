package com.example.michel.mycalendar2.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.models.synchronization.CycleDB;
import com.example.michel.mycalendar2.models.synchronization.WeekScheduleDB;
import com.example.michel.mycalendar2.utils.ConvertingUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CycleDao {
    private SQLiteDatabase database;

    public CycleDao(SQLiteDatabase database){
        this.database = database;
    }

    public int[] getWeekSchedule(UUID idWeekSchedule){
        int[] weekSchedule = new int[7];
        String uuidStr = idWeekSchedule.toString().replace("-", "");

        String rawQuery = "select * from week_schedules where _id_week_schedule = X'"+uuidStr+"'";
        Cursor cursor = database.rawQuery(rawQuery, null);

        /*String rawQuery = "select * from week_schedules where lower(hex(_id_week_schedule)) = ?";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{uuidStr});*/

        /*Cursor cursor = database.query("week_schedules", null, "_id_week_schedule=?",
                new String[]{"X'" + sb.toString()+"'"}, null, null, null);*/
        if(cursor.moveToFirst()){
            do{
                weekSchedule[0] = cursor.getInt(cursor.getColumnIndex("mon"));
                weekSchedule[1] = cursor.getInt(cursor.getColumnIndex("tue"));
                weekSchedule[2] = cursor.getInt(cursor.getColumnIndex("wed"));
                weekSchedule[3] = cursor.getInt(cursor.getColumnIndex("thu"));
                weekSchedule[4] = cursor.getInt(cursor.getColumnIndex("fri"));
                weekSchedule[5] = cursor.getInt(cursor.getColumnIndex("sat"));
                weekSchedule[6] = cursor.getInt(cursor.getColumnIndex("sun"));
                String f = cursor.getString(cursor.getColumnIndex("synch_time"));
                String f3 = cursor.getString(cursor.getColumnIndex("synch_time"));
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
                weekScheduleTableValues.put("change_type", 2);
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
        cycleTableValues.put("change_type", 2);
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

    public List<CycleDB> getCycleDBEntriesForSynchronization(Date date){
        ArrayList<CycleDB> cycleDBArrayList = new ArrayList<>();
        String dateStr = ConvertingUtils.convertDateToString(date);
        String userIdStr = String.valueOf(AccountGeneralUtils.curUser.getId());
        String rawQuery = "select cl._id_cycle, cl.period, cl.period_DM_type, cl.once_a_period, cl.once_a_period_DM_type, cl._id_week_schedule, " +
                "cl._id_cycling_type, cl.change_type, cl.synch_time " +
                "from cycles cl inner join pill_reminders pr on cl._id_cycle=pr._id_cycle " +
                "where cl.synch_time >= ? and pr._id_user=? " +
                "union " +
                "select cl._id_cycle, cl.period, cl.period_DM_type, cl.once_a_period, cl.once_a_period_DM_type, cl._id_week_schedule, " +
                "cl._id_cycling_type, cl.change_type, cl.synch_time " +
                "from cycles cl inner join measurement_reminders mr on cl._id_cycle=mr._id_cycle " +
                "where cl.synch_time >= ? and mr._id_user=?";

        Cursor cursor = database.rawQuery(rawQuery, new String[]{dateStr, userIdStr, dateStr, userIdStr});
        /*Cursor cursor = database.query("cycles", null,
                "synch_time >= ?",
                new String[] {ConvertingUtils.convertDateToString(date)},
                null, null, null);*/
        if(cursor.moveToFirst()){
            do{
                /*UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_cycle")));
                int period = cursor.getInt(cursor.getColumnIndex("period"));
                int periodDMType = cursor.getInt(cursor.getColumnIndex("period_DM_type"));
                int onceAPeriod = cursor.getInt(cursor.getColumnIndex("once_a_period"));
                int onceAPeriodDMType = cursor.getInt(cursor.getColumnIndex("once_a_period_DM_type"));
                UUID idWeekSchedule = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_week_schedule")));
                int idCyclingType = cursor.getInt(cursor.getColumnIndex("_id_cycling_type"));
                int changeType = cursor.getInt(cursor.getColumnIndex("change_type"));
                String synchTimeStr = cursor.getString(cursor.getColumnIndex("synch_time"));*/

                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(0));
                int period = cursor.getInt(1);
                int periodDMType = cursor.getInt(2);
                int onceAPeriod = cursor.getInt(3);
                int onceAPeriodDMType = cursor.getInt(4);
                UUID idWeekSchedule = null;
                try {
                    idWeekSchedule = ConvertingUtils.convertBytesToUUID(cursor.getBlob(5));
                }
                catch (NullPointerException ex){

                }
                int idCyclingType = cursor.getInt(6);
                int changeType = cursor.getInt(7);
                String synchTimeStr = cursor.getString(8);

                cycleDBArrayList.add(new CycleDB(ConvertingUtils.convertStringToDate(synchTimeStr),
                        changeType, id, period, periodDMType, onceAPeriod>0?onceAPeriod:null,
                        onceAPeriodDMType>0?onceAPeriodDMType:null, idWeekSchedule, idCyclingType));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return cycleDBArrayList;
    }

    public List<WeekScheduleDB> getWeekScheduleDBEntriesForSynchronization(Date date){
        ArrayList<WeekScheduleDB> weekScheduleDBArrayList = new ArrayList<>();
        String dateStr = ConvertingUtils.convertDateToString(date);
        String userIdStr = String.valueOf(AccountGeneralUtils.curUser.getId());
        String rawQuery = "select ws.mon, ws.tue, ws.wed, ws.thu, ws.fri, ws.sat, ws.sun, ws._id_week_schedule, ws.change_type, ws.synch_time " +
                "from week_schedules ws inner join cycles cl on ws._id_week_schedule=cl._id_week_schedule inner join pill_reminders pr on cl._id_cycle=pr._id_cycle " +
                "where ws.synch_time >= ? and pr._id_user=? " +
                "union " +
                "select ws.mon, ws.tue, ws.wed, ws.thu, ws.fri, ws.sat, ws.sun, ws._id_week_schedule, ws.change_type, ws.synch_time " +
                "from week_schedules ws inner join cycles cl on ws._id_week_schedule=cl._id_week_schedule inner join measurement_reminders mr on cl._id_cycle=mr._id_cycle " +
                "where ws.synch_time >= ? and mr._id_user=?";

        Cursor cursor = database.rawQuery(rawQuery, new String[]{dateStr, userIdStr, dateStr, userIdStr});
        /*Cursor cursor = database.query("week_schedules", null,
                "synch_time >= ?",
                new String[] {ConvertingUtils.convertDateToString(date)},
                null, null, null);*/
        int[] weekSchedule = new int[7];
        if(cursor.moveToFirst()){
            do{
                /*weekSchedule[0] = cursor.getInt(cursor.getColumnIndex("mon"));
                weekSchedule[1] = cursor.getInt(cursor.getColumnIndex("tue"));
                weekSchedule[2] = cursor.getInt(cursor.getColumnIndex("wed"));
                weekSchedule[3] = cursor.getInt(cursor.getColumnIndex("thu"));
                weekSchedule[4] = cursor.getInt(cursor.getColumnIndex("fri"));
                weekSchedule[5] = cursor.getInt(cursor.getColumnIndex("sat"));
                weekSchedule[6] = cursor.getInt(cursor.getColumnIndex("sun"));
                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(cursor.getColumnIndex("_id_week_schedule")));
                int changeType = cursor.getInt(cursor.getColumnIndex("change_type"));
                String synchTimeStr = cursor.getString(cursor.getColumnIndex("synch_time"));*/

                weekSchedule[0] = cursor.getInt(0);
                weekSchedule[1] = cursor.getInt(1);
                weekSchedule[2] = cursor.getInt(2);
                weekSchedule[3] = cursor.getInt(3);
                weekSchedule[4] = cursor.getInt(4);
                weekSchedule[5] = cursor.getInt(5);
                weekSchedule[6] = cursor.getInt(6);
                UUID id = ConvertingUtils.convertBytesToUUID(cursor.getBlob(7));
                int changeType = cursor.getInt(8);
                String synchTimeStr = cursor.getString(9);

                weekScheduleDBArrayList.add(new WeekScheduleDB(ConvertingUtils.convertStringToDate(synchTimeStr),
                        changeType, id, weekSchedule[0], weekSchedule[1], weekSchedule[2],
                        weekSchedule[3], weekSchedule[4], weekSchedule[5], weekSchedule[6]));
            }
            while (cursor.moveToNext());
        }
        cursor.close();

        return weekScheduleDBArrayList;
    }

    //************************************************************Bebore_deletion************************************************************
    public UUID updateCycleBeforeDeletion(UUID cycleId){
        ContentValues cycleTableValues = new ContentValues();
        cycleTableValues.put("change_type", 3);
        database.update("cycles", cycleTableValues, "lower(hex(_id_cycle)) = ?",
                new String[]{cycleId.toString().replace("-", "")});
        return cycleId;
    }

    public UUID updateWeekScheduleBeforeDeletion(UUID weekScheduleID){
        ContentValues weekScheduleTableValues = new ContentValues();
        weekScheduleTableValues.put("change_type", 3);
        database.update("week_schedules", weekScheduleTableValues,
                "lower(hex(_id_week_schedule)) = ?", new String[]{weekScheduleID.toString().replace("-", "")});
        return weekScheduleID;
    }
}
