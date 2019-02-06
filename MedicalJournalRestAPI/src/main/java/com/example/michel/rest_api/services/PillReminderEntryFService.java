package com.example.michel.rest_api.services;

import com.example.michel.rest_api.dao.implementations.PillReminderEntryFDao;
import com.example.michel.rest_api.models.pill.PillReminderEntryF;
import com.example.michel.rest_api.services.interfaces.IPillReminderEntryFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PillReminderEntryFService implements IPillReminderEntryFService {
    @Autowired
    private PillReminderEntryFDao pillReminderEntryFDao;

    @Override
    public List<PillReminderEntryF> getPillReminderEntriesByDate(Date date) {
       return pillReminderEntryFDao.getPillReminderEntriesByDate(date);
    }
}
