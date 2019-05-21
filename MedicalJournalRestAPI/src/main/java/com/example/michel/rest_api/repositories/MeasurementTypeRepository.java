package com.example.michel.rest_api.repositories;

import com.example.michel.rest_api.models.MeasurementType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MeasurementTypeRepository extends CrudRepository<MeasurementType, Integer> {

    @Query(value = "select * from measurement_type mt inner join measurement_value_type mvt on" +
            " mt._id_measur_value_type = mvt._id_measur_value_type where mt._id_measurement_type = ?1", nativeQuery = true)
    public MeasurementType findMeasurementTypeById(Integer id);

    /*@Query(value = "select mvt.type_value_name, mt.type_name from measurement_type mt inner join measurement_value_type mvt on" +
            " mt._id_measur_value_type = mvt._id_measur_value_type where mt._id_measurement_type = ?1", nativeQuery = true)
    public TestModel findmeasurementvaluetypeName(Integer id);*/

    @Query(value = "select * from measurement_type mt where mt._id_measurement_type = ?1", nativeQuery = true)
    public MeasurementType findMeasurementTypeByIdMeasurementType(Integer id);

    public List<MeasurementType> findAll();
}
