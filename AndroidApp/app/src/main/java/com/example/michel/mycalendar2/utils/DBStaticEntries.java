package com.example.michel.mycalendar2.utils;

import com.example.michel.mycalendar2.utils.utilModels.MeasurementType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DBStaticEntries {

    public static Map<String, Integer> doseTypes;

    public static Map<String, Integer> dateTypes;

    public static Map<String, Integer> cycleTypes;

    public static List<MeasurementType> measurementTypes;

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static String getKeyByValue2(Map<String, Integer> map, int value) {
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (entry.getValue()==value) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static MeasurementType getMeasurementTypeById(int id){
        for(MeasurementType mt : measurementTypes){
            if (mt.getIndex()==id)
                return mt;
        }
        return null;
    }
}
