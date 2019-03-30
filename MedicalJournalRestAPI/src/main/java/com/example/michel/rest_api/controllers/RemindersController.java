package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.*;
import com.example.michel.rest_api.models.auxiliary_models.CycleAndMeasurementComby;
import com.example.michel.rest_api.models.auxiliary_models.CycleAndPillComby;
import com.example.michel.rest_api.models.auxiliary_models.MeasurementTypeF;
import com.example.michel.rest_api.models.auxiliary_models.request_bodies.DeletionReminderCourseBody;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.ReminderSynchronizationReqModule;
import com.example.michel.rest_api.models.auxiliary_models.synchronization.SynchronizationReq;
import com.example.michel.rest_api.models.measurement.MeasurementReminderF;
import com.example.michel.rest_api.models.pill.PillReminderF;
import com.example.michel.rest_api.services.*;
import com.example.michel.rest_api.utils.CalculationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;

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

    @Autowired
    private CalculationUtils calculationUtils;

    @PostMapping(value = "/getAllPillReminders", produces = "application/json")
    public List<PillReminderF> getAllPillReminders(){
        List<PillReminderF> pillReminderFList = pillReminderFService.getAllPillRemindersF(43);
        return pillReminderFList;
    }

    @PostMapping(value = "/getAllMeasurementReminders", produces = "application/json")
    public List<MeasurementReminderF> getAllMeasurementReminders(){
        List<MeasurementReminderF> measurementReminderFList = measurementReminderFService.getAllMeasurementRemindersF(43);
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

    /*******************************************************************Pill_Reminder*****************************************************************/
    @PostMapping(value = "/createPillReminderCourse", produces = "application/json")
    public void createPillReminderCourse(@RequestBody CycleAndPillComby req){
        if (req.getCycleDBInsertEntry().getWeekSchedule() != null){
            UUID wsId = weekScheduleService.createAndSaveWeekSchedule(req.getCycleDBInsertEntry().getWeekSchedule());
            req.getCycleDBInsertEntry().setIdWeekSchedule(wsId);
        }
        UUID cyId = cycleService.createAndSaveCycle(req.getCycleDBInsertEntry());
        UUID pillId = pillService.createAndSavePill(req.getPillReminderCourse().getPillName(), "");
        req.getPillReminderCourse().setIdCycle(cyId);
        UUID prId = pillReminderService.createAndSavePillReminder(req.getPillReminderCourse(), pillId, 0);

        for(int i=0; i<req.getPillReminderCourse().getReminderTimes().length; i++)
            reminderTimeService.createAndSaveReminderTime(req.getPillReminderCourse().getReminderTimes()[i],
                prId, 1);

        int perDayCount = calculationUtils.calculateDayCount(req.getCycleDBInsertEntry().getPeriod(),
                req.getCycleDBInsertEntry().getPeriodDMType());
        int interDayCount = Integer.MAX_VALUE;
        if (req.getCycleDBInsertEntry().getIdCyclingType() == 3)
            interDayCount = calculationUtils.calculateDayCount(req.getCycleDBInsertEntry().getOnceAPeriod(),
                    req.getCycleDBInsertEntry().getOnceAPeriodDMType());

        pillReminderEntryService.createAndSavePillReminderEntries(req.getPillReminderCourse().getStartDate(),
                req.getPillReminderCourse().getReminderTimes(), req.getCycleDBInsertEntry().getIdCyclingType(),
                perDayCount, prId, interDayCount, req.getCycleDBInsertEntry().getWeekSchedule());
    }

    @PostMapping(value = "/updatePillReminderCourse", produces = "application/json")
    public void updatePillReminderCourse(@RequestBody CycleAndPillComby req){
        if (req.getCycleDBInsertEntry().getWeekSchedule()!=null){
            if (req.getCycleDBInsertEntry().getIdWeekSchedule()!=null)
                weekScheduleService.updateWeekScheduleById(req.getCycleDBInsertEntry().getWeekSchedule(),
                        req.getCycleDBInsertEntry().getIdWeekSchedule());
            else {
                UUID wsId = weekScheduleService.createAndSaveWeekSchedule(req.getCycleDBInsertEntry().getWeekSchedule());
                req.getCycleDBInsertEntry().setIdWeekSchedule(wsId);
            }
        }
        cycleService.updateCycleById(req.getCycleDBInsertEntry());
        Pill prePill = pillService.getPillByPillReminderId(req.getPillReminderCourse().getIdPillReminder());
        UUID pillId = prePill.getIdPill();
        if (!req.getPillReminderCourse().getPillName().equals(prePill.getPillName()))
        {
            pillId = pillService.createAndSavePill(req.getPillReminderCourse().getPillName(), "");
        }
        pillReminderService.updatePillReminderById(req.getPillReminderCourse(), pillId, 0);
        pillReminderEntryService.updateAndMarkAsDeletedAfterDate(new Date(), req.getPillReminderCourse().getIdPillReminder());
        reminderTimeService.updateAndMarkAsDeletedByReminderId(req.getPillReminderCourse().getIdPillReminder(), 1);

        for(int i=0; i<req.getPillReminderCourse().getReminderTimes().length; i++)
            reminderTimeService.createAndSaveReminderTime(req.getPillReminderCourse().getReminderTimes()[i],
                    req.getPillReminderCourse().getIdPillReminder(), 1);

        if (req.getPillReminderCourse().getIsActive() == 1){
            int perDayCount = calculationUtils.calculateDayCount(req.getCycleDBInsertEntry().getPeriod(),
                    req.getCycleDBInsertEntry().getPeriodDMType());
            int interDayCount = Integer.MAX_VALUE;
            if (req.getCycleDBInsertEntry().getIdCyclingType() == 3)
                interDayCount = calculationUtils.calculateDayCount(req.getCycleDBInsertEntry().getOnceAPeriod(),
                        req.getCycleDBInsertEntry().getOnceAPeriodDMType());
            pillReminderEntryService.createAndSavePillReminderEntries(req.getPillReminderCourse().getStartDate(),
                    req.getPillReminderCourse().getReminderTimes(), req.getCycleDBInsertEntry().getIdCyclingType(),
                    perDayCount, req.getPillReminderCourse().getIdPillReminder(), interDayCount,
                    req.getCycleDBInsertEntry().getWeekSchedule());
        }
    }

    @PostMapping(value = "/getPillReminderCourse", produces = "application/json")
    public CycleAndPillComby getPillReminderCourse(@RequestBody Map<String, UUID> req){
        UUID prId = req.get("pillReminderId");
        CycleAndPillComby cycleAndPillComby = pillReminderFService.getCycleAndPillCombyByID(prId, 43);
        cycleAndPillComby.getPillReminderCourse().setReminderTimes(reminderTimeService
                .getReminderTimeDateArrByReminderId(prId , 1));
        if (cycleAndPillComby.getCycleDBInsertEntry().getIdWeekSchedule() != null)
            cycleAndPillComby.getCycleDBInsertEntry().setWeekSchedule(
                    weekScheduleService.getWeekScheduleAsBoolArrById(cycleAndPillComby
                            .getCycleDBInsertEntry().getIdWeekSchedule()));

        return cycleAndPillComby;
    }

    /*******************************************************************Measurement_Reminder*****************************************************************/
    @PostMapping(value = "/createMeasurementReminderCourse", produces = "application/json")
    public void createMeasurementReminderCourse(@RequestBody CycleAndMeasurementComby req){
        if (req.getCycleDBInsertEntry().getWeekSchedule() != null){
            UUID wsId = weekScheduleService.createAndSaveWeekSchedule(req.getCycleDBInsertEntry().getWeekSchedule());
            req.getCycleDBInsertEntry().setIdWeekSchedule(wsId);
        }
        UUID cyId = cycleService.createAndSaveCycle(req.getCycleDBInsertEntry());
        req.getMeasurementReminderCourse().setIdCycle(cyId);
        UUID mrId = measurementReminderService.createAndSaveMeasurementReminder(req.getMeasurementReminderCourse(), 0);

        for(int i=0; i<req.getMeasurementReminderCourse().getReminderTimes().length; i++)
            reminderTimeService.createAndSaveReminderTime(req.getMeasurementReminderCourse().getReminderTimes()[i],
                    mrId, 2);

        int perDayCount = calculationUtils.calculateDayCount(req.getCycleDBInsertEntry().getPeriod(),
                req.getCycleDBInsertEntry().getPeriodDMType());
        int interDayCount = Integer.MAX_VALUE;
        if (req.getCycleDBInsertEntry().getIdCyclingType() == 3)
            interDayCount = calculationUtils.calculateDayCount(req.getCycleDBInsertEntry().getOnceAPeriod(),
                    req.getCycleDBInsertEntry().getOnceAPeriodDMType());

        measurementReminderEntryService.createAndSaveMeasurementReminderEntries(
                req.getMeasurementReminderCourse().getStartDate(),
                req.getMeasurementReminderCourse().getReminderTimes(), req.getCycleDBInsertEntry().getIdCyclingType(),
                perDayCount, mrId, interDayCount, req.getCycleDBInsertEntry().getWeekSchedule());
    }

    @PostMapping(value = "/updateMeasurementReminderCourse", produces = "application/json")
    public void updateMeasurementReminderCourse(@RequestBody CycleAndMeasurementComby req){
        if (req.getCycleDBInsertEntry().getWeekSchedule()!=null){
            if (req.getCycleDBInsertEntry().getIdWeekSchedule()!=null)
                weekScheduleService.updateWeekScheduleById(req.getCycleDBInsertEntry().getWeekSchedule(),
                        req.getCycleDBInsertEntry().getIdWeekSchedule());
            else {
                UUID wsId = weekScheduleService.createAndSaveWeekSchedule(req.getCycleDBInsertEntry().getWeekSchedule());
                req.getCycleDBInsertEntry().setIdWeekSchedule(wsId);
            }
        }
        measurementReminderService.updateMeasurementReminderById(req.getMeasurementReminderCourse(), 0);
        cycleService.updateCycleById(req.getCycleDBInsertEntry());
        measurementReminderEntryService.updateAndMarkAsDeletedAfterDate(new Date(),
                req.getMeasurementReminderCourse().getIdMeasurementReminder());
        reminderTimeService.updateAndMarkAsDeletedByReminderId(req.getMeasurementReminderCourse().getIdMeasurementReminder(), 2);

        for(int i=0; i<req.getMeasurementReminderCourse().getReminderTimes().length; i++)
            reminderTimeService.createAndSaveReminderTime(req.getMeasurementReminderCourse().getReminderTimes()[i],
                    req.getMeasurementReminderCourse().getIdMeasurementReminder(), 2);

        if (req.getMeasurementReminderCourse().getIsActive() == 1){
            int perDayCount = calculationUtils.calculateDayCount(req.getCycleDBInsertEntry().getPeriod(),
                    req.getCycleDBInsertEntry().getPeriodDMType());
            int interDayCount = Integer.MAX_VALUE;
            if (req.getCycleDBInsertEntry().getIdCyclingType() == 3)
                interDayCount = calculationUtils.calculateDayCount(req.getCycleDBInsertEntry().getOnceAPeriod(),
                        req.getCycleDBInsertEntry().getOnceAPeriodDMType());
            measurementReminderEntryService.createAndSaveMeasurementReminderEntries(
                    req.getMeasurementReminderCourse().getStartDate(),
                    req.getMeasurementReminderCourse().getReminderTimes(), req.getCycleDBInsertEntry().getIdCyclingType(),
                    perDayCount, req.getMeasurementReminderCourse().getIdMeasurementReminder(), interDayCount,
                    req.getCycleDBInsertEntry().getWeekSchedule());
        }
    }

    @PostMapping(value = "/getMeasurementReminderCourse", produces = "application/json")
    public CycleAndMeasurementComby getMeasurementReminderCourse(@RequestBody Map<String, UUID> req){
        UUID mrId = req.get("measurementReminderId");
        CycleAndMeasurementComby cycleAndMeasurementComby = measurementReminderFService.getCycleAndMeasurementCombyById(mrId, 43);
        cycleAndMeasurementComby.getMeasurementReminderCourse().setReminderTimes(reminderTimeService
                .getReminderTimeDateArrByReminderId(mrId , 2));
        if (cycleAndMeasurementComby.getCycleDBInsertEntry().getIdWeekSchedule() != null)
            cycleAndMeasurementComby.getCycleDBInsertEntry().setWeekSchedule(
                    weekScheduleService.getWeekScheduleAsBoolArrById(cycleAndMeasurementComby
                            .getCycleDBInsertEntry().getIdWeekSchedule()));

        return cycleAndMeasurementComby;
    }


    @PostMapping(value = "/deleteReminderCourse", produces = "application/json")
    public void deleteReminderCourse(@RequestBody DeletionReminderCourseBody req){
        if (req.getCourseType() == 1){
            pillReminderEntryService.updateAndMarkAsDeletedByPillReminderId(req.getIdReminder());
            reminderTimeService.updateAndMarkAsDeletedByReminderId(req.getIdReminder(), 1);
            if (req.getIdWeekSchedule()!=null)
                weekScheduleService.updateAndMarkAsDeletedById(req.getIdWeekSchedule());
            cycleService.updateAndMarkAsDeletedById(req.getIdCycle());
            pillReminderService.updateAndMarkAsDeletedById(req.getIdReminder());
        }
        else {
            measurementReminderEntryService.updateAndMarkAsDeletedByMeasurementReminderId(
                    req.getIdReminder());
            reminderTimeService.updateAndMarkAsDeletedByReminderId(req.getIdReminder(), 2);
            if (req.getIdWeekSchedule()!=null)
                weekScheduleService.updateAndMarkAsDeletedById(req.getIdWeekSchedule());
            cycleService.updateAndMarkAsDeletedById(req.getIdCycle());
            measurementReminderService.updateAndMarkAsDeletedById(req.getIdReminder());
        }
    }

}
