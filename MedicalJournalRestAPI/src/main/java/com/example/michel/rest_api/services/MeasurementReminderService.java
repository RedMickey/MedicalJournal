package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.MeasurementReminder;
import com.example.michel.rest_api.repositories.MeasurementReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MeasurementReminderService {
    @Autowired
    private MeasurementReminderRepository measurementReminderRepository;

    public Iterable<MeasurementReminder> saveAll(List<MeasurementReminder> measurementReminderList) {
        return measurementReminderRepository.saveAll(measurementReminderList);
    }

    public boolean updateOrDelete(List<MeasurementReminder> measurementReminderList){
        boolean hasDeletion = false;
        for (MeasurementReminder measurementReminder: measurementReminderList) {
            if (measurementReminder.getChangeType()<3)
                measurementReminderRepository.save(measurementReminder);
            else{
                measurementReminderRepository.delete(measurementReminder);
                hasDeletion = true;
            }
        }
        return hasDeletion;
    }

    public void deleteAllByIds(List<UUID> uuidList){
        uuidList.forEach(id -> measurementReminderRepository.deleteById(id));
    }

    public List<MeasurementReminder> getMeasurementRemindersForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        return measurementReminderRepository.getMeasurementRemindersBySynchTimeGreaterThanEqualAndUserIdEquals(
                synchronizationTimestamp, userId);
    }

    public Map<Boolean, List<MeasurementReminder>> getSeparatedMeasurementRemindersForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<MeasurementReminder> measurementReminderList = measurementReminderRepository
                .getMeasurementRemindersBySynchTimeGreaterThanEqualAndUserIdEquals(
                synchronizationTimestamp, userId);
        return measurementReminderList.stream().collect(Collectors
                .partitioningBy(mr -> mr.getChangeType()<3));
    }

    @Transactional
    public void updateAndMarkAsDeleted(List<UUID> uuidList) {
        Timestamp synchronizationTimestamp = new Timestamp(new Date().getTime());
        uuidList.forEach(id -> measurementReminderRepository.updateAndMarkAsDeletedById(id, synchronizationTimestamp));
    }
}
