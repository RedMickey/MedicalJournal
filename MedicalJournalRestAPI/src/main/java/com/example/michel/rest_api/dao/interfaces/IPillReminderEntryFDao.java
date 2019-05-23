package com.example.michel.rest_api.dao.interfaces;

import com.example.michel.rest_api.models.auxiliary_models.request_bodies.UpdatePillReminderBody;
import com.example.michel.rest_api.models.pill.PillReminderEntryF;

import java.util.Date;
import java.util.List;

public interface IPillReminderEntryFDao {

    public List<PillReminderEntryF> getPillReminderEntriesByDate(Date date, int userId);

    public int updateIsDonePillReminderEntry(UpdatePillReminderBody updatePillReminderBody);

    public List<PillReminderEntryF> getChunkPillReminderEntries(Date startDate, Date endDate, int userId);
}
