package com.example.michel.rest_api.models;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;

@Data
public class TestModel {
    @Column(name = "type_value_name")
    private String typeValueName;
    @Column(name = "type_name")
    private String typeName;
}
