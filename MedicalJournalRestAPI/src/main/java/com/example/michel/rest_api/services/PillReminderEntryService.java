package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.PillReminderEntry;
import com.example.michel.rest_api.repositories.PillReminderEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PillReminderEntryService {
    @Autowired
    private PillReminderEntryRepository pillReminderEntryRepository;

    public Iterable<PillReminderEntry> saveAll(List<PillReminderEntry> pillReminderEntryList) {
        return pillReminderEntryRepository.saveAll(pillReminderEntryList);
    }

    public PillReminderEntry save(PillReminderEntry pillReminderEntry){
        return pillReminderEntryRepository.save(pillReminderEntry);
    }

    public boolean updateOrDelete(List<PillReminderEntry> pillReminderEntryList){
        boolean hasDeletion = false;
        for (PillReminderEntry pillReminderEntry: pillReminderEntryList) {
            if (pillReminderEntry.getChangeType()<3)
                pillReminderEntryRepository.save(pillReminderEntry);
            else{
                pillReminderEntryRepository.delete(pillReminderEntry);
                hasDeletion = true;
            }
        }
        return hasDeletion;
    }

    public void deleteAllByIds(List<UUID> uuidList){
        uuidList.forEach(id -> pillReminderEntryRepository.deleteById(id));
    }

    @Transactional
    public List<PillReminderEntry> getPillReminderEntriesForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<PillReminderEntry> pillReminderEntries = pillReminderEntryRepository
                .getPillReminderEntriesForSynchronization( synchronizationTimestamp, userId);
        pillReminderEntries.forEach(pre -> pre.setReminderTime(new Time(pre.getReminderDate().getTime())));
        return pillReminderEntries;
    }

    @Transactional
    public Map<Boolean, List<PillReminderEntry>> getSeparatedPillReminderEntriesForSynchronization(
            Timestamp synchronizationTimestamp, Integer userId){
        List<PillReminderEntry> pillReminderEntries = pillReminderEntryRepository
                .getPillReminderEntriesForSynchronization( synchronizationTimestamp, userId);
        return pillReminderEntries.parallelStream().peek(pre -> pre.setReminderTime(new Time(pre.getReminderDate().getTime())))
                .collect(Collectors.partitioningBy(pre -> pre.getChangeType()<3));
    }

    @Transactional
    public void updateAndMarkAsDeleted(List<UUID> uuidList) {
        Timestamp synchronizationTimestamp = new Timestamp(new Date().getTime());
        uuidList.forEach(id -> pillReminderEntryRepository.updateAndMarkAsDeletedById(id, synchronizationTimestamp));
    }

    public UUID createAndSavePillReminderEntry(Date reminderDate, UUID idPillReminder, int isOneTime){
        UUID id = UUID.randomUUID();
        PillReminderEntry pillReminderEntry = new PillReminderEntry(id,
                0, reminderDate, idPillReminder, null, isOneTime,
                new Timestamp(new Date().getTime()), 1);
        pillReminderEntryRepository.save(pillReminderEntry);
        return id;
    }

    public void createAndSavePillReminderEntries(Date startDate, Date[] reminderDates, int IdCyclingType, int perDayCount, UUID idPillReminder,
        int interDayCount, boolean[] weekSchedule){
        Calendar cal = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal.setTime(startDate);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        switch (IdCyclingType){
            case 1:
                for(int i=0; i<perDayCount;i++){
                    for (int j=0; j<reminderDates.length; j++){
                        cal2.setTime(reminderDates[j]);
                        cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
                        cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
                        createAndSavePillReminderEntry(cal.getTime(), idPillReminder, 0);
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 2:
                for(int i=0; i<perDayCount;i++){
                    if(weekSchedule[cal.get(Calendar.DAY_OF_WEEK)-1]){
                        for (int j=0; j<reminderDates.length; j++){
                            cal2.setTime(reminderDates[j]);
                            cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
                            cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
                            createAndSavePillReminderEntry(cal.getTime(), idPillReminder, 0);
                        }
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 3:
                for(int i=0; i<perDayCount;i+=interDayCount){
                    for (int j=0; j<reminderDates.length; j++){
                        cal2.setTime(reminderDates[j]);
                        cal.set(Calendar.HOUR_OF_DAY, cal2.get(Calendar.HOUR_OF_DAY));
                        cal.set(Calendar.MINUTE, cal2.get(Calendar.MINUTE));
                        createAndSavePillReminderEntry(cal.getTime(), idPillReminder, 0);
                    }
                    cal.add(Calendar.DATE, interDayCount);
                }
                break;
        }
    }

    @Transactional
    public void updateAndMarkAsDeletedAfterDate(Date reminderDate, UUID idPillReminder){
        pillReminderEntryRepository.updateAndMarkAsDeletedAfterDate(new Timestamp(new Date().getTime()),
                reminderDate, idPillReminder);
    }

    @Transactional
    public void updateAndMarkAsDeletedByPillReminderId(UUID idPillReminder){
        pillReminderEntryRepository.updateAndMarkAsDeletedByPillReminderId(idPillReminder,
                new Timestamp(new Date().getTime()));
    }
}
