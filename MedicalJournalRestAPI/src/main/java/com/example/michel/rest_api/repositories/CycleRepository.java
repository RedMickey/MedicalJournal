package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.Cycle;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CycleRepository extends CrudRepository<Cycle, UUID> {
}
