package com.example.michel.rest_api.controllers;

import com.example.michel.rest_api.models.auxiliary_models.PillAndMeasurementReminderEntriesF;
import com.example.michel.rest_api.models.auxiliary_models.request_bodies.HistoryBody;
import com.example.michel.rest_api.models.measurement.MeasurementReminderEntryF;
import com.example.michel.rest_api.models.pill.PillReminderEntryF;
import com.example.michel.rest_api.services.MeasurementReminderEntryFService;
import com.example.michel.rest_api.services.PillReminderEntryFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/history")
public class HistoryController {
    @Autowired
    private PillReminderEntryFService pillReminderEntryFService;

    @Autowired
    private MeasurementReminderEntryFService measurementReminderEntryFService;

    @PostMapping(value = "/getHistoryForPeriod", produces = "application/json")
    public List<PillAndMeasurementReminderEntriesF> getHistoryForPeriod(@RequestBody HistoryBody req){
        PillReminderEntryF[] pillReminderEntries = pillReminderEntryFService.getChunkPillReminderEntries(
                req.getStartDate(), req.getEndDate(), req.getUserId());
        MeasurementReminderEntryF[] measurementReminderEntries = measurementReminderEntryFService.getChunkMeasurementReminderEntries(
                req.getStartDate(), req.getEndDate(), req.getUserId());

        List<PillAndMeasurementReminderEntriesF> pillAndMeasurementReminderEntriesList = new ArrayList<PillAndMeasurementReminderEntriesF>();
        if (measurementReminderEntries.length>0||pillReminderEntries.length>0) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            int ip=0, im=0;

            if (measurementReminderEntries.length>0&&pillReminderEntries.length>0){
                cal1.setTime(measurementReminderEntries[measurementReminderEntries.length-1].getDate().
                        compareTo(pillReminderEntries[pillReminderEntries.length-1].getDate())<0?
                        measurementReminderEntries[measurementReminderEntries.length-1].getDate():
                        pillReminderEntries[pillReminderEntries.length-1].getDate());
                cal2.setTime(measurementReminderEntries[0].getDate().
                        compareTo(pillReminderEntries[0].getDate())>0?
                        measurementReminderEntries[0].getDate():
                        pillReminderEntries[0].getDate());
            }
            else if(measurementReminderEntries.length>0){
                cal1.setTime(measurementReminderEntries[measurementReminderEntries.length-1].getDate());
                cal2.setTime(measurementReminderEntries[0].getDate());
            }
            else {
                cal1.setTime(pillReminderEntries[pillReminderEntries.length-1].getDate());
                cal2.setTime(pillReminderEntries[0].getDate());
            }

            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);
            Date endDate = cal1.getTime();

            while (cal2.getTime().compareTo(endDate)>=0){
                PillAndMeasurementReminderEntriesF pamre = new PillAndMeasurementReminderEntriesF();

                if (ip<pillReminderEntries.length){
                    cal1.setTime(pillReminderEntries[ip].getDate());
                    while (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)&&
                            cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)) {
                        pamre.pillReminderEntries.add(pillReminderEntries[ip]);
                        ip++;
                        try {
                            cal1.setTime(pillReminderEntries[ip].getDate());
                        }catch (ArrayIndexOutOfBoundsException aiobe){
                            break;
                        }
                    }
                }

                if (im<measurementReminderEntries.length){
                    cal1.setTime(measurementReminderEntries[im].getDate());
                    while (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)&&
                            cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)) {
                        pamre.measurementReminderEntries.add(measurementReminderEntries[im]);
                        //if (im<measurementReminderEntries.length)
                        im++;
                        try {
                            cal1.setTime(measurementReminderEntries[im].getDate());
                        }
                        catch (ArrayIndexOutOfBoundsException aiobe){
                            break;
                        }
                    }
                }

                if (pamre.measurementReminderEntries.size() != 0 || pamre.pillReminderEntries.size() != 0)
                    pillAndMeasurementReminderEntriesList.add(pamre);

                cal2.add(Calendar.DAY_OF_MONTH, -1);
            }

        }
        return pillAndMeasurementReminderEntriesList;
    }
}
