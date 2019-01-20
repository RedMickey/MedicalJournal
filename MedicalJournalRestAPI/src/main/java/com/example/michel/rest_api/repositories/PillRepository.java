package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.Pill;
import org.springframework.data.repository.CrudRepository;

public interface PillRepository extends CrudRepository<Pill, String> {
}
