package com.example.michel.rest_api.models.auxiliary_models.synchronization;

import lombok.Data;

import java.util.Date;

@Data
public class SynchronizationReq {
    private int userId;
    private Date synchronizationTimestamp;
}
