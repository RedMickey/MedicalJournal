package com.example.michel.rest_api.services;

import com.example.michel.rest_api.repositories.ReminderTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class ReminderTimeService {
    @Autowired
    private ReminderTimeRepository reminderTimeRepository;
}
