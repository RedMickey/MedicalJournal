package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.auxiliary_models.MeasurementTypeF;
import com.example.michel.rest_api.models.measurement.MeasurementReminderF;
import com.example.michel.rest_api.models.pill.PillReminderF;
import com.example.michel.rest_api.services.MeasurementReminderFService;
import com.example.michel.rest_api.services.MeasurementTypeService;
import com.example.michel.rest_api.services.PillReminderFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
