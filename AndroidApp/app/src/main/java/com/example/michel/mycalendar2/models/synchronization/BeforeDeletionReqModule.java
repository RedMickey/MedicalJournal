package com.example.michel.mycalendar2.models.synchronization;

import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeforeDeletionReqModule {
    private List<UUID> reminderIds;
    private List<UUID> reminderEntriesIds;
    private List<UUID> cycleIds;
    private List<UUID> reminderTimeIds;
    private List<UUID> weekScheduleIds;
    private int type;
    private int userId;
}
