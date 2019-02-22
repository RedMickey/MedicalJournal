package com.example.michel.mycalendar2.models.synchronization;

import java.util.UUID;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class PillDB extends SynchModel {
    private UUID idPill;
    private String pillName;
    private String pillDescription;
}
