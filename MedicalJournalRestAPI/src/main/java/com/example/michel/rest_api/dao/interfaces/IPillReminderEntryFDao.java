package com.example.michel.rest_api.dao.interfaces;

import com.example.michel.rest_api.models.auxiliary_models.UpdatePillReminderBody;
import com.example.michel.rest_api.models.pill.PillReminderEntryF;

import java.util.Date;
import java.util.List;

public interface IPillReminderEntryFDao {

    public List<PillReminderEntryF> getPillReminderEntriesByDate(Date date, int userId);

    public int updateIsDonePillReminderEntry(UpdatePillReminderBody updatePillReminderBody);
}
