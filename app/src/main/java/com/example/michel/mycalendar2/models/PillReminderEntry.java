package com.example.michel.mycalendar2.models;

public class PillReminderEntry {
    private int id;
    private String name;
    private String time;

    public PillReminderEntry(int id, String name, String time)
    {
        this.id = id;
        this.name = name;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }
}