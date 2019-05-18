package com.example.michel.mycalendar2.utils;

import com.example.michel.mycalendar2.models.synchronization.SynchModel;

import java.util.List;

public class SynchronizationUtils {

    public static <T extends SynchModel> boolean containsDeletionMarks(List<T> list){
        for (T item: list) {
            if (item.getChangeType() == 3){
                return true;
            }
        }
        return false;
    }
}
