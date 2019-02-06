package com.example.michel.rest_api.models.pill;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Data
public class PillReminderEntryF {
    private String pillName;
    private int pillCount;
    private String pillCountType;
    protected int isDone;
    protected java.sql.Date date;
    protected java.sql.Date havingMealsTime;
    protected boolean isLate;
    protected String id;
    protected int havingMealsType;
}
