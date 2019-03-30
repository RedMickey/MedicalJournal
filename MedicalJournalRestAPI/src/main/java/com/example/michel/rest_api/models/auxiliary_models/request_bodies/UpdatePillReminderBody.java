package com.example.michel.rest_api.models.auxiliary_models.request_bodies;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UpdatePillReminderBody {
    protected Integer isDone;
    protected UUID id;
    protected Date date;
}
