package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.PillReminderEntry;
import com.example.michel.rest_api.repositories.PillReminderEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PillReminderEntryService {
    @Autowired
    private PillReminderEntryRepository pillReminderEntryRepository;

    public Iterable<PillReminderEntry> saveAll(List<PillReminderEntry> pillReminderEntryList) {
        return pillReminderEntryRepository.saveAll(pillReminderEntryList);
    }
}
