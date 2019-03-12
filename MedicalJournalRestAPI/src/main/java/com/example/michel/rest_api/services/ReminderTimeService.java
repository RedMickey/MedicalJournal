package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.ReminderTime;
import com.example.michel.rest_api.repositories.ReminderTimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReminderTimeService {
    @Autowired
    private ReminderTimeRepository reminderTimeRepository;

    public Iterable<ReminderTime> saveAll(List<ReminderTime> reminderTimeList) {
        return reminderTimeRepository.saveAll(reminderTimeList);
    }
}
