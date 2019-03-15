package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.PillReminderEntry;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PillReminderEntryRepository extends CrudRepository<PillReminderEntry, UUID> {
}
