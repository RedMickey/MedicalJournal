package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.Pill;
import com.example.michel.rest_api.repositories.PillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PillService {
    @Autowired
    private PillRepository pillRepository;

    public Iterable<Pill> saveAll(List<Pill> pillList) {
        return pillRepository.saveAll(pillList);
    }
}
