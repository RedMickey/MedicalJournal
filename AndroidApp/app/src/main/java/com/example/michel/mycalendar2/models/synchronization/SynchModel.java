package com.example.michel.mycalendar2.models.synchronization;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SynchModel {
    private Date synchTime;
    private int changeType;

    public SynchModel(int changeType){
        this.synchTime = null;
        this.changeType = changeType;
    }
}
