package com.example.michel.mycalendar2.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.example.michel.mycalendar2.utils.ConvertingUtils;

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
}
