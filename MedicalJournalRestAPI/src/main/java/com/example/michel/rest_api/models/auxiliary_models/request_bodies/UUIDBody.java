package com.example.michel.rest_api.models.auxiliary_models.request_bodies;

import lombok.Data;

import java.util.UUID;

@Data
public class UUIDBody {
    private UUID entryUuid;
    private Integer userId;
}
