package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.TemperatureMeasurement;
import org.springframework.data.repository.CrudRepository;

public interface TemperatureMeasurementRepository extends CrudRepository<TemperatureMeasurement, String> {
}
