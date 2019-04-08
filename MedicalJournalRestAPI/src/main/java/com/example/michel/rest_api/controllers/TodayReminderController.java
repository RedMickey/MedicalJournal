package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.auxiliary_models.request_bodies.DateBody;
import com.example.michel.rest_api.models.auxiliary_models.request_bodies.UpdateMeasurementReminderBody;
import com.example.michel.rest_api.models.auxiliary_models.request_bodies.UpdatePillReminderBody;
import com.example.michel.rest_api.models.measurement.MeasurementReminderEntryF;
import com.example.michel.rest_api.models.pill.PillReminderEntryF;
import com.example.michel.rest_api.services.MeasurementReminderEntryFService;
import com.example.michel.rest_api.services.PillReminderEntryFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/today")
public class TodayReminderController {

    @Autowired
    private PillReminderEntryFService pillReminderEntryFService;

    @Autowired
    private MeasurementReminderEntryFService measurementReminderEntryFService;

    @PostMapping(value = "/getPillReminders", produces = "application/json")
    public List<PillReminderEntryF> getPillReminders(@RequestBody DateBody req){
        Date date = req.getDate();
        List<PillReminderEntryF> pillReminderEntryFList = pillReminderEntryFService.getPillReminderEntriesByDate(date, req.getUserId());
        return pillReminderEntryFList;
    }

    @PostMapping(value = "/getMeasurementReminders", produces = "application/json")
    public List<MeasurementReminderEntryF> getMeasurementReminders(@RequestBody DateBody req){
        Date date = req.getDate();
        List<MeasurementReminderEntryF> measurementReminderEntryFList = measurementReminderEntryFService
                .getMeasurementReminderEntriesByDate(date, req.getUserId());
        return measurementReminderEntryFList;
    }

    @PostMapping(value = "/updatePillReminder", produces = "application/json")
    public void updatePillReminder(@RequestBody UpdatePillReminderBody updatePillReminderBody){
        int r = pillReminderEntryFService.updateIsDonePillReminderEntry(updatePillReminderBody);
    }

    @PostMapping(value = "/updateMeasurementReminder", produces = "application/json")
    public void updateMeasurementReminder(@RequestBody UpdateMeasurementReminderBody updateMeasurementReminderBody){
        int r = measurementReminderEntryFService.updateIsDoneMeasurementReminderEntry(updateMeasurementReminderBody);
    }
}
