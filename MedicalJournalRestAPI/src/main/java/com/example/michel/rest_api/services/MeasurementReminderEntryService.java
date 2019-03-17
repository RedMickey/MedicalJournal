package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.MeasurementReminderEntry;
import com.example.michel.rest_api.repositories.MeasurementReminderEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

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
}
