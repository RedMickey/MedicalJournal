package com.example.michel.rest_api.services;

import com.example.michel.rest_api.repositories.MeasurementReminderRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class MeasurementReminderService {
    @Autowired
    private MeasurementReminderRepository measurementReminderRepository;
}
