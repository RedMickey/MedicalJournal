package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.MeasurementReminderEntry;
import org.springframework.data.repository.CrudRepository;

public interface MeasurementReminderEntryRepository extends CrudRepository<MeasurementReminderEntry, String> {
}
