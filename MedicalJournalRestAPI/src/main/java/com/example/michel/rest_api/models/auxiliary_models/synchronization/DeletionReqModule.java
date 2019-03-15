package com.example.michel.rest_api.models.auxiliary_models.synchronization;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class DeletionReqModule {
    private List<UUID> reminderIds;
    private List<UUID> reminderEntriesIds;
    private List<UUID> cycleIds;
    private List<UUID> reminderTimeIds;
    private List<UUID> weekScheduleIds;
    private int type;
}
