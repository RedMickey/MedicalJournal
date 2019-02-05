package com.example.michel.rest_api.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "measurement_type", schema = "medical_journal", catalog = "")
@SecondaryTable(name="measurement_value_type", pkJoinColumns={
        @PrimaryKeyJoinColumn(name="_id_measur_value_type", referencedColumnName="_id_measurement_type")}
)
public class MeasurementType {
    private int idMeasurementType;
    private String typeName;
    private double standardMinValue;
    private double standardMaxValue;
    private int idMeasurValueType;

    private String typeValueName;
    //private MeasurementValueType measurementValueType;

    @Id
    @Column(name = "_id_measurement_type")
    public int getIdMeasurementType() {
        return idMeasurementType;
    }

    public void setIdMeasurementType(int idMeasurementType) {
        this.idMeasurementType = idMeasurementType;
    }

    @Basic
    @Column(name = "type_name")
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Basic
    @Column(name = "standard_min_value")
    public double getStandardMinValue() {
        return standardMinValue;
    }

    public void setStandardMinValue(double standardMinValue) {
        this.standardMinValue = standardMinValue;
    }

    /*@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="_id_measur_value_type")
    public MeasurementValueType getMeasurementValueType() {
        return this.measurementValueType;
    }


    public void setMeasurementValueType(MeasurementValueType measurementValueType) {
        this.measurementValueType = measurementValueType;
    }*/

    @Basic
    @Column(name = "standard_max_value")
    public double getStandardMaxValue() {
        return standardMaxValue;
    }

    public void setStandardMaxValue(double standardMaxValue) {
        this.standardMaxValue = standardMaxValue;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeasurementType that = (MeasurementType) o;
        return idMeasurementType == that.idMeasurementType &&
                Double.compare(that.standardMinValue, standardMinValue) == 0 &&
                Double.compare(that.standardMaxValue, standardMaxValue) == 0 &&
                Objects.equals(typeName, that.typeName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(idMeasurementType, typeName, standardMinValue, standardMaxValue);
    }

    @Column(name = "_id_measur_value_type")
    public int getIdMeasurValueType() {
        return idMeasurValueType;
    }

    public void setIdMeasurValueType(int idMeasurValueType) {
        this.idMeasurValueType = idMeasurValueType;
    }

    @Column(table="measurement_value_type")
    public String getTypeValueName() {
        return typeValueName;
    }

    public void setTypeValueName(String typeValueName) {
        this.typeValueName = typeValueName;
    }
}
