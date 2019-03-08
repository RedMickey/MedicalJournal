package com.example.michel.rest_api.services;

import com.example.michel.rest_api.dao.implementations.MeasurementReminderFDao;
import com.example.michel.rest_api.models.measurement.MeasurementReminderF;
import com.example.michel.rest_api.models.pill.PillReminderF;
import com.example.michel.rest_api.services.interfaces.IMeasurementReminderFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasurementReminderFService implements IMeasurementReminderFService {
    @Autowired
    private MeasurementReminderFDao measurementReminderFDao;

    @Override
    public List<MeasurementReminderF> getAllMeasurementRemindersF() {
        return measurementReminderFDao.getAllMeasurementRemindersF();
    }
}
