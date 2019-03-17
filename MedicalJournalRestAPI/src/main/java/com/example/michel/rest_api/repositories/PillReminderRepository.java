package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.PillReminder;
import org.springframework.data.repository.CrudRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface PillReminderRepository extends CrudRepository<PillReminder, UUID> {

    List<PillReminder> getPillRemindersBySynchTimeGreaterThanEqualAndUserIdEquals(
            Timestamp synchronizationTimestamp, Integer userId
    );
}
