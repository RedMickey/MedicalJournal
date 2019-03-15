package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.PillReminderEntry;
import com.example.michel.rest_api.repositories.PillReminderEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

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
}
