package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.WeekSchedule;
import org.springframework.data.repository.CrudRepository;

public interface WeekScheduleRepository extends CrudRepository<WeekSchedule, String> {
}
