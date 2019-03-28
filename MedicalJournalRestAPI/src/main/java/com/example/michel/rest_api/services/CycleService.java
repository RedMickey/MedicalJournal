package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.Cycle;
import com.example.michel.rest_api.models.auxiliary_models.CycleDBInsertEntry;
import com.example.michel.rest_api.repositories.CycleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<Cycle> getCycleDBEntriesForSynchronization(Timestamp synchronizationTimestamp, Integer userId){
        return cycleRepository.getCycleDBEntriesForSynchronization(synchronizationTimestamp, userId);
    }

    public Map<Boolean, List<Cycle>> getSeparatedCycleDBEntriesForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<Cycle> cycleList = cycleRepository.getCycleDBEntriesForSynchronization(synchronizationTimestamp, userId);
        return cycleList.stream().collect(Collectors
                .partitioningBy(c -> c.getChangeType()<3));
    }

    @Transactional
    public void updateAndMarkAsDeleted(List<UUID> uuidList) {
        Timestamp synchronizationTimestamp = new Timestamp(new Date().getTime());
        uuidList.forEach(id -> cycleRepository.updateAndMarkAsDeletedById(id, synchronizationTimestamp));
    }

    public UUID createAndSaveCycle(CycleDBInsertEntry cycleDBInsertEntry){
        UUID id = UUID.randomUUID();
        Cycle cycle = new Cycle(id, cycleDBInsertEntry.getPeriod(),
                cycleDBInsertEntry.getPeriodDMType(), cycleDBInsertEntry.getOnceAPeriod(),
                cycleDBInsertEntry.getOnceAPeriodDMType(), cycleDBInsertEntry.getIdWeekSchedule(),
                cycleDBInsertEntry.getIdCyclingType(), new Timestamp(new Date().getTime()), 1);
        cycleRepository.save(cycle);
        return id;
    }
}
