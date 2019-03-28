package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.WeekSchedule;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.WeekScheduleS;
import com.example.michel.rest_api.repositories.UserRepository;
import com.example.michel.rest_api.repositories.WeekScheduleRepository;
import com.example.michel.rest_api.utils.ConvertingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WeekScheduleService {
    @Autowired
    private WeekScheduleRepository weekScheduleRepository;

    @Autowired
    private ConvertingUtils convertingUtils;

    public Iterable<WeekSchedule> saveAll2(WeekScheduleS[] weekScheduleSArr){

        List<WeekSchedule> weekScheduleList = new ArrayList<>();
        for (WeekScheduleS weekSchedules: weekScheduleSArr) {
            weekScheduleList.add(new WeekSchedule(weekSchedules.getIdWeekSchedule(), weekSchedules.getMon(),
                    weekSchedules.getTue(), weekSchedules.getWed(), weekSchedules.getThu(),
                    weekSchedules.getFri(), weekSchedules.getSat(), weekSchedules.getSun(),
                    new Timestamp(123), 0));
        }

        return weekScheduleRepository.saveAll(weekScheduleList);
    }

    public UUID createAndSaveWeekSchedule(boolean[] weekScheduleArr){
        UUID id = UUID.randomUUID();
        int[] intWeekSchedule = convertingUtils.boolArrToIntArr(weekScheduleArr);
        WeekSchedule weekSchedule = new WeekSchedule(id,
                intWeekSchedule[0],
                intWeekSchedule[1],
                intWeekSchedule[2],
                intWeekSchedule[3],
                intWeekSchedule[4],
                intWeekSchedule[5],
                intWeekSchedule[6],
                new Timestamp(new Date().getTime()),
                1);
        weekScheduleRepository.save(weekSchedule);
        return id;
    }

    public Iterable<WeekSchedule> saveAll(List<WeekSchedule> weekSchedules){
        return weekScheduleRepository.saveAll(weekSchedules);
    }

    public boolean updateOrDelete(List<WeekSchedule> weekSchedules){
        boolean hasDeletion = false;
        for (WeekSchedule weekSchedule: weekSchedules) {
            if (weekSchedule.getChangeType()<3)
                weekScheduleRepository.save(weekSchedule);
            else{
                weekScheduleRepository.delete(weekSchedule);
                hasDeletion = true;
            }
        }
        return hasDeletion;
    }

    public void deleteAllByIds(List<UUID> uuidList){
        uuidList.forEach(id -> weekScheduleRepository.deleteById(id));
    }

    public List<WeekSchedule> getWeekSchedulesForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        return weekScheduleRepository.getWeekSchedulesForSynchronization(
                synchronizationTimestamp, userId);
    }

    public Map<Boolean, List<WeekSchedule>> getSeparatedWeekSchedulesForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<WeekSchedule> weekScheduleList = weekScheduleRepository.getWeekSchedulesForSynchronization(
                synchronizationTimestamp, userId);
        return weekScheduleList.stream().collect(Collectors
                .partitioningBy(ws -> ws.getChangeType()<3));
    }

    @Transactional
    public void updateAndMarkAsDeleted(List<UUID> uuidList) {
        Timestamp synchronizationTimestamp = new Timestamp(new Date().getTime());
        uuidList.forEach(id -> weekScheduleRepository.updateAndMarkAsDeletedById(id, synchronizationTimestamp));
    }

    public boolean[] getWeekScheduleAsBoolArrById(UUID id){
        WeekSchedule weekSchedule = weekScheduleRepository
                .getByIdWeekScheduleEqualsAndChangeTypeLessThanEqual(id, 3);
        return new boolean[]{
                weekSchedule.getMon()==1,
                weekSchedule.getTue()==1,
                weekSchedule.getWed()==1,
                weekSchedule.getThu()==1,
                weekSchedule.getFri()==1,
                weekSchedule.getSat()==1,
                weekSchedule.getSun()==1
        };
    }
}
