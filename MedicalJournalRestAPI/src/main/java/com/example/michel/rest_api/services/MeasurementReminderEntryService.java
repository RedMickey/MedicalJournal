package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.MeasurementReminderEntry;
import com.example.michel.rest_api.repositories.MeasurementReminderEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementReminderEntryService {
    @Autowired
    private MeasurementReminderEntryRepository measurementReminderEntryRepository;

    public Iterable<MeasurementReminderEntry> saveAll(List<MeasurementReminderEntry> measurementReminderEntryList) {
        return measurementReminderEntryRepository.saveAll(measurementReminderEntryList);
    }
}
