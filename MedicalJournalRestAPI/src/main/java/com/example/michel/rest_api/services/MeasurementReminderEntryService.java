package com.example.michel.rest_api.services;

import com.example.michel.rest_api.repositories.MeasurementReminderEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class MeasurementReminderEntryService {
    @Autowired
    private MeasurementReminderEntryRepository measurementReminderEntryRepository;
}
