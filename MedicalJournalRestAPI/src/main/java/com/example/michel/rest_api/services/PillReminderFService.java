package com.example.michel.rest_api.services;

import com.example.michel.rest_api.dao.implementations.PillReminderFDao;
import com.example.michel.rest_api.models.auxiliary_models.CycleAndPillComby;
import com.example.michel.rest_api.models.pill.PillReminderF;
import com.example.michel.rest_api.services.interfaces.IPillReminderFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PillReminderFService implements IPillReminderFService {

    @Autowired
    private PillReminderFDao pillReminderFDao;

    @Override
    public List<PillReminderF> getAllPillRemindersF(int userId) {
        return pillReminderFDao.getAllPillRemindersF(userId);
    }

    @Override
    public CycleAndPillComby getCycleAndPillCombyByID(UUID prID, int userId) {
        return pillReminderFDao.getCycleAndPillCombyByID(prID, userId);
    }
}
