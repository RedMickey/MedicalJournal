package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.*;
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

    /*@PostMapping(value = "/synchronizeReminderReqModules", produces = "application/json")
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
    }*/

    @PostMapping(value = "/synchronizeReminderReqModules", produces = "application/json")
    public Map synchronizeReminderReqModules(@RequestBody ReminderSynchronizationReqModule req){

        weekScheduleService.saveAll(req.getWeekScheduleDBList());
        cycleService.saveAll(req.getCycleDBList());
        pillService.saveAll(req.getPillDBList());
        pillReminderService.saveAll(req.getPillReminderDBList());
        measurementReminderService.saveAll(req.getMeasurementReminderDBList());
        reminderTimeService.saveAll(req.getReminderTimeDBList());
        pillReminderEntryService.saveAll(req.getPillReminderEntryDBList());
        measurementReminderEntryService.saveAll(req.getMeasurementReminderEntryDBList());

        Date synchronizationTimestamp = userService.updateUserSynchronizationTime(req.getUserId());
        return Collections.singletonMap("synchronizationTimestamp", synchronizationTimestamp);
    }

    /*@PostMapping(value = "/synchronizeDeletion", produces = "application/json")
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
    }*/

    @PostMapping(value = "/synchronizeDeletion", produces = "application/json")
    public Map synchronizeDeletion(@RequestBody DeletionReqModule req){
        reminderTimeService.updateAndMarkAsDeleted(req.getReminderTimeIds());
        if (req.getType() == 1){
            pillReminderEntryService.updateAndMarkAsDeleted(req.getReminderEntriesIds());
            pillReminderService.updateAndMarkAsDeleted(req.getReminderIds());
        }
        else {
            measurementReminderEntryService.updateAndMarkAsDeleted(req.getReminderEntriesIds());
            measurementReminderService.updateAndMarkAsDeleted(req.getReminderIds());
        }
        cycleService.updateAndMarkAsDeleted(req.getCycleIds());
        weekScheduleService.updateAndMarkAsDeleted(req.getWeekScheduleIds());

        Date synchronizationTimestamp = userService.updateUserSynchronizationTime(req.getUserId());
        return Collections.singletonMap("synchronizationTimestamp", synchronizationTimestamp);
    }

    @PostMapping(value = "/synchronizeReminderEntries", produces = "application/json")
    public Map synchronizeReminderEntries(@RequestBody ReminderEntriesReqModule req){
        pillReminderEntryService.saveAll(req.getPillReminderEntryDBList());
        measurementReminderEntryService.saveAll(req.getMeasurementReminderEntryDBList());

        Date synchronizationTimestamp = userService.updateUserSynchronizationTime(req.getUserId());
        return Collections.singletonMap("synchronizationTimestamp", synchronizationTimestamp);
    }

    @PostMapping(value = "/getAllDataForSynchronization", produces = "application/json")
    public ReminderSynchronizationReqModule[] getAllDataForSynchronization(@RequestBody SynchronizationReq req){
        Timestamp oldSynchronizationTimestamp = new Timestamp(req.getSynchronizationTimestamp().getTime());
        ReminderSynchronizationReqModule rsrmUpAndIn = new ReminderSynchronizationReqModule();
        ReminderSynchronizationReqModule rsrmDel = new ReminderSynchronizationReqModule();

        Map<Boolean, List<Cycle>> cyclesMap = cycleService
                .getSeparatedCycleDBEntriesForSynchronization(oldSynchronizationTimestamp, req.getUserId());
        Map<Boolean, List<MeasurementReminder>> measurementRemindersMap = measurementReminderService
                .getSeparatedMeasurementRemindersForSynchronization(oldSynchronizationTimestamp, req.getUserId());
        Map<Boolean, List<MeasurementReminderEntry>> measurementReminderEntriesMap = measurementReminderEntryService
                .getSeparatedMeasurementReminderEntriesForSynchronization(oldSynchronizationTimestamp, req.getUserId());
        Map<Boolean, List<WeekSchedule>> weekSchedulesMap = weekScheduleService
                .getSeparatedWeekSchedulesForSynchronization(oldSynchronizationTimestamp, req.getUserId());
        Map<Boolean, List<ReminderTime>> reminderTimeMap = reminderTimeService
                .getSeparatedReminderTimeForSynchronization(oldSynchronizationTimestamp, req.getUserId());
        Map<Boolean, List<Pill>> pillsMap = pillService
                .getSeparatedPillsForSynchronization(oldSynchronizationTimestamp, req.getUserId());
        Map<Boolean, List<PillReminder>> pillRemindersMap = pillReminderService
                .getSeparatedPillRemindersForSynchronization(oldSynchronizationTimestamp, req.getUserId());
        Map<Boolean, List<PillReminderEntry>> pillReminderEntriesMap = pillReminderEntryService
                .getSeparatedPillReminderEntriesForSynchronization(oldSynchronizationTimestamp, req.getUserId());

        rsrmUpAndIn.setCycleDBList(cyclesMap.get(true));
        rsrmUpAndIn.setMeasurementReminderDBList(measurementRemindersMap.get(true));
        rsrmUpAndIn.setMeasurementReminderEntryDBList(measurementReminderEntriesMap.get(true));
        rsrmUpAndIn.setWeekScheduleDBList(weekSchedulesMap.get(true));
        rsrmUpAndIn.setReminderTimeDBList(reminderTimeMap.get(true));
        rsrmUpAndIn.setPillDBList(pillsMap.get(true));
        rsrmUpAndIn.setPillReminderDBList(pillRemindersMap.get(true));
        rsrmUpAndIn.setPillReminderEntryDBList(pillReminderEntriesMap.get(true));

        if (req.getSynchronizationTimestamp().getTime()>10000){
            rsrmDel.setCycleDBList(cyclesMap.get(false));
            rsrmDel.setMeasurementReminderDBList(measurementRemindersMap.get(false));
            rsrmDel.setMeasurementReminderEntryDBList(measurementReminderEntriesMap.get(false));
            rsrmDel.setWeekScheduleDBList(weekSchedulesMap.get(false));
            rsrmDel.setReminderTimeDBList(reminderTimeMap.get(false));
            rsrmDel.setPillDBList(pillsMap.get(false));
            rsrmDel.setPillReminderDBList(pillRemindersMap.get(false));
            rsrmDel.setPillReminderEntryDBList(pillReminderEntriesMap.get(false));
        }

        Date synchronizationTimestamp = userService.updateUserSynchronizationTime(req.getUserId());
        rsrmUpAndIn.setSynchronizationTimestamp(synchronizationTimestamp);
        rsrmDel.setSynchronizationTimestamp(synchronizationTimestamp);

        return new ReminderSynchronizationReqModule[]{
                rsrmUpAndIn, rsrmDel
        };
    }
}
