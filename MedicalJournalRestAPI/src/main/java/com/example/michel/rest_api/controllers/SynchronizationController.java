package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.WeekSchedule;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.PillReminderReqModule;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.WeekScheduleS;
import com.example.michel.rest_api.models.measurement.MeasurementReminderEntryF;
import com.example.michel.rest_api.services.PillReminderEntryFService;
import com.example.michel.rest_api.services.WeekScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/synchronization")
public class SynchronizationController {

    @Autowired
    private WeekScheduleService weekScheduleService;

    @PostMapping(value = "/synchronizeWeekSchedules", produces = "application/json")
    public Map synchronizeWeekSchedules(@RequestBody WeekScheduleS[] req){
        weekScheduleService.saveAll2(req);
        return null;
    }

    @PostMapping(value = "/synchronizePillReminderModules", produces = "application/json")
    public Map synchronizePillReminderModules(@RequestBody PillReminderReqModule req){

        return null;
    }
}
