package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.MeasurementReminderEntry;
import com.example.michel.rest_api.models.PillReminderEntry;
import com.example.michel.rest_api.models.WeekSchedule;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.DeletionReqModule;
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

import java.util.*;

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
        List<Boolean> hasDeletion = new ArrayList<>();
        if (req.getType() == 1){
            weekScheduleService.saveAll(req.getWeekScheduleDBList());
            cycleService.saveAll(req.getCycleDBList());
            pillService.saveAll(req.getPillDBList());
            pillReminderService.saveAll(req.getPillReminderDBList());
            reminderTimeService.saveAll(req.getReminderTimeDBList());
            pillReminderEntryService.saveAll(req.getPillReminderEntryDBList());
        }
        else {
            hasDeletion.add(weekScheduleService.updateOrDelete(req.getWeekScheduleDBList()));
            hasDeletion.add(cycleService.updateOrDelete(req.getCycleDBList()));
            hasDeletion.add(pillService.updateOrDelete(req.getPillDBList()));
            hasDeletion.add(pillReminderService.updateOrDelete(req.getPillReminderDBList()));
            hasDeletion.add(reminderTimeService.updateOrDelete(req.getReminderTimeDBList()));
            hasDeletion.add(pillReminderEntryService.updateOrDelete(req.getPillReminderEntryDBList()));
        }

        return Collections.singletonMap("hasDeletion", hasDeletion.contains(true));
    }

    @PostMapping(value = "/synchronizeMeasurementReminderReqModules", produces = "application/json")
    public Map synchronizeMeasurementReminderReqModules(@RequestBody MeasurementReminderReqModule req){
        List<Boolean> hasDeletion = new ArrayList<>();
        if (req.getType() == 1){
            weekScheduleService.saveAll(req.getWeekScheduleDBList());
            cycleService.saveAll(req.getCycleDBList());
            measurementReminderService.saveAll(req.getMeasurementReminderDBList());
            reminderTimeService.saveAll(req.getReminderTimeDBList());
            measurementReminderEntryService.saveAll(req.getMeasurementReminderEntryDBList());
        }
        else {
            hasDeletion.add(weekScheduleService.updateOrDelete(req.getWeekScheduleDBList()));
            hasDeletion.add(cycleService.updateOrDelete(req.getCycleDBList()));
            hasDeletion.add(measurementReminderService.updateOrDelete(req.getMeasurementReminderDBList()));
            hasDeletion.add(reminderTimeService.updateOrDelete(req.getReminderTimeDBList()));
            hasDeletion.add(measurementReminderEntryService.updateOrDelete(req.getMeasurementReminderEntryDBList()));
        }

        return Collections.singletonMap("hasDeletion", hasDeletion.contains(true));
    }

    @PostMapping(value = "/synchronizeDeletion", produces = "application/json")
    public Map synchronizeDeletion(@RequestBody DeletionReqModule req){
        reminderTimeService.deleteAllByIds(req.getReminderTimeIds());
        if (req.getType() == 1){
            pillReminderEntryService.deleteAllByIds(req.getReminderEntriesIds());
            pillReminderService.deleteAllByIds(req.getReminderIds());
        }
        else {
            measurementReminderEntryService.deleteAllByIds(req.getReminderEntriesIds());
            measurementReminderService.deleteAllByIds(req.getReminderIds());
        }
        cycleService.deleteAllByIds(req.getCycleIds());
        weekScheduleService.deleteAllByIds(req.getWeekScheduleIds());

        return null;
    }

    @PostMapping(value = "/synchronizeMeasurementReminderEntry", produces = "application/json")
    public Map synchronizeMeasurementReminderEntry(@RequestBody MeasurementReminderEntry req){
        measurementReminderEntryService.save(req);

        return null;
    }

    @PostMapping(value = "/synchronizePillReminderEntry", produces = "application/json")
    public Map synchronizePillReminderEntry(@RequestBody PillReminderEntry req){
        pillReminderEntryService.save(req);

        return null;
    }
}
