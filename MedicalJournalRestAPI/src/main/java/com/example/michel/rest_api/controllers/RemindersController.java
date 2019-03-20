package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.*;
import com.example.michel.rest_api.models.auxiliary_models.MeasurementTypeF;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.ReminderSynchronizationReqModule;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.SynchronizationReq;
import com.example.michel.rest_api.models.measurement.MeasurementReminderF;
import com.example.michel.rest_api.models.pill.PillReminderF;
import com.example.michel.rest_api.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reminders")
public class RemindersController {
    @Autowired
    private PillReminderFService pillReminderFService;

    @Autowired
    private MeasurementReminderFService measurementReminderFService;

    @Autowired
    private MeasurementTypeService measurementTypeService;

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

    @PostMapping(value = "/getAllPillReminders", produces = "application/json")
    public List<PillReminderF> getAllPillReminders(){
        List<PillReminderF> pillReminderFList = pillReminderFService.getAllPillRemindersF();
        return pillReminderFList;
    }

    @PostMapping(value = "/getAllMeasurementReminders", produces = "application/json")
    public List<MeasurementReminderF> getAllMeasurementReminders(){
        List<MeasurementReminderF> measurementReminderFList = measurementReminderFService.getAllMeasurementRemindersF();
        return measurementReminderFList;
    }

    @PostMapping(value = "/getMeasurementTypes", produces = "application/json")
    public List<MeasurementTypeF> getMeasurementTypes(){
        List<MeasurementTypeF> measurementTypeFList = measurementTypeService.findAllF();
        return measurementTypeFList;
    }

    /*@PostMapping(value = "/synchronizeTest4", produces = "application/json")
    public List<WeekSchedule> synchronizeTest4(){
        Calendar cal = Calendar.getInstance();
        cal.set(2019, 2, 15, 18, 00);
        return weekScheduleService.getWeekSchedulesForSynchronization(
                new Timestamp(cal.getTimeInMillis()), 43);
    }*/

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

        rsrmDel.setCycleDBList(cyclesMap.get(false));
        rsrmDel.setMeasurementReminderDBList(measurementRemindersMap.get(false));
        rsrmDel.setMeasurementReminderEntryDBList(measurementReminderEntriesMap.get(false));
        rsrmDel.setWeekScheduleDBList(weekSchedulesMap.get(false));
        rsrmDel.setReminderTimeDBList(reminderTimeMap.get(false));
        rsrmDel.setPillDBList(pillsMap.get(false));
        rsrmDel.setPillReminderDBList(pillRemindersMap.get(false));
        rsrmDel.setPillReminderEntryDBList(pillReminderEntriesMap.get(false));

        Date synchronizationTimestamp = userService.updateUserSynchronizationTime(req.getUserId());
        rsrmUpAndIn.setSynchronizationTimestamp(synchronizationTimestamp);
        rsrmDel.setSynchronizationTimestamp(synchronizationTimestamp);

        return new ReminderSynchronizationReqModule[]{
                rsrmUpAndIn, rsrmDel
        };
    }
}
