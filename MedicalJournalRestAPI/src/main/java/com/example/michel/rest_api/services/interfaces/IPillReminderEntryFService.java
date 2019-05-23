package com.example.michel.rest_api.services.interfaces;

import com.example.michel.rest_api.models.auxiliary_models.request_bodies.UpdatePillReminderBody;
import com.example.michel.rest_api.models.pill.PillReminderEntryF;

import java.util.Date;
import java.util.List;

public interface IPillReminderEntryFService {

    public List<PillReminderEntryF> getPillReminderEntriesByDate(Date date, int userId);

    public int updateIsDonePillReminderEntry(UpdatePillReminderBody updatePillReminderBody);

    public PillReminderEntryF[] getChunkPillReminderEntries(Date startDate, Date endDate, int userId);
}
