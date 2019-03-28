package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.MeasurementReminderEntry;
import com.example.michel.rest_api.repositories.MeasurementReminderEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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
}
