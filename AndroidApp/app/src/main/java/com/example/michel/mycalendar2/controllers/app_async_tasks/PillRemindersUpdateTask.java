package com.example.michel.mycalendar2.controllers.app_async_tasks;

import android.app.AlarmManager;
import android.content.Context;
import android.os.AsyncTask;

import com.example.michel.mycalendar2.controllers.app_async_tasks.synchronization.SynchronizationPillReminderTask;
import com.example.michel.mycalendar2.services.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.view.custom_views.calendarview.data.DateData;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.Context.ALARM_SERVICE;

public class PillRemindersUpdateTask extends AsyncTask<CycleAndPillComby, Void, Void> {
    private boolean needUpdatePill;
    private Context appContext;

    public PillRemindersUpdateTask(boolean needUpdatePill, Context context){
        super();
        this.needUpdatePill = needUpdatePill;
        appContext = context;
    }

    @Override
    protected Void doInBackground(CycleAndPillComby... cycleAndPillCombies) {
        PillReminderDBInsertEntry pillReminderDBInsertEntry = cycleAndPillCombies[0].pillReminderDBInsertEntry;
        CycleDBInsertEntry cycleDBInsertEntry = cycleAndPillCombies[0].cycleDBInsertEntry;
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        PillReminderDao pillReminderDao = new PillReminderDao(dbAdapter.open().getDatabase());
        CycleDao cycleDao = new CycleDao(dbAdapter.getDatabase());
        ReminderTimeDao reminderTimeDao = new ReminderTimeDao(dbAdapter.getDatabase());

        cycleDao.updateCycle(cycleDBInsertEntry.getIdCycle(), cycleDBInsertEntry.getIdWeekSchedule(),
                cycleDBInsertEntry.getPeriod(), cycleDBInsertEntry.getPeriodDMType(),
                cycleDBInsertEntry.getOnce_aPeriod(), cycleDBInsertEntry.getOnce_aPeriodDMType(),
                cycleDBInsertEntry.getIdCyclingType(), cycleDBInsertEntry.getWeekSchedule()
        );

        pillReminderDao.updatePillReminder( pillReminderDBInsertEntry.getIdPillReminder(),
                pillReminderDBInsertEntry.getPillName(), pillReminderDBInsertEntry.getPillCount(),
                pillReminderDBInsertEntry.getIdPillCountType(), pillReminderDBInsertEntry.getStartDate().getDateString(),
                pillReminderDBInsertEntry.getIdCycle(), pillReminderDBInsertEntry.getIdHavingMealsType(),
                pillReminderDBInsertEntry.getHavingMealsTime(), pillReminderDBInsertEntry.getAnnotation(),
                pillReminderDBInsertEntry.getIsActive(), pillReminderDBInsertEntry.getReminderTimes().length,
                needUpdatePill
        );

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String curDate = sdf.format(cal.getTime());

        List<PillReminderEntry> todayPillReminderEntriesOld = pillReminderDao.getPillReminderEntriesByDateAndPillReminder(
                pillReminderDBInsertEntry.getIdPillReminder(),
                new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
        ));

