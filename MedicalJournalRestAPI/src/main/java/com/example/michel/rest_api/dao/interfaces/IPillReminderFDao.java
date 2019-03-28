package com.example.michel.rest_api.dao.interfaces;

import com.example.michel.rest_api.models.auxiliary_models.CycleAndPillComby;
import com.example.michel.rest_api.models.pill.PillReminderF;

import java.util.List;
import java.util.UUID;

public interface IPillReminderFDao {

    public List<PillReminderF> getAllPillRemindersF(int userId);

    public CycleAndPillComby getCycleAndPillCombyByID(UUID prID, int userId);
}
