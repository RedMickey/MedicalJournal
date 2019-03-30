package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.MeasurementReminderEntry;
import com.example.michel.rest_api.repositories.MeasurementReminderEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MeasurementReminderEntryService {
    @Autowired
    private MeasurementReminderEntryRepository measurementReminderEntryRepository;

    public Iterable<MeasurementReminderEntry> saveAll(List<MeasurementReminderEntry> measurementReminderEntryList) {
        return measurementReminderEntryRepository.saveAll(measurementReminderEntryList);
    }

    public MeasurementReminderEntry save(MeasurementReminderEntry measurementReminderEntry){
        return measurementReminderEntryRepository.save(measurementReminderEntry);
    }

    public boolean updateOrDelete(List<MeasurementReminderEntry> measurementReminderEntryList){
        boolean hasDeletion = false;
        for (MeasurementReminderEntry measurementReminderEntry: measurementReminderEntryList) {
            if (measurementReminderEntry.getChangeType()<3)
                measurementReminderEntryRepository.save(measurementReminderEntry);
            else{
                measurementReminderEntryRepository.delete(measurementReminderEntry);
                hasDeletion = true;
            }
        }
        return hasDeletion;
    }

    public void deleteAllByIds(List<UUID> uuidList){
        uuidList.forEach(id -> measurementReminderEntryRepository.deleteById(id));
    }

    public List<MeasurementReminderEntry> getMeasurementReminderEntriesForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<MeasurementReminderEntry> measurementReminderEntries = measurementReminderEntryRepository
                .getMeasurementReminderEntriesForSynchronization(
                synchronizationTimestamp, userId);
        measurementReminderEntries.forEach(mre -> mre.setReminderTime(new Time(mre.getReminderDate().getTime())));
        return measurementReminderEntries;
    }

    public Map<Boolean, List<MeasurementReminderEntry>> getSeparatedMeasurementReminderEntriesForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<MeasurementReminderEntry> measurementReminderEntries = measurementReminderEntryRepository
                .getMeasurementReminderEntriesForSynchronization(
                        synchronizationTimestamp, userId);
        return measurementReminderEntries.parallelStream().peek(mre -> mre.setReminderTime(new Time(mre.getReminderDate().getTime())))
                .collect(Collectors.partitioningBy(mre -> mre.getChangeType()<3));
    }

    @Transactional
    public void updateAndMarkAsDeleted(List<UUID> uuidList) {
        Timestamp synchronizationTimestamp = new Timestamp(new Date().getTime());
        uuidList.forEach(id -> measurementReminderEntryRepository.updateAndMarkAsDeletedById(id, synchronizationTimestamp));
    }

    public UUID createAndSaveMeasurementReminderEntry(Date reminderDate, UUID idMeasurementReminder, int isOneTime){
        UUID id = UUID.randomUUID();
        MeasurementReminderEntry mre = new MeasurementReminderEntry(id,
                -10000d, -10000d, idMeasurementReminder, 0, null,
                reminderDate, isOneTime, new Timestamp(new Date().getTime()), 1
        );
        measurementReminderEntryRepository.save(mre);
        return id;
    }

    public void createAndSaveMeasurementReminderEntries(Date startDate, Date[] reminderDates, int IdCyclingType, int perDayCount, UUID idMeasurementReminder,
                                                 int interDayCount, boolean[] weekSchedule){
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        switch (IdCyclingType){
            case 1:
                for(int i=0; i<perDayCount;i++){
                    for (int j=0; j<reminderDates.length; j++){
                        cal2.setTime(reminderDates[j]);
                        cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
                        cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
                        createAndSaveMeasurementReminderEntry(cal.getTime(), idMeasurementReminder, 0);
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 2:
                for(int i=0; i<perDayCount;i++){
                    if(weekSchedule[cal.get(Calendar.DAY_OF_WEEK)-1]){
                        for (int j=0; j<reminderDates.length; j++){
                            cal2.setTime(reminderDates[j]);
                            cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
                            cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
                            createAndSaveMeasurementReminderEntry(cal.getTime(), idMeasurementReminder, 0);
                        }
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 3:
                for(int i=0; i<perDayCount;i+=interDayCount){
                    for (int j=0; j<reminderDates.length; j++){
                        cal2.setTime(reminderDates[j]);
                        cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
                        cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
                        createAndSaveMeasurementReminderEntry(cal.getTime(), idMeasurementReminder, 0);
                    }
                    cal.add(Calendar.DATE, interDayCount);
                }
                break;
        }
    }

    @Transactional
    public void updateAndMarkAsDeletedByMeasurementReminderId(UUID idMeasurementReminder){
        measurementReminderEntryRepository.updateAndMarkAsDeletedByMeasurementReminderId(
                idMeasurementReminder, new Timestamp(new Date().getTime()));
    }

    @Transactional
    public void updateAndMarkAsDeletedAfterDate(Date reminderDate, UUID idMeasurementReminder){
        measurementReminderEntryRepository.updateAndMarkAsDeletedAfterDate(
                new Timestamp(new Date().getTime()), reminderDate, idMeasurementReminder);
    }
}
