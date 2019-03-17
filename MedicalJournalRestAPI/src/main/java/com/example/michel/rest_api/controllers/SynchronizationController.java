package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.MeasurementReminderEntry;
import com.example.michel.rest_api.models.PillReminderEntry;
import com.example.michel.rest_api.models.WeekSchedule;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.*;
import com.example.michel.rest_api.models.measurement.MeasurementReminderEntryF;
import com.example.michel.rest_api.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
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

    @Autowired
    private UserService userService;

    @PostMapping(value = "/synchronizeWeekSchedules", produces = "application/json")
    public Map synchronizeWeekSchedules(@RequestBody WeekScheduleS[] req){
        weekScheduleService.saveAll2(req);
        return null;
    }

    /*
    @PostMapping(value = "/synchronizePillReminderModules", produces = "application/json")
    public Map synchronizePillReminderModules(@RequestBody PillReminderReqModule req){
        List<Boolean> hasDeletion = new ArrayList<>();
        hasDeletion.add(weekScheduleService.updateOrDelete(req.getWeekScheduleDBList()));
        hasDeletion.add(cycleService.updateOrDelete(req.getCycleDBList()));
        hasDeletion.add(pillService.updateOrDelete(req.getPillDBList()));
        hasDeletion.add(pillReminderService.updateOrDelete(req.getPillReminderDBList()));
        hasDeletion.add(reminderTimeService.updateOrDelete(req.getReminderTimeDBList()));
        hasDeletion.add(pillReminderEntryService.updateOrDelete(req.getPillReminderEntryDBList()));

        return Collections.singletonMap("hasDeletion", hasDeletion.contains(true));
    }

    @PostMapping(value = "/synchronizeMeasurementReminderReqModules", produces = "application/json")
    public Map synchronizeMeasurementReminderReqModules(@RequestBody MeasurementReminderReqModule req){
        List<Boolean> hasDeletion = new ArrayList<>();
        hasDeletion.add(weekScheduleService.updateOrDelete(req.getWeekScheduleDBList()));
        hasDeletion.add(cycleService.updateOrDelete(req.getCycleDBList()));
        hasDeletion.add(measurementReminderService.updateOrDelete(req.getMeasurementReminderDBList()));
        hasDeletion.add(reminderTimeService.updateOrDelete(req.getReminderTimeDBList()));
        hasDeletion.add(measurementReminderEntryService.updateOrDelete(req.getMeasurementReminderEntryDBList()));

        return Collections.singletonMap("hasDeletion", hasDeletion.contains(true));
    }*/

    @PostMapping(value = "/synchronizeReminderReqModules", produces = "application/json")
    public SynchronizationReminderResp synchronizeMeasurementReminderReqModules(@RequestBody ReminderSynchronizationReqModule req){
        List<Boolean> hasDeletion = new ArrayList<>();

        hasDeletion.add(weekScheduleService.updateOrDelete(req.getWeekScheduleDBList()));
        hasDeletion.add(cycleService.updateOrDelete(req.getCycleDBList()));
        hasDeletion.add(pillService.updateOrDelete(req.getPillDBList()));
        hasDeletion.add(pillReminderService.updateOrDelete(req.getPillReminderDBList()));
        hasDeletion.add(measurementReminderService.updateOrDelete(req.getMeasurementReminderDBList()));
        hasDeletion.add(reminderTimeService.updateOrDelete(req.getReminderTimeDBList()));
        hasDeletion.add(pillReminderEntryService.updateOrDelete(req.getPillReminderEntryDBList()));
        hasDeletion.add(measurementReminderEntryService.updateOrDelete(req.getMeasurementReminderEntryDBList()));

        Date synchronizationTimestamp = userService.updateUserSynchronizationTime(req.getUserId());
        return new SynchronizationReminderResp(
                synchronizationTimestamp,
                hasDeletion.contains(true));
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

        Date synchronizationTimestamp = userService.updateUserSynchronizationTime(req.getUserId());
        return Collections.singletonMap("synchronizationTimestamp", synchronizationTimestamp);
    }

    /*@PostMapping(value = "/synchronizeMeasurementReminderEntry", produces = "application/json")
    public Map synchronizeMeasurementReminderEntry(@RequestBody MeasurementReminderEntry req){
        measurementReminderEntryService.save(req);

        return null;
    }

    @PostMapping(value = "/synchronizePillReminderEntry", produces = "application/json")
    public Map synchronizePillReminderEntry(@RequestBody PillReminderEntry req){
        pillReminderEntryService.save(req);

        return null;
    }*/

    @PostMapping(value = "/synchronizeReminderEntries", produces = "application/json")
    public Map synchronizeReminderEntries(@RequestBody ReminderEntriesReqModule req){
        pillReminderEntryService.saveAll(req.getPillReminderEntryDBList());
        measurementReminderEntryService.saveAll(req.getMeasurementReminderEntryDBList());

        Date synchronizationTimestamp = userService.updateUserSynchronizationTime(req.getUserId());
        return Collections.singletonMap("synchronizationTimestamp", synchronizationTimestamp);
    }

    @PostMapping(value = "/getAllDataForSynchronization", produces = "application/json")
    public ReminderSynchronizationReqModule getAllDataForSynchronization(@RequestBody SynchronizationReq req){
        Timestamp oldSynchronizationTimestamp = new Timestamp(req.getSynchronizationTimestamp().getTime());
        ReminderSynchronizationReqModule reminderSynchronizationReqModule = new ReminderSynchronizationReqModule();

        reminderSynchronizationReqModule.setCycleDBList(cycleService
                .getCycleDBEntriesForSynchronization(oldSynchronizationTimestamp, req.getUserId()));
        reminderSynchronizationReqModule.setMeasurementReminderDBList(measurementReminderService
                .getMeasurementRemindersForSynchronization(oldSynchronizationTimestamp, req.getUserId()));
        reminderSynchronizationReqModule.setMeasurementReminderEntryDBList(measurementReminderEntryService
                .getMeasurementReminderEntriesForSynchronization(oldSynchronizationTimestamp, req.getUserId()));
        reminderSynchronizationReqModule.setWeekScheduleDBList(weekScheduleService
                .getWeekSchedulesForSynchronization(oldSynchronizationTimestamp, req.getUserId()));
        reminderSynchronizationReqModule.setReminderTimeDBList(reminderTimeService
                .getReminderTimeForSynchronization(oldSynchronizationTimestamp, req.getUserId()));
        reminderSynchronizationReqModule.setPillDBList(pillService
                .getPillsForSynchronization(oldSynchronizationTimestamp, req.getUserId()));
        reminderSynchronizationReqModule.setPillReminderDBList(pillReminderService
                .getPillRemindersForSynchronization(oldSynchronizationTimestamp, req.getUserId()));
        reminderSynchronizationReqModule.setPillReminderEntryDBList(pillReminderEntryService
                .getPillReminderEntriesForSynchronization(oldSynchronizationTimestamp, req.getUserId()));

        reminderSynchronizationReqModule.setSynchronizationTimestamp(
                userService.updateUserSynchronizationTime(req.getUserId())
        );
        return reminderSynchronizationReqModule;
    }
}
