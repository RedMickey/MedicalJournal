package com.example.michel.rest_api.services;

import com.example.michel.rest_api.dao.implementations.MeasurementReminderFDao;
import com.example.michel.rest_api.models.auxiliary_models.CycleAndMeasurementComby;
import com.example.michel.rest_api.models.measurement.MeasurementReminderF;
import com.example.michel.rest_api.models.pill.PillReminderF;
import com.example.michel.rest_api.services.interfaces.IMeasurementReminderFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MeasurementReminderFService implements IMeasurementReminderFService {
    @Autowired
    private MeasurementReminderFDao measurementReminderFDao;

    @Override
    public List<MeasurementReminderF> getAllMeasurementRemindersF(int userId) {
        return measurementReminderFDao.getAllMeasurementRemindersF(userId);
    }

    @Override
    public CycleAndMeasurementComby getCycleAndMeasurementCombyById(UUID mrID, int userId) {
        return measurementReminderFDao.getCycleAndMeasurementCombyById(mrID, userId);
    }
}
