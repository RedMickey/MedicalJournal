package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.Cycle;
import com.example.michel.rest_api.repositories.CycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CycleService {
    @Autowired
    private CycleRepository cycleRepository;

    public Iterable<Cycle> saveAll(List<Cycle> cycleList) {
        return cycleRepository.saveAll(cycleList);
    }

    public boolean updateOrDelete(List<Cycle> cycleList){
        boolean hasDeletion = false;
        for (Cycle cycle: cycleList) {
            if (cycle.getChangeType()<3)
                cycleRepository.save(cycle);
            else{
                cycleRepository.delete(cycle);
                hasDeletion = true;
            }
        }
        return hasDeletion;
    }

    public void deleteAllByIds(List<UUID> uuidList){
        uuidList.forEach(id -> cycleRepository.deleteById(id));
    }

}
