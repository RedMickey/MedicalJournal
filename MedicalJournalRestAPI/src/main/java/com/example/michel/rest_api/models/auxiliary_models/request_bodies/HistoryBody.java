package com.example.michel.rest_api.models.auxiliary_models.request_bodies;

import lombok.Data;

import java.util.Date;

@Data
public class HistoryBody {
    private Integer userId;
    private Date startDate;
    private Date endDate;
}
