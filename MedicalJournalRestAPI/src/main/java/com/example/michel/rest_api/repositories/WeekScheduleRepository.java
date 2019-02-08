package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.WeekSchedule;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WeekScheduleRepository extends CrudRepository<WeekSchedule, String> {

    List<WeekSchedule> findAll();
}
