package com.example.michel.mycalendar2.controllers.app_async_tasks.synchronization;

import android.os.AsyncTask;

import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.CycleDao;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.dao.ReminderTimeDao;

import java.util.List;
import java.util.UUID;

public class AfterSynchronizationDeletionTask extends AsyncTask<Void, Void, Integer> {
    private List<Integer> deletionTypes;
    private UUID reminderId;

    public AfterSynchronizationDeletionTask(List<Integer> deletionTypes){
        this.deletionTypes = deletionTypes;
    }

    public AfterSynchronizationDeletionTask(List<Integer> deletionTypes, UUID reminderId){
        this.deletionTypes = deletionTypes;
        this.reminderId = reminderId;
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        /*DatabaseAdapter dbAdapter = new DatabaseAdapter();
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
        dbAdapter.close();*/

        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        ReminderTimeDao reminderTimeDao = new ReminderTimeDao(dbAdapter.open().getDatabase());
        PillReminderDao pillReminderDao = new PillReminderDao(dbAdapter.getDatabase());;
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(dbAdapter.getDatabase());;
        CycleDao cycleDao = new CycleDao(dbAdapter.getDatabase());

        for (int t: deletionTypes) {
            switch (t){
                case 1: // deletePillReminderEntries
                    pillReminderDao.deletePillReminderEntriesAfterSynchronization();
                    break;
                case 2: // deleteMeasurementReminderEntries
                    measurementReminderDao.deleteMeasurementReminderEntriesAfterSynchronization();
                    break;
                case 3: // deleteReminderTime
                    reminderTimeDao.deleteReminderTimeAfterSynchronization();
                    break;
                case 4: // deleteWeekSchedules
                    cycleDao.deleteWeekSchedulesAfterSynchronization();
                    break;
                case 5: // deleteCycles
                    cycleDao.deleteCyclesAfterSynchronizationCascade();
                    break;
                case 6: // deletePillReminder
                    pillReminderDao.deletePillReminderAfterSynchronization();
                    break;
                case 7: // deleteMeasurementReminder
                    measurementReminderDao.deleteMeasurementReminderAfterSynchronization();
                    break;
                default:
                    pillReminderDao.deletePillReminderEntriesAfterSynchronization();
                    measurementReminderDao.deleteMeasurementReminderEntriesAfterSynchronization();
                    reminderTimeDao.deleteReminderTimeAfterSynchronization();
                    cycleDao.deleteWeekSchedulesAfterSynchronization();
                    cycleDao.deleteCyclesAfterSynchronizationCascade();
                    pillReminderDao.deletePillReminderAfterSynchronization();
                    measurementReminderDao.deleteMeasurementReminderAfterSynchronization();
                    break;
            }
        }

        dbAdapter.close();

        return null;
    }

    public void setReminderId(UUID reminderId) {
        this.reminderId = reminderId;
    }

    public void setDeletionTypes(List<Integer> deletionTypes) {
        this.deletionTypes = deletionTypes;
    }
}
