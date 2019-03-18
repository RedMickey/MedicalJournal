package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.PillReminder;
import com.example.michel.rest_api.repositories.PillReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PillReminderService {
    @Autowired
    private PillReminderRepository pillReminderRepository;

    public Iterable<PillReminder> saveAll(List<PillReminder> pillReminderList) {
        return pillReminderRepository.saveAll(pillReminderList);
    }

    public boolean updateOrDelete(List<PillReminder> pillReminderList){
        boolean hasDeletion = false;
        for (PillReminder pillReminder: pillReminderList) {
            if (pillReminder.getChangeType()<3)
                pillReminderRepository.save(pillReminder);
            else{
                pillReminderRepository.delete(pillReminder);
                hasDeletion = true;
            }
        }
        return hasDeletion;
    }

    public void deleteAllByIds(List<UUID> uuidList){
        uuidList.forEach(id -> pillReminderRepository.deleteById(id));
    }

    public List<PillReminder> getPillRemindersForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        return pillReminderRepository.getPillRemindersBySynchTimeGreaterThanEqualAndUserIdEquals(
                synchronizationTimestamp, userId);
    }

    public Map<Boolean, List<PillReminder>> getSeparatedPillRemindersForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<PillReminder> pillReminderList = pillReminderRepository
                .getPillRemindersBySynchTimeGreaterThanEqualAndUserIdEquals(
                synchronizationTimestamp, userId);
        return pillReminderList.stream().collect(Collectors
                .partitioningBy(pr -> pr.getChangeType()<3));
    }
}
