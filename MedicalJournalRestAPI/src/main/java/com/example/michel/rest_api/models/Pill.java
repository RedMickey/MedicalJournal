package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Data
public class Pill {
    @Id
    @Column(name = "_id_pill")
    private UUID id;
    @Column(name = "pill_name")
    private String pillName;
    @Column(name = "pill_description")
    private String pillDescription;
    @Column(name = "synch_time")
    private Timestamp synchTime;
    @Column(name = "change_type")
    private int changeType;
}
