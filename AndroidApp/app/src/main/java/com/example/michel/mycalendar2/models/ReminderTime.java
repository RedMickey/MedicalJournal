package com.example.michel.mycalendar2.models;

import java.util.UUID;

public class ReminderTime {
    private String reminderTimeStr;
    private UUID idReminderTime;

    public ReminderTime(UUID idReminderTime, String reminderTimeStr){
        this.idReminderTime = idReminderTime;
        this.reminderTimeStr = reminderTimeStr;
    }

    public ReminderTime(String reminderTimeStr){
        this.idReminderTime = null;
        this.reminderTimeStr = reminderTimeStr;
    }

    public String getReminderTimeStr() {
        return reminderTimeStr;
    }

    public void setReminderTimeStr(String reminderTimeStr) {
        this.reminderTimeStr = reminderTimeStr;
    }

    public UUID getIdReminderTime() {
        return idReminderTime;
    }

    public void setIdReminderTime(UUID idReminderTime) {
        this.idReminderTime = idReminderTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj==this) return true;
        if (obj==null || obj.getClass()!=this.getClass()) return false;
        return (this.idReminderTime==((ReminderTime) obj).getIdReminderTime()&&
                this.reminderTimeStr.compareTo(((ReminderTime) obj).getReminderTimeStr()) ==0);
    }
}
