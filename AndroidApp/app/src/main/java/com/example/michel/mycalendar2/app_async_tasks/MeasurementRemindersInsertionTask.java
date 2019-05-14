package com.example.michel.mycalendar2.app_async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.michel.mycalendar2.app_async_tasks.synchronization.SynchronizationMeasurementReminderTask;
import com.example.michel.mycalendar2.app_async_tasks.synchronization.SynchronizationWeekScheduleTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.models.CycleAndMeasurementComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderDBEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class MeasurementRemindersInsertionTask extends AsyncTask<CycleAndMeasurementComby, Void, Void> {
    private Context appContext;

    public MeasurementRemindersInsertionTask(Context context){
        appContext = context;
    }

    @Override
    protected Void doInBackground(CycleAndMeasurementComby... cycleAndMeasurementCombies) {
        MeasurementReminderDBEntry mrdbe = cycleAndMeasurementCombies[0].measurementReminderDBEntry;
        CycleDBInsertEntry cdbie = cycleAndMeasurementCombies[0].cycleDBInsertEntry;
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(dbAdapter.open().getDatabase());
        CycleDao cycleDao = new CycleDao(dbAdapter.getDatabase());
        ReminderTimeDao reminderTimeDao = new ReminderTimeDao(dbAdapter.getDatabase());

        mrdbe.setIdCycle(
                cycleDao.insertCycle(cdbie.getPeriod(), cdbie.getPeriodDMType(),
                        cdbie.getOnce_aPeriod(), cdbie.getOnce_aPeriodDMType(),
                        cdbie.getIdCyclingType(), cdbie.getWeekSchedule()
                )
        );

        UUID measurementReminderId = measurementReminderDao.insertMeasurementReminder(
                mrdbe.getIdMeasurementType(),
                mrdbe.getStartDate().getDateString(),
                mrdbe.getIdCycle(), mrdbe.getIdHavingMealsType(),
                mrdbe.getHavingMealsTime(), mrdbe.getAnnotation(),
                mrdbe.getIsActive(), mrdbe.getReminderTimes().length, 0,
                mrdbe.getIsGfitListening()
        );

        for (int i=0; i<mrdbe.getReminderTimes().length; i++){
            reminderTimeDao.insertReminderTime(mrdbe.getReminderTimes()[i].getReminderTimeStr(), measurementReminderId, 1);
        }

        Calendar cal = Calendar.getInstance();
        cal.set(mrdbe.getStartDate().getYear(),
                mrdbe.getStartDate().getMonth()-1,
                mrdbe.getStartDate().getDay());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        switch (cdbie.getIdCyclingType()){
            case 1:
                for(int i=0; i<cdbie.getDayCount();i++){
                    for (int j=0; j<mrdbe.getReminderTimes().length; j++){
                        measurementReminderDao.insertMeasurementReminderEntry(
                                sdf.format(new Date(cal.getTimeInMillis())),
                                measurementReminderId,
                                mrdbe.getReminderTimes()[j].getReminderTimeStr()
                        );
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 2:
                for(int i=0; i<cdbie.getDayCount();i++){
                    if(cdbie.getWeekSchedule()[cal.get(Calendar.DAY_OF_WEEK)-1]==1){
                        for (int j=0; j<mrdbe.getReminderTimes().length; j++){
                            measurementReminderDao.insertMeasurementReminderEntry(
                                    sdf.format(cal.getTime()),
                                    measurementReminderId,
                                    mrdbe.getReminderTimes()[j].getReminderTimeStr()
                            );
                        }
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 3:
                for(int i=0; i<cdbie.getDayCount();i+=cdbie.getDayInterval()){
                    for (int j=0; j<mrdbe.getReminderTimes().length; j++){
                        measurementReminderDao.insertMeasurementReminderEntry(
                                sdf.format(new Date(cal.getTimeInMillis())),
                                measurementReminderId,
                                mrdbe.getReminderTimes()[j].getReminderTimeStr()
                        );
                    }
                    cal.add(Calendar.DATE, cdbie.getDayInterval());
                }
                break;
        }

        dbAdapter.close();

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        MeasurementNotificationsCreationTask mnct = new MeasurementNotificationsCreationTask();
        mnct.execute(appContext);
        if (AccountGeneralUtils.curUser.getId()!=1){
            SynchronizationMeasurementReminderTask synchronizationMeasurementReminderTask = new
                    SynchronizationMeasurementReminderTask(appContext);
            synchronizationMeasurementReminderTask.execute();
        }
    }
}
