package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.Cycle;
import com.example.michel.rest_api.repositories.CycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CycleService {
    @Autowired
    private CycleRepository cycleRepository;

    public Iterable<Cycle> saveAll(List<Cycle> cycleList) {
        return cycleRepository.saveAll(cycleList);
    }
}
