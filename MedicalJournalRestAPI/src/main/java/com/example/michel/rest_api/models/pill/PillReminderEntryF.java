package com.example.michel.rest_api.models.pill;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;
import java.util.UUID;

@Data
public class PillReminderEntryF {
    private String pillName;
    private int pillCount;
    private String pillCountType;
    protected int isDone;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Europe/Moscow")
    protected Date date;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", timezone = "Europe/Moscow")
    protected Date havingMealsTime;
    protected UUID id;
    protected int havingMealsType;
}
