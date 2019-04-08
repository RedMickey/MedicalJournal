package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.PillReminder;
import com.example.michel.rest_api.models.pill.PillReminderCourse;
import com.example.michel.rest_api.repositories.PillReminderRepository;
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
public class PillReminderService {
    @Autowired
    private PillReminderRepository pillReminderRepository;

    public Iterable<PillReminder> saveAll(List<PillReminder> pillReminderList) {
        return pillReminderRepository.saveAll(pillReminderList);
    }

    public boolean updateOrDelete(List<PillReminder> pillReminderList){
        boolean hasDeletion = false;
        for (PillReminder pillReminder: pillReminderList) {
            if (pillReminder.getChangeType()<3)
                pillReminderRepository.save(pillReminder);
            else{
                pillReminderRepository.delete(pillReminder);
                hasDeletion = true;
            }
        }
        return hasDeletion;
    }

    public void deleteAllByIds(List<UUID> uuidList){
        uuidList.forEach(id -> pillReminderRepository.deleteById(id));
    }

    public List<PillReminder> getPillRemindersForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        return pillReminderRepository.getPillRemindersBySynchTimeGreaterThanEqualAndUserIdEquals(
                synchronizationTimestamp, userId);
    }

    public Map<Boolean, List<PillReminder>> getSeparatedPillRemindersForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<PillReminder> pillReminderList = pillReminderRepository
                .getPillRemindersBySynchTimeGreaterThanEqualAndUserIdEquals(
                synchronizationTimestamp, userId);
        return pillReminderList.stream().collect(Collectors
                .partitioningBy(pr -> pr.getChangeType()<3));
    }

    @Transactional
    public void updateAndMarkAsDeleted(List<UUID> uuidList) {
        Timestamp synchronizationTimestamp = new Timestamp(new Date().getTime());
        uuidList.forEach(id -> pillReminderRepository.updateAndMarkAsDeletedById(id, synchronizationTimestamp));
    }

    @Transactional
    public void updateAndMarkAsDeletedById(UUID idPillReminder){
        pillReminderRepository.updateAndMarkAsDeletedById(idPillReminder, new Timestamp(new Date().getTime()));
    }

    public UUID createAndSavePillReminder(PillReminderCourse pillReminderCourse, UUID pillId, int isOnetime, int userId){
        UUID id = UUID.randomUUID();
        PillReminder pillReminder = new PillReminder(id,
                pillId, pillReminderCourse.getPillCount(), pillReminderCourse.getIdPillCountType(),
                new java.sql.Date(pillReminderCourse.getStartDate().getTime()), pillReminderCourse.getIdCycle(),
                pillReminderCourse.getIdHavingMealsType(), pillReminderCourse.getHavingMealsTime(),
                pillReminderCourse.getAnnotation(), pillReminderCourse.getIsActive(),
                pillReminderCourse.getReminderTimes().length, isOnetime, userId,
                new Timestamp(new Date().getTime()), 1);
        pillReminderRepository.save(pillReminder);
        return id;
    }

    public void updatePillReminderById(PillReminderCourse pillReminderCourse, UUID pillId, int isOnetime, int userId){
        PillReminder pillReminder = new PillReminder(pillReminderCourse.getIdPillReminder(),
                pillId, pillReminderCourse.getPillCount(), pillReminderCourse.getIdPillCountType(),
                new java.sql.Date(pillReminderCourse.getStartDate().getTime()), pillReminderCourse.getIdCycle(),
                pillReminderCourse.getIdHavingMealsType(), pillReminderCourse.getHavingMealsTime(),
                pillReminderCourse.getAnnotation(), pillReminderCourse.getIsActive(),
                pillReminderCourse.getReminderTimes().length, isOnetime, userId,
                new Timestamp(new Date().getTime()), 2);
        pillReminderRepository.save(pillReminder);
    }
}
