package com.example.michel.rest_api.services;

import com.example.michel.rest_api.repositories.PillReminderEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PillReminderEntryService {
    @Autowired
    private PillReminderEntryRepository pillReminderEntryRepository;
}
