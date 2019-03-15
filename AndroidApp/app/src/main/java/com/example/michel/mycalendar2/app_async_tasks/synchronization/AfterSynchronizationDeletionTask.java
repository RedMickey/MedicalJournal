package com.example.michel.mycalendar2.app_async_tasks.synchronization;

import android.os.AsyncTask;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;
import com.example.michel.mycalendar2.models.synchronization.MeasurementReminderDB;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class AfterSynchronizationDeletionTask extends AsyncTask<Void, Void, Integer> {
    private int type;
    private UUID reminderId;
    private String curDateStr;

    public AfterSynchronizationDeletionTask(int type){
        this.type = type;
    }

    public AfterSynchronizationDeletionTask(int type, UUID reminderId, String curDateStr){
        this.type = type;
        this.reminderId = reminderId;
        this.curDateStr = curDateStr;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        ReminderTimeDao reminderTimeDao = new ReminderTimeDao(dbAdapter.open().getDatabase());
        PillReminderDao pillReminderDao = null;
        MeasurementReminderDao measurementReminderDao = null;
        CycleDao cycleDao = null;

        switch (type){
            case 1:
                pillReminderDao = new PillReminderDao(dbAdapter.getDatabase());
                pillReminderDao.deletePillReminderEntriesAfterSynchronization(reminderId);
                reminderTimeDao.deleteReminderTimeAfterSynchronizationByReminderId(reminderId,0);
                break;
            case 2:
                measurementReminderDao = new MeasurementReminderDao(dbAdapter.getDatabase());
                measurementReminderDao.deleteMeasurementReminderEntriesAfterSynchronization(reminderId);
                reminderTimeDao.deleteReminderTimeAfterSynchronizationByReminderId(reminderId,1);
                break;
            case 3:
                pillReminderDao = new PillReminderDao(dbAdapter.getDatabase());
                cycleDao = new CycleDao(dbAdapter.getDatabase());
                pillReminderDao.deletePillReminderEntriesByPillReminderId(reminderId);
                reminderTimeDao.deleteReminderTimeByReminderId(reminderId, 0);
                cycleDao.deleteWeekSchedulesAfterSynchronization();
                cycleDao.deleteCyclesAfterSynchronizationCascade();
                pillReminderDao.deletePillReminderById(reminderId);
                break;
            case 4:
                measurementReminderDao = new MeasurementReminderDao(dbAdapter.getDatabase());
                cycleDao = new CycleDao(dbAdapter.getDatabase());
                measurementReminderDao.deleteMeasurementReminderEntriesByMeasurementReminderId(reminderId);
                reminderTimeDao.deleteReminderTimeByReminderId(reminderId, 1);
                cycleDao.deleteWeekSchedulesAfterSynchronization();
                cycleDao.deleteCyclesAfterSynchronizationCascade();
                measurementReminderDao.deleteMeasurementReminderById(reminderId);
                //List<MeasurementReminderDB> m = measurementReminderDao.getMeasurementReminderDBEntriesForSynchronization(new Date(12345));
                //int y= 5;
                break;
                default:
                    pillReminderDao = new PillReminderDao(dbAdapter.getDatabase());
                    measurementReminderDao = new MeasurementReminderDao(dbAdapter.getDatabase());
                    cycleDao = new CycleDao(dbAdapter.getDatabase());

                    pillReminderDao.deletePillReminderEntriesAfterSynchronization();
                    measurementReminderDao.deleteMeasurementReminderEntriesAfterSynchronization();
                    reminderTimeDao.deleteReminderTimeAfterSynchronization();
                    cycleDao.deleteWeekSchedulesAfterSynchronization();
                    cycleDao.deleteCyclesAfterSynchronizationCascade();
                    pillReminderDao.deletePillReminderAfterSynchronization();
                    measurementReminderDao.deleteMeasurementReminderAfterSynchronization();
                    break;
        }
        dbAdapter.close();

        return null;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setReminderId(UUID reminderId) {
        this.reminderId = reminderId;
    }

    public void setCurDateStr(String curDateStr) {
        this.curDateStr = curDateStr;
    }
}
