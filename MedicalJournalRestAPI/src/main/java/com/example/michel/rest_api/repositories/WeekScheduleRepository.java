package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.WeekSchedule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface WeekScheduleRepository extends CrudRepository<WeekSchedule, UUID> {

    List<WeekSchedule> findAll();
}
