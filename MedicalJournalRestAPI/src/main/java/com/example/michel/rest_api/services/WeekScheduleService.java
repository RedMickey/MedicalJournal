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
}
