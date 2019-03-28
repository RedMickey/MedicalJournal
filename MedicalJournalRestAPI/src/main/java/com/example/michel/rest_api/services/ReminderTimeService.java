package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.ReminderTime;
import com.example.michel.rest_api.repositories.ReminderTimeRepository;
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
public class ReminderTimeService {
    @Autowired
    private ReminderTimeRepository reminderTimeRepository;

    public Iterable<ReminderTime> saveAll(List<ReminderTime> reminderTimeList) {
        return reminderTimeRepository.saveAll(reminderTimeList);
    }

    public boolean updateOrDelete(List<ReminderTime> reminderTimeList){
        boolean hasDeletion = false;
        for (ReminderTime reminderTime: reminderTimeList) {
            if (reminderTime.getChangeType()<3)
                reminderTimeRepository.save(reminderTime);
            else{
                reminderTimeRepository.delete(reminderTime);
                hasDeletion = true;
            }
        }
        return hasDeletion;
    }

    public void deleteAllByIds(List<UUID> uuidList){
        uuidList.forEach(id -> reminderTimeRepository.deleteById(id));
    }

    public List<ReminderTime> getReminderTimeForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        return reminderTimeRepository.getReminderTimeForSynchronization(
                synchronizationTimestamp, userId);
    }

    public Map<Boolean, List<ReminderTime>> getSeparatedReminderTimeForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<ReminderTime> reminderTimeList = reminderTimeRepository.getReminderTimeForSynchronization(
                synchronizationTimestamp, userId);
        return reminderTimeList.stream().collect(Collectors
                .partitioningBy(rt -> rt.getChangeType()<3));
    }

    public Date[] getReminderTimeDateArrByReminderId(UUID reminderId, int reminderType){
        if (reminderType == 1){
            List<ReminderTime> reminderTimeList = reminderTimeRepository
                    .getAllByIdPillReminderEqualsAndChangeTypeLessThanEqual(
                    reminderId, 3);
            return reminderTimeList.stream().map(reminderTime -> reminderTime.getReminderTime()).toArray(Date[]::new);
        }
        else {
            List<ReminderTime> reminderTimeList = reminderTimeRepository
                    .getAllByIdMeasurementReminderEqualsAndChangeTypeLessThanEqual(
                            reminderId, 3);
            return reminderTimeList.stream().map(reminderTime -> reminderTime.getReminderTime()).toArray(Date[]::new);
        }
    }

    @Transactional
    public void updateAndMarkAsDeleted(List<UUID> uuidList) {
        Timestamp synchronizationTimestamp = new Timestamp(new Date().getTime());
        uuidList.forEach(id -> reminderTimeRepository.updateAndMarkAsDeletedById(id, synchronizationTimestamp));
    }

    public UUID createAndSavePillReminder(Date reminderTime, UUID reminderId, int type){
        UUID id = UUID.randomUUID();
        ReminderTime reminderTimeEntry = new ReminderTime();
        reminderTimeEntry.setIdReminderTime(id);
        reminderTimeEntry.setReminderTime(new Time(reminderTime.getTime()));
        if (type == 1)
            reminderTimeEntry.setIdPillReminder(reminderId);
        else
            reminderTimeEntry.setIdMeasurementReminder(reminderId);
        reminderTimeEntry.setSynchTime(new Timestamp(new Date().getTime()));
        reminderTimeEntry.setChangeType(1);
        reminderTimeRepository.save(reminderTimeEntry);
        return id;
    }
}
