package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.Pill;
import com.example.michel.rest_api.repositories.PillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class PillService {
    @Autowired
    private PillRepository pillRepository;

    public Iterable<Pill> saveAll(List<Pill> pillList) {
        return pillRepository.saveAll(pillList);
    }

    public boolean updateOrDelete(List<Pill> pillList){
        boolean hasDeletion = false;
        for (Pill pill: pillList) {
            if (pill.getChangeType()<3)
                pillRepository.save(pill);
            else{
                pillRepository.delete(pill);
                hasDeletion = true;
            }
        }
        return hasDeletion;
    }

     public List<Pill> getPillsForSynchronization(Timestamp synchronizationTimestamp, Integer userId){
        return pillRepository.getPillsForSynchronization(synchronizationTimestamp, userId);
    }
}
