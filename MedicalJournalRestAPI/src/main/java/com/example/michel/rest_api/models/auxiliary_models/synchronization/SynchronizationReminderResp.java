package com.example.michel.rest_api.models.auxiliary_models.synchronization;

import lombok.Data;

import java.util.Date;

@Data
public class SynchronizationReminderResp {
    private final Date synchronizationTimestamp;
    private final boolean hasDeletion;
}
