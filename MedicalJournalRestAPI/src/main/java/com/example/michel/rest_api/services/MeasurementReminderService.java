package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.MeasurementReminder;
import com.example.michel.rest_api.repositories.MeasurementReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementReminderService {
    @Autowired
    private MeasurementReminderRepository measurementReminderRepository;

    public Iterable<MeasurementReminder> saveAll(List<MeasurementReminder> measurementReminderList) {
        return measurementReminderRepository.saveAll(measurementReminderList);
    }
}
