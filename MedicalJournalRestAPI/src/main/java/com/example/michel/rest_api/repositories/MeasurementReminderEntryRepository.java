package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.MeasurementReminderEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface MeasurementReminderEntryRepository extends CrudRepository<MeasurementReminderEntry, UUID> {

}
