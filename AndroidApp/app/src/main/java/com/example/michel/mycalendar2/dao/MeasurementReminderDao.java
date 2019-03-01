package com.example.michel.mycalendar2.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.models.CycleAndMeasurementComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminder;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderDBEntry;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.models.measurement.MeasurementStatEntry;
import com.example.michel.mycalendar2.utils.ConvertingUtils;
import com.example.michel.mycalendar2.utils.DBStaticEntries;
import com.example.michel.mycalendar2.utils.utilModels.MeasurementType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MeasurementReminderDao {
    private SQLiteDatabase database;

    public MeasurementReminderDao(SQLiteDatabase database){
        this.database = database;
    }

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
        measurementReminderTableValues.put("_id_user", AccountGeneralUtils.curUser.getId());
        if (isOneTime==1)
            measurementReminderTableValues.put("is_one_time", isOneTime);
        database.insert("measurement_reminders", null, measurementReminderTableValues);

        //Log.i("pill_new_id", String.valueOf(pillId));
        return measurementReminderId;
    }

    public List<MeasurementReminder> getAllMeasurementReminders(){
        List<MeasurementReminder> measurementReminders = new ArrayList<>();
        /*String rawQuery = "select  mr.start_date, mr.isActive, cl.period, mr.times_a_day, mr._id_measurement_type, mr._id_having_meals_type, cl.period_DM_type, " +
                "      CASE mr._id_measurement_type  " +
                "         WHEN 1 THEN (select COUNT(*) from temperature_measurements tm where tm._id_measurement_reminder=mr._id_measurement_reminder and tm.is_done=0 ) " +
                "         WHEN 2 THEN (select COUNT(*) from blood_pressure_measurements bpm where bpm._id_measurement_reminder=mr._id_measurement_reminder and bpm.is_done=0 ) " +
                "      END as count_left, mr._id_measurement_reminder " +
                "    from measurement_reminders mr inner join cycles cl on mr._id_cycle=cl._id_cycle ORDER BY mr.IsActive";*/
        String rawQuery = "select  mr.start_date, mr.isActive, cl.period, mr.times_a_day, mr._id_measurement_type, mr._id_having_meals_type, cl.period_DM_type, " +
                "(select COUNT(*) from measurement_reminder_entries mre where mre._id_measurement_reminder=mr._id_measurement_reminder and mre.is_done=0 and mr._id_user=? ) " +
                "as count_left, mr._id_measurement_reminder, mt._id_measur_value_type " +
                "from measurement_reminders mr inner join cycles cl on mr._id_cycle=cl._id_cycle inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type " +
                "where mr._id_user=? ORDER BY mr.isActive DESC";
        String userIdStr = String.valueOf(AccountGeneralUtils.curUser.getId());
        Cursor cursor = database.rawQuery(rawQuery, new String[]{userIdStr, userIdStr});
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
                    " and mre.reminder_time between ? and ? and mr._id_user=? GROUP BY mre.reminder_date";
            cursor = database.rawQuery(rawQuery, new String[]{String.format("%02d",month), String.valueOf(year),
                    timeStr1, timeStr2, String.valueOf(AccountGeneralUtils.curUser.getId())});
        }
        else if (type == 1){
            String rawQuery = "select AVG(mre.value1) as avg_value1, AVG(mre.value2) as avg_value2, mre.reminder_date" +
                    " from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder" +
                    " where mre._id_measurement_reminder = X'"+uuidStr+"' and mre.is_done = 1 and strftime('%m', mre.reminder_date) = ? and strftime('%Y', mre.reminder_date) = ?" +
                    " and mr._id_user=? GROUP BY mre.reminder_date";
            cursor = database.rawQuery(rawQuery, new String[]{String.format("%02d",month), String.valueOf(year),
                    String.valueOf(AccountGeneralUtils.curUser.getId())});
        }
        else {
            String rawQuery = "select AVG(mre.value1) as avg_value1, AVG(mre.value2) as avg_value2, mre.reminder_date" +
                    " from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder" +
                    " where mre._id_measurement_reminder = X'"+uuidStr+"' and mre.is_done = 1 and strftime('%m', mre.reminder_date) = ? and strftime('%Y', mre.reminder_date) = ?" +
                    " and (mre.reminder_time between ? and '00:00:00' or mre.reminder_time between '00:00:00' and ?) and mr._id_user=? GROUP BY mre.reminder_date";
            cursor = database.rawQuery(rawQuery, new String[]{String.valueOf(month), String.valueOf(year),
                    String.valueOf(AccountGeneralUtils.curUser.getId())});
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
        String userIdStr = String.valueOf(AccountGeneralUtils.curUser.getId());
        if (limit!=-1) {
            String rawQuery = "select mr.start_date, mr.isActive, cl.period, mr.times_a_day, mr._id_measurement_type, mr._id_having_meals_type, cl.period_DM_type, mr._id_measurement_reminder, mt._id_measur_value_type," +
                    " COUNT(sub.id) as count_done, SUM(sub.value1) as sum_value1, SUM(sub.value2) as sum_value2" +
                    " from measurement_reminders mr inner join cycles cl on mr._id_cycle=cl._id_cycle inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type" +
                    " join (" +
                    "    select" +
                    "        mre._id_measurement_reminder as id," +
                    "        mre.value1," +
                    "        mre.value2" +
                    "    from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder where mre.is_done=1" +
                    "    and mr._id_user=?" +
                    " ) sub on sub.id=mr._id_measurement_reminder where mr._id_user=? GROUP BY mr._id_measurement_reminder ORDER BY mr.start_date DESC LIMIT ?";
            cursor = database.rawQuery(rawQuery, new String[]{userIdStr, userIdStr, String.valueOf(limit)});
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
                    "    from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder where mre.is_done=1" +
                    "    and mr._id_user=?" +
                    " ) sub on sub.id=mr._id_measurement_reminder where mr._id_user=? GROUP BY mr._id_measurement_reminder ORDER BY mr.start_date DESC";
            cursor = database.rawQuery(rawQuery, new String[]{userIdStr, userIdStr});
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
                " where mr._id_user=? and mr._id_measurement_reminder=X'"+uuidStr+"'";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{String.valueOf(AccountGeneralUtils.curUser.getId())});
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
                ReminderTimeDao reminderTimeDao = new ReminderTimeDao(database);
                ReminderTime[] reminderTimes = reminderTimeDao.getReminderEntriesTime(idMeasurementReminder, startDateStr, 1);

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
        measurementReminderTableValues.put("change_type", 2);
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
                " inner join measurement_value_types mvt on mt._id_measur_value_type=mvt._id_measur_value_type where mre.reminder_date=? and (mr.IsActive=1 or mre.is_done=1) and mr._id_user=? ORDER BY mre.is_done";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{date.getDateString(), String.valueOf(AccountGeneralUtils.curUser.getId())});
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
                " and (mr.IsActive=1 or mre.is_done=1) and mr._id_user=? ORDER BY mre.is_done";
        Cursor cursor = database.rawQuery(rawQuery, new String[]{date.getDateString(), String.valueOf(AccountGeneralUtils.curUser.getId())});
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
        String userIdStr = String.valueOf(AccountGeneralUtils.curUser.getId());
        if (param==0){
            String rawQuery = "select mre._id_measur_remind_entry, mr._id_measurement_type, mvt.type_value_name, mt.type_name, mre.value1, mre.value2, mre.is_done, mr._id_having_meals_type, mre.reminder_time, mr.having_meals_time, mre.reminder_date" +
                    " from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type" +
                    " inner join measurement_value_types mvt on mt._id_measur_value_type=mvt._id_measur_value_type where mre.reminder_date between ? and ? and (mr.IsActive=1 or mre.is_done=1) and mr._id_user=?" +
                    " ORDER BY mre.reminder_date DESC, mre.reminder_time DESC";
            cursor = database.rawQuery(rawQuery, new String[]{date1.getDateString(), date2.getDateString(),
                    userIdStr});
        }
        else {
            String rawQuery = "select mre._id_measur_remind_entry, mr._id_measurement_type, mvt.type_value_name, mt.type_name, mre.value1, mre.value2, mre.is_done, mr._id_having_meals_type, mre.reminder_time, mr.having_meals_time, mre.reminder_date" +
                    " from measurement_reminder_entries mre inner join measurement_reminders mr on mre._id_measurement_reminder=mr._id_measurement_reminder inner join measurement_types mt on mr._id_measurement_type=mt._id_measurement_type" +
                    " inner join measurement_value_types mvt on mt._id_measur_value_type=mvt._id_measur_value_type where mre.reminder_date <= ? and (mr.IsActive=1 or mre.is_done=1) and mr._id_user=?" +
                    " ORDER BY mre.reminder_date DESC, mre.reminder_time DESC";
            cursor = database.rawQuery(rawQuery, new String[]{date2.getDateString(), userIdStr});
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
        measurementReminderEntryTableValues.put("change_type", 2);
        if (!newTime.equals(""))
            measurementReminderEntryTableValues.put("reminder_time", newTime);
        if (type==0){
            measurementReminderEntryTableValues.put("value1", value1);
            measurementReminderEntryTableValues.put("value2", value2);
        }
        database.update("measurement_reminder_entries", measurementReminderEntryTableValues,
                "_id_measur_remind_entry=X'" + measurementReminderEntryID.toString().replace("-", "") + "'", null);
    }
}
