package com.example.michel.rest_api.models.auxiliary_models.websocket_responce_bodies;

import com.example.michel.rest_api.models.auxiliary_models.request_bodies.UpdateMeasurementReminderBody;
import com.example.michel.rest_api.models.auxiliary_models.request_bodies.UpdatePillReminderBody;
import lombok.Data;

@Data
public class PillAndMeasurementEntriesUpdateBody {

    private final UpdatePillReminderBody updatePillReminderBody;
    private final UpdateMeasurementReminderBody updateMeasurementReminderBody;
}
