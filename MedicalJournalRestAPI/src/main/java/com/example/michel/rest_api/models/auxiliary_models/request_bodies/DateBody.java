package com.example.michel.rest_api.models.auxiliary_models.request_bodies;

import lombok.Data;

import java.util.Date;

@Data
public class DateBody {
    private Date date;
    private Integer userId;
}
