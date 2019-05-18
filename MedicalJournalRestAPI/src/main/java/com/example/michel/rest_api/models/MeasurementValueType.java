package com.example.michel.rest_api.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "measurement_value_type", schema = "medical_journal", catalog = "")
public class MeasurementValueType {
    private int idMeasurValueType;
    private String typeValueName;

    @Id
    @Column(name = "_id_measur_value_type")
    public int getIdMeasurValueType() {
        return idMeasurValueType;
    }

    public void setIdMeasurValueType(int idMeasurValueType) {
        this.idMeasurValueType = idMeasurValueType;
    }

    @Basic
    @Column(name = "type_value_name")
    public String getTypeValueName() {
        return typeValueName;
    }

    public void setTypeValueName(String typeValueName) {
        this.typeValueName = typeValueName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementValueType that = (MeasurementValueType) o;
        return idMeasurValueType == that.idMeasurValueType &&
                Objects.equals(typeValueName, that.typeValueName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idMeasurValueType, typeValueName);
    }
}
