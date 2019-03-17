package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.Cycle;
import com.example.michel.rest_api.models.PillReminder;
import com.example.michel.rest_api.models.PillReminderEntry;
import com.example.michel.rest_api.models.WeekSchedule;
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
import java.util.List;

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

    /*@PostMapping(value = "/synchronizeTest", produces = "application/json")
    public List<PillReminderEntry> synchronizeTest(){
        Calendar cal = Calendar.getInstance();
        cal.set(2019, 2, 17, 12, 00);
        return pillReminderEntryService.getPillReminderEntriesForSynchronization(43,
                new Timestamp(cal.getTimeInMillis()));
    }*/

    /*@PostMapping(value = "/synchronizeTest2", produces = "application/json")
    public List<PillReminder> synchronizeTest2(){
        Calendar cal = Calendar.getInstance();
        cal.set(2019, 2, 13, 12, 00);
        return pillReminderService.getPillRemindersForSynchronization(
                new Timestamp(cal.getTimeInMillis()), 43);
    }

    @PostMapping(value = "/synchronizeTest3", produces = "application/json")
    public List<Cycle> synchronizeTest3(){
        Calendar cal = Calendar.getInstance();
        cal.set(2019, 2, 17, 12, 00);
        return cycleService.getCycleDBEntriesForSynchronization(
                new Timestamp(cal.getTimeInMillis()), 43);
    }

    @PostMapping(value = "/synchronizeTest4", produces = "application/json")
    public List<WeekSchedule> synchronizeTest4(){
        Calendar cal = Calendar.getInstance();
        cal.set(2019, 2, 15, 18, 00);
        return weekScheduleService.getWeekSchedulesForSynchronization(
                new Timestamp(cal.getTimeInMillis()), 43);
    }*/

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
