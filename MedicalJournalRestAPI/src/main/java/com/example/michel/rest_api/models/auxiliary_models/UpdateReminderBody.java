package com.example.michel.rest_api.models.auxiliary_models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UpdateReminderBody {
    protected Integer isDone;
    protected UUID id;
    protected Date date;
}
