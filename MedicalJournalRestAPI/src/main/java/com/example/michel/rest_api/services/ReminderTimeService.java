package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.ReminderTime;
import com.example.michel.rest_api.repositories.ReminderTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

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
}
