package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.BloodPressureMeasurement;
import org.springframework.data.repository.CrudRepository;

public interface BloodPressureMeasurementRepository extends CrudRepository<BloodPressureMeasurement, String> {

}
