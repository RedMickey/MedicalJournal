package com.example.michel.mycalendar2.utils;

import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class TUtils {

    public TUtils(){}

    public CycleDBInsertEntry setDateParams(CycleDBInsertEntry cycleDBInsertEntry, int period, String periodDMType, int type) throws NumberFormatException{
        if (type==0)
        {
            cycleDBInsertEntry.setPeriod(period); //required verification
            cycleDBInsertEntry.setPeriodDMType(
                    DBStaticEntries.dateTypes.get(periodDMType)
            );
            switch (periodDMType){
                case "дн.":
                    cycleDBInsertEntry.setDayCount(period);
                    break;
                case "нед.":
                    cycleDBInsertEntry.setDayCount(period*7);
                    break;
                case "мес.":
                    cycleDBInsertEntry.setDayCount(period*30);
                    break;
            }
        }
        else {
            cycleDBInsertEntry.setOnce_aPeriod(period); //required verification
            cycleDBInsertEntry.setOnce_aPeriodDMType(
                    DBStaticEntries.dateTypes.get(periodDMType)
            );
            switch (periodDMType){
                case "дн.":
                    cycleDBInsertEntry.setDayInterval(period);
                    break;
                case "нед.":
                    cycleDBInsertEntry.setDayInterval(period*7);
                    break;
                case "мес.":
                    cycleDBInsertEntry.setDayInterval(period*30);
                    break;
            }
        }
        return cycleDBInsertEntry;
    }

    public int createPillReminders(CycleDBInsertEntry cycleDBInsertEntry,
                                  PillReminderDBInsertEntry pillReminderDBInsertEntry, int period,
                                  String periodDMType, int type){

        cycleDBInsertEntry = setDateParams(cycleDBInsertEntry, period, periodDMType, type);

        UUID pillReminderId = pillReminderDBInsertEntry.getIdPillReminder();

        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        PillReminderDao pillReminderDao = new PillReminderDao(dbAdapter.open().getDatabase());

        Calendar cal = Calendar.getInstance();
        cal.set(pillReminderDBInsertEntry.getStartDate().getYear(),
                pillReminderDBInsertEntry.getStartDate().getMonth()-1,
                pillReminderDBInsertEntry.getStartDate().getDay());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        switch (cycleDBInsertEntry.getIdCyclingType()){
            case 1:
                for(int i=0; i<cycleDBInsertEntry.getDayCount();i++){
                    for (int j=0; j<pillReminderDBInsertEntry.getReminderTimes().length; j++){
                        pillReminderDao.insertPillReminderEntry(
                                sdf.format(new Date(cal.getTimeInMillis())),
                                pillReminderId,
                                pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr(), 0
                        );
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 2:
                for(int i=0; i<cycleDBInsertEntry.getDayCount();i++){
                    if(cycleDBInsertEntry.getWeekSchedule()[cal.get(Calendar.DAY_OF_WEEK)-1]==1){
                        for (int j=0; j<pillReminderDBInsertEntry.getReminderTimes().length; j++){
                            pillReminderDao.insertPillReminderEntry(
                                    sdf.format(cal.getTime()),
                                    pillReminderId,
                                    pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr(), 0
                            );
                        }
                    }
                    cal.add(Calendar.DATE, 1);
                }
                break;
            case 3:
                for(int i=0; i<cycleDBInsertEntry.getDayCount();i+=cycleDBInsertEntry.getDayInterval()){
                    for (int j=0; j<pillReminderDBInsertEntry.getReminderTimes().length; j++){
                        pillReminderDao.insertPillReminderEntry(
                                sdf.format(new Date(cal.getTimeInMillis())),
                                pillReminderId,
                                pillReminderDBInsertEntry.getReminderTimes()[j].getReminderTimeStr(), 0
                        );
                    }
                    cal.add(Calendar.DATE, cycleDBInsertEntry.getDayInterval());
                }
                break;
        }
        dbAdapter.close();
        return cycleDBInsertEntry.getDayCount()*pillReminderDBInsertEntry.getReminderTimes().length;
    }

}