        cal.add(Calendar.DAY_OF_MONTH,1);
        List<PillReminderEntry> tomorrowPillReminderEntriesOld = pillReminderDao.getPillReminderEntriesByDateAndPillReminder(
                pillReminderDBInsertEntry.getIdPillReminder(),
                new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
        ));

        AlarmManager alarmManager = (AlarmManager) appContext.getSystemService(ALARM_SERVICE);
        PillNotificationsCreationTask.cancelAlarms(appContext, todayPillReminderEntriesOld, alarmManager);
        PillNotificationsCreationTask.cancelAlarms(appContext, tomorrowPillReminderEntriesOld, alarmManager);

        cal.add(Calendar.DAY_OF_MONTH, -1);

        if (AccountGeneralUtils.curUser.getId()==1){
            pillReminderDao.deletePillReminderEntriesAfterDate(pillReminderDBInsertEntry.getIdPillReminder(), curDate);
            reminderTimeDao.deleteReminderTimeByReminderId(pillReminderDBInsertEntry.getIdPillReminder(),0);
        }
        else {
            pillReminderDao.updateBeforeDeletionPillReminderEntriesAfterDate(pillReminderDBInsertEntry.getIdPillReminder(), curDate);
            reminderTimeDao.updateBeforeDeletionReminderTimeByReminderId(pillReminderDBInsertEntry.getIdPillReminder(),0);
        }

        for (int i=0; i<pillReminderDBInsertEntry.getReminderTimes().length; i++){
            reminderTimeDao.insertReminderTime(pillReminderDBInsertEntry.getReminderTimes()[i].getReminderTimeStr(), pillReminderDBInsertEntry.getIdPillReminder(), 0);
        }
        //dbAdapter.deleteIrrelevantReminderTime(pillReminderDBInsertEntry.getIdPillReminder(), pillReminderDBInsertEntry.getStartDate().getDateString());
        /*int[] reminderTimeIds = new int[pillReminderDBInsertEntry.getReminderTimes().length];
        for (int i=0; i<reminderTimeIds.length; i++){
            if (pillReminderDBInsertEntry.getReminderTimes()[i].getIdReminderTime()==0||!pillReminderDBInsertEntry.getReminderTimes()[i].isUsed())
                reminderTimeIds[i] = dbAdapter.insertReminderTime(pillReminderDBInsertEntry.getReminderTimes()[i].getReminderTimeStr()+":00", pillReminderDBInsertEntry.getIdPillReminder(), 0);
        }*/

        if (pillReminderDBInsertEntry.getIsActive()==1) {
            switch (cycleDBInsertEntry.getIdCyclingType()) {
                case 1:
                    for (int i = 0; i < cycleDBInsertEntry.getDayCount(); i++) {
                        for (int j = 0; j < pillReminderDBInsertEntry.getReminderTimes().length; j++) {
                            pillReminderDao.insertPillReminderEntry(
                                    sdf.format(new Date(cal.getTimeInMillis())),
                                    pillReminderDBInsertEntry.getIdPillReminder(),
                                    pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr(),0
                            );
                        }
                        cal.add(Calendar.DATE, 1);
                    }
                    break;
                case 2:
                    for (int i = 0; i < cycleDBInsertEntry.getDayCount(); i++) {
                        if (cycleDBInsertEntry.getWeekSchedule()[cal.get(Calendar.DAY_OF_WEEK) - 1] == 1) {
                            for (int j = 0; j < pillReminderDBInsertEntry.getReminderTimes().length; j++) {
                                pillReminderDao.insertPillReminderEntry(
                                        sdf.format(new Date(cal.getTimeInMillis())),
                                        pillReminderDBInsertEntry.getIdPillReminder(),
                                        pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr(),0
                                );
                            }
                        }
                        cal.add(Calendar.DATE, 1);
                    }
                    break;
                case 3:
                    for (int i = 0; i < cycleDBInsertEntry.getDayCount(); i += cycleDBInsertEntry.getDayInterval()) {
                        for (int j = 0; j < pillReminderDBInsertEntry.getReminderTimes().length; j++) {
                            pillReminderDao.insertPillReminderEntry(
                                    sdf.format(new Date(cal.getTimeInMillis())),
                                    pillReminderDBInsertEntry.getIdPillReminder(),
                                    pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr(),0
                            );
                        }
                        cal.add(Calendar.DATE, cycleDBInsertEntry.getDayInterval());
                    }
                    break;
            }
        }

        cal = Calendar.getInstance();
        List<PillReminderEntry> todayPillReminderEntriesNew = pillReminderDao.getPillReminderEntriesByDateAndPillReminder(
                pillReminderDBInsertEntry.getIdPillReminder(),
                new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
                ));

        cal.add(Calendar.DAY_OF_MONTH,1);
        List<PillReminderEntry> tomorrowPillReminderEntriesNew = pillReminderDao.getPillReminderEntriesByDateAndPillReminder(
                pillReminderDBInsertEntry.getIdPillReminder(),
                new DateData(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH)
                ));

        PillNotificationsCreationTask.setupAlarms(appContext, todayPillReminderEntriesNew, alarmManager);
        PillNotificationsCreationTask.setupAlarms(appContext, tomorrowPillReminderEntriesNew, alarmManager);

        dbAdapter.close();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        /*PillNotificationsCreationTask nct = new PillNotificationsCreationTask();
        nct.execute(appContext);*/
        if (AccountGeneralUtils.curUser.getId()!=1) {
            SynchronizationPillReminderTask synchronizationPillReminderTask = new SynchronizationPillReminderTask(appContext);
            synchronizationPillReminderTask.execute();
        }
    }
}
