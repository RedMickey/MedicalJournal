package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.WeekSchedule;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.WeekScheduleS;
import com.example.michel.rest_api.repositories.UserRepository;
import com.example.michel.rest_api.repositories.WeekScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class WeekScheduleService {
    @Autowired
    private WeekScheduleRepository weekScheduleRepository;

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
}
