package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.PillReminder;
import org.springframework.data.repository.CrudRepository;

public interface PillReminderRepository extends CrudRepository<PillReminder, String> {
}
