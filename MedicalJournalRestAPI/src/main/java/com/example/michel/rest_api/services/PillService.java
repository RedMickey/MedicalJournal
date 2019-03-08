package com.example.michel.rest_api.services;

import com.example.michel.rest_api.repositories.PillRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class PillService {
    @Autowired
    private PillRepository pillRepository;
}
