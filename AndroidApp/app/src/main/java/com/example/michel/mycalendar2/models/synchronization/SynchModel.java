package com.example.michel.mycalendar2.models.synchronization;

import java.util.Date;

import lombok.Data;

@Data
public class SynchModel {
    private Date synchTime;
    private int changeType;
}
