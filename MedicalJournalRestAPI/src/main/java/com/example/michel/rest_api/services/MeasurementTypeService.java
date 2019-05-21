package com.example.michel.rest_api.services;

import com.example.michel.rest_api.models.MeasurementType;
import com.example.michel.rest_api.models.auxiliary_models.MeasurementTypeF;
import com.example.michel.rest_api.repositories.MeasurementTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MeasurementTypeService {
    @Autowired
    private MeasurementTypeRepository measurementTypeRepository;

    public List<MeasurementTypeF> findAllF(){
        return measurementTypeRepository.findAll().stream().map(
                measurementType -> {return new MeasurementTypeF(measurementType.getIdMeasurementType(), measurementType.getTypeName());}
        ).collect(Collectors.toList());
    }

}
