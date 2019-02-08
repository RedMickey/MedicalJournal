package com.example.michel.rest_api.services;

import com.example.michel.rest_api.dao.implementations.PillReminderEntryFDao;
import com.example.michel.rest_api.dao.interfaces.IPillReminderEntryFDao;
import com.example.michel.rest_api.models.auxiliary_models.UpdateReminderBody;
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

    /*@Autowired
    private IPillReminderEntryFDao pillReminderEntryFDao;*/

    @Override
    public List<PillReminderEntryF> getPillReminderEntriesByDate(Date date) {
       List<PillReminderEntryF> pillReminderEntryFList = pillReminderEntryFDao.getPillReminderEntriesByDate(date);
       pillReminderEntryFList.sort((o1, o2) ->
               o1.getDate().compareTo(o2.getDate()));
       return pillReminderEntryFList;
    }

    @Override
    public int updateIsDonePillReminderEntry(UpdateReminderBody updateReminderBody) {
        return pillReminderEntryFDao.updateIsDonePillReminderEntry(updateReminderBody);
    }
}
