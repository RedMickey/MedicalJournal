package com.example.michel.rest_api.services;

import com.example.michel.rest_api.dao.implementations.MeasurementReminderEntryFDao;
import com.example.michel.rest_api.dao.implementations.PillReminderEntryFDao;
import com.example.michel.rest_api.models.auxiliary_models.UpdateMeasurementReminderBody;
import com.example.michel.rest_api.models.measurement.MeasurementReminderEntryF;
import com.example.michel.rest_api.services.interfaces.IMeasurementReminderEntryFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MeasurementReminderEntryFService implements IMeasurementReminderEntryFService {
    @Autowired
    private MeasurementReminderEntryFDao measurementReminderEntryFDao;

    @Override
    public List<MeasurementReminderEntryF> getMeasurementReminderEntriesByDate(Date date, int userId) {
        List<MeasurementReminderEntryF> measurementReminderEntryFList = measurementReminderEntryFDao.getMeasurementReminderEntriesByDate(date, userId);
        measurementReminderEntryFList.sort((o1, o2) ->
                o1.getDate().compareTo(o2.getDate()));
        return measurementReminderEntryFList;
    }

    @Override
    public int updateIsDoneMeasurementReminderEntry(UpdateMeasurementReminderBody updateMeasurementReminderBody) {
        return measurementReminderEntryFDao.updateIsDoneMeasurementReminderEntry(updateMeasurementReminderBody);
    }

}
