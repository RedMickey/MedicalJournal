package com.example.michel.rest_api.services;

import com.example.michel.rest_api.dao.implementations.PillReminderFDao;
import com.example.michel.rest_api.models.pill.PillReminderF;
import com.example.michel.rest_api.services.interfaces.IPillReminderFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PillReminderFService implements IPillReminderFService {

    @Autowired
    private PillReminderFDao pillReminderFDao;

    @Override
    public List<PillReminderF> getAllPillRemindersF() {
        return pillReminderFDao.getAllPillRemindersF();
    }
}
