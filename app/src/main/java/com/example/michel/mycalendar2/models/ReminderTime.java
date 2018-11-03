package com.example.michel.mycalendar2.models;

public class ReminderTime {
    private String reminderTimeStr;
    private int idReminderTime;

    public ReminderTime(int idReminderTime, String reminderTimeStr){
        this.idReminderTime = idReminderTime;
        this.reminderTimeStr = reminderTimeStr;
    }

    public ReminderTime(String reminderTimeStr){
        this.idReminderTime = 0;
        this.reminderTimeStr = reminderTimeStr;
    }

    public String getReminderTimeStr() {
        return reminderTimeStr;
    }

    public void setReminderTimeStr(String reminderTimeStr) {
        this.reminderTimeStr = reminderTimeStr;
    }

    public int getIdReminderTime() {
        return idReminderTime;
    }

    public void setIdReminderTime(int idReminderTime) {
        this.idReminderTime = idReminderTime;
    }
}
