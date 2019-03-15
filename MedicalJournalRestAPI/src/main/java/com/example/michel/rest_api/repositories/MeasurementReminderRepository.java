package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.MeasurementReminder;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MeasurementReminderRepository extends CrudRepository<MeasurementReminder, UUID> {
}
