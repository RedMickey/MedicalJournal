package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.ReminderTime;
import org.springframework.data.repository.CrudRepository;

public interface ReminderTimeRepository extends CrudRepository<ReminderTime, String> {
}
