package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.PillReminder;
import com.example.michel.rest_api.repositories.PillReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PillReminderService {
    @Autowired
    private PillReminderRepository pillReminderRepository;

    public Iterable<PillReminder> saveAll(List<PillReminder> pillReminderList) {
        return pillReminderRepository.saveAll(pillReminderList);
    }
}
