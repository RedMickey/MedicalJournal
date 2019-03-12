package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.WeekSchedule;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.MeasurementReminderReqModule;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.PillReminderReqModule;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.WeekScheduleS;
import com.example.michel.rest_api.models.measurement.MeasurementReminderEntryF;
import com.example.michel.rest_api.services.*;
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

    @Autowired
    private CycleService cycleService;

    @Autowired
    private MeasurementReminderEntryService measurementReminderEntryService;

    @Autowired
    private MeasurementReminderService measurementReminderService;

    @Autowired
    private PillReminderEntryService pillReminderEntryService;

    @Autowired
    private PillReminderService pillReminderService;

    @Autowired
    private PillService pillService;

    @Autowired
    private ReminderTimeService reminderTimeService;

    @PostMapping(value = "/synchronizeWeekSchedules", produces = "application/json")
    public Map synchronizeWeekSchedules(@RequestBody WeekScheduleS[] req){
        weekScheduleService.saveAll2(req);
        return null;
    }

    @PostMapping(value = "/synchronizePillReminderModules", produces = "application/json")
    public Map synchronizePillReminderModules(@RequestBody PillReminderReqModule req){
        weekScheduleService.saveAll(req.getWeekScheduleDBList());
        cycleService.saveAll(req.getCycleDBList());
        pillService.saveAll(req.getPillDBList());
        pillReminderService.saveAll(req.getPillReminderDBList());
        reminderTimeService.saveAll(req.getReminderTimeDBList());
        pillReminderEntryService.saveAll(req.getPillReminderEntryDBList());

        return null;
    }

    @PostMapping(value = "/synchronizeMeasurementReminderReqModules", produces = "application/json")
    public Map synchronizeMeasurementReminderReqModules(@RequestBody MeasurementReminderReqModule req){
        weekScheduleService.saveAll(req.getWeekScheduleDBList());
        cycleService.saveAll(req.getCycleDBList());
        measurementReminderService.saveAll(req.getMeasurementReminderDBList());
        reminderTimeService.saveAll(req.getReminderTimeDBList());
        measurementReminderEntryService.saveAll(req.getMeasurementReminderEntryDBList());

        return null;
    }
}
