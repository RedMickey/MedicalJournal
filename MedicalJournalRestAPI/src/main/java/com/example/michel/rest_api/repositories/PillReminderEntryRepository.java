package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.PillReminderEntry;
import org.springframework.data.repository.CrudRepository;

public interface PillReminderEntryRepository extends CrudRepository<PillReminderEntry, String> {
}
