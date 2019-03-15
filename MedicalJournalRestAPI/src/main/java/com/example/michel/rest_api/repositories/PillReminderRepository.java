package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.PillReminder;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PillReminderRepository extends CrudRepository<PillReminder, UUID> {
}
