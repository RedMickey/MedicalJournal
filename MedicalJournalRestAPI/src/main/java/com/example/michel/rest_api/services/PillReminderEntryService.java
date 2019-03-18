package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.PillReminderEntry;
import com.example.michel.rest_api.repositories.PillReminderEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PillReminderEntryService {
    @Autowired
    private PillReminderEntryRepository pillReminderEntryRepository;

    public Iterable<PillReminderEntry> saveAll(List<PillReminderEntry> pillReminderEntryList) {
        return pillReminderEntryRepository.saveAll(pillReminderEntryList);
    }

    public PillReminderEntry save(PillReminderEntry pillReminderEntry){
        return pillReminderEntryRepository.save(pillReminderEntry);
    }

    public boolean updateOrDelete(List<PillReminderEntry> pillReminderEntryList){
        boolean hasDeletion = false;
        for (PillReminderEntry pillReminderEntry: pillReminderEntryList) {
            if (pillReminderEntry.getChangeType()<3)
                pillReminderEntryRepository.save(pillReminderEntry);
            else{
                pillReminderEntryRepository.delete(pillReminderEntry);
                hasDeletion = true;
            }
        }
        return hasDeletion;
    }

    public void deleteAllByIds(List<UUID> uuidList){
        uuidList.forEach(id -> pillReminderEntryRepository.deleteById(id));
    }

    @Transactional
    public List<PillReminderEntry> getPillReminderEntriesForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<PillReminderEntry> pillReminderEntries = pillReminderEntryRepository
                .getPillReminderEntriesForSynchronization( synchronizationTimestamp, userId);
        pillReminderEntries.forEach(pre -> pre.setReminderTime(new Time(pre.getReminderDate().getTime())));
        return pillReminderEntries;
    }

    @Transactional
    public Map<Boolean, List<PillReminderEntry>> getSeparatedPillReminderEntriesForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<PillReminderEntry> pillReminderEntries = pillReminderEntryRepository
                .getPillReminderEntriesForSynchronization( synchronizationTimestamp, userId);
        return pillReminderEntries.parallelStream().peek(pre -> pre.setReminderTime(new Time(pre.getReminderDate().getTime())))
                .collect(Collectors.partitioningBy(pre -> pre.getChangeType()<3));
    }
}
