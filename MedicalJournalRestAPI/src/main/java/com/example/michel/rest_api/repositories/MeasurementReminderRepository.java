package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.MeasurementReminder;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface MeasurementReminderRepository extends CrudRepository<MeasurementReminder, UUID> {

    List<MeasurementReminder> getMeasurementRemindersBySynchTimeGreaterThanEqualAndUserIdEquals(
            Timestamp synchronizationTimestamp, Integer userId
    );
}
