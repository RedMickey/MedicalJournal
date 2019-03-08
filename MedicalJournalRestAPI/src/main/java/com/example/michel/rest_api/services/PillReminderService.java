package com.example.michel.rest_api.services;

import com.example.michel.rest_api.repositories.PillReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PillReminderService {
    @Autowired
    private PillReminderRepository pillReminderRepository;
}
