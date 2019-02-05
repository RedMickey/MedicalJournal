package com.example.michel.rest_api.models.pill;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Data
public class PillReminderEntryJ {

    @Column(name = "pill_name")
    private String pillName;
    @Column(name = "pill_count")
    private int pillCount;
    @Column(name = "type_name")
    private String pillCountType;
    @Column(name = "is_done")
    protected int isDone;
    @Column(name = "reminder_date")
    protected java.sql.Date date;
    @Column(name = "having_meals_time")
    protected java.sql.Date havingMealsTime;
    protected boolean isLate;
    @Column(name = "_id_pill_reminder_entry")
    protected String id;
    @Column(name = "_id_having_meals_type")
    protected int havingMealsType;
}
