package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.auxiliary_models.UpdateReminderBody;
import com.example.michel.rest_api.models.pill.PillReminderEntryF;
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

    @PostMapping(value = "/getReminders", produces = "application/json")
    public List<PillReminderEntryF> getReminders(@RequestBody Map<String, Date> req){
        Date date = req.get("date");

        List<PillReminderEntryF> pillReminderEntryFList = pillReminderEntryFService.getPillReminderEntriesByDate(date);
        return pillReminderEntryFList;
    }

    @PostMapping(value = "/updateReminder", produces = "application/json")
    public void updateReminder(@RequestBody UpdateReminderBody updateReminderBody){
        int r = pillReminderEntryFService.updateIsDonePillReminderEntry(updateReminderBody);
    }
}
