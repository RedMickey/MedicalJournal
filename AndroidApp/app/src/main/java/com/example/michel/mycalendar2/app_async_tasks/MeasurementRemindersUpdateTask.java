package com.example.michel.mycalendar2.app_async_tasks;

import android.app.AlarmManager;
import android.content.Context;
import android.os.AsyncTask;

import com.example.michel.mycalendar2.app_async_tasks.synchronization.SynchronizationMeasurementReminderTask;
import com.example.michel.mycalendar2.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.models.CycleAndMeasurementComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderDBEntry;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class MeasurementRemindersUpdateTask extends AsyncTask<CycleAndMeasurementComby, Void, Void> {
    //private boolean needUpdateMeasurement;
    private Context appContext;

    public MeasurementRemindersUpdateTask(Context context){
        super();
        //this.needUpdateMeasurement = needUpdateMeasurement;
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

        cycleDao.updateCycle(cdbie.getIdCycle(), cdbie.getIdWeekSchedule(),
                cdbie.getPeriod(), cdbie.getPeriodDMType(),
                cdbie.getOnce_aPeriod(), cdbie.getOnce_aPeriodDMType(),
                cdbie.getIdCyclingType(), cdbie.getWeekSchedule()
        );

        measurementReminderDao.updateMeasurementReminder( mrdbe.getIdMeasurementReminder(),
                mrdbe.getStartDate().getDateString(),
                mrdbe.getIdCycle(), mrdbe.getIdHavingMealsType(),
                mrdbe.getHavingMealsTime(), mrdbe.getAnnotation(),
                mrdbe.getIsActive(), mrdbe.getReminderTimes().length
        );

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String curDate = sdf.format(cal.getTime());

        List<MeasurementReminderEntry> todayMeasurementReminderEntriesOld = measurementReminderDao.getMeasurementReminderEntriesByDateAndMeasurementReminder(
                mrdbe.getIdMeasurementReminder(),
                new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
        ));

        cal.add(Calendar.DAY_OF_MONTH,1);
        List<MeasurementReminderEntry> tomorrowMeasurementReminderEntriesOld = measurementReminderDao.getMeasurementReminderEntriesByDateAndMeasurementReminder(
                mrdbe.getIdMeasurementReminder(),
                new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
        ));

        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(ALARM_SERVICE);
        MeasurementNotificationsCreationTask.cancelAlarms(appContext, todayMeasurementReminderEntriesOld, alarmManager);
        MeasurementNotificationsCreationTask.cancelAlarms(appContext, tomorrowMeasurementReminderEntriesOld, alarmManager);

        cal.add(Calendar.DAY_OF_MONTH, -1);
        measurementReminderDao.deleteMeasurementReminderEntriesAfterDate(mrdbe.getIdMeasurementReminder(), curDate);
        // need check for change start date

        reminderTimeDao.deleteReminderTimeByReminderId(mrdbe.getIdMeasurementReminder(),1);

        for (int i=0; i<mrdbe.getReminderTimes().length; i++){
            reminderTimeDao.insertReminderTime(mrdbe.getReminderTimes()[i].getReminderTimeStr(), mrdbe.getIdMeasurementReminder(), 1);
        }

        if (mrdbe.getIsActive()==1) {
            switch (cdbie.getIdCyclingType()){
                case 1:
                    for(int i=0; i<cdbie.getDayCount();i++){
                        for (int j=0; j<mrdbe.getReminderTimes().length; j++){
                            measurementReminderDao.insertMeasurementReminderEntry(
                                    sdf.format(new Date(cal.getTimeInMillis())),
                                    mrdbe.getIdMeasurementReminder(),
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
                                        mrdbe.getIdMeasurementReminder(),
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
                                    mrdbe.getIdMeasurementReminder(),
                                    mrdbe.getReminderTimes()[j].getReminderTimeStr()
                            );
                        }
                        cal.add(Calendar.DATE, cdbie.getDayInterval());
                    }
                    break;
            }
        }

        cal = Calendar.getInstance();
        List<MeasurementReminderEntry> todayMeasurementReminderEntriesNew = measurementReminderDao.getMeasurementReminderEntriesByDateAndMeasurementReminder(
                mrdbe.getIdMeasurementReminder(),
                new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
                ));

        cal.add(Calendar.DAY_OF_MONTH,1);
        List<MeasurementReminderEntry> tomorrowMeasurementReminderEntriesNew = measurementReminderDao.getMeasurementReminderEntriesByDateAndMeasurementReminder(
                mrdbe.getIdMeasurementReminder(),
                new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
                ));

        MeasurementNotificationsCreationTask.setupAlarms(appContext, todayMeasurementReminderEntriesNew, alarmManager);
        MeasurementNotificationsCreationTask.setupAlarms(appContext, tomorrowMeasurementReminderEntriesNew, alarmManager);

        dbAdapter.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        /*MeasurementNotificationsCreationTask mnct = new MeasurementNotificationsCreationTask();
        mnct.execute(appContext);*/
        if (AccountGeneralUtils.curUser.getId()!=1){
            SynchronizationMeasurementReminderTask synchronizationMeasurementReminderTask = new
                    SynchronizationMeasurementReminderTask(appContext, 2);
            synchronizationMeasurementReminderTask.execute();
        }
    }
}
