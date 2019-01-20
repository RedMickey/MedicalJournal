package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.MeasurementReminder;
import org.springframework.data.repository.CrudRepository;

public interface MeasurementReminderRepository extends CrudRepository<MeasurementReminder, String> {
}
