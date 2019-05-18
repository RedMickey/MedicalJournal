package com.example.michel.rest_api.utils;

public class CalculationUtils {

    public int calculateDayCount(int period ,int periodDMType){
        int dayCount = 0;
        switch (periodDMType){
            case 1: //days
                dayCount = period;
                break;
            case 2: //weeks
                dayCount = period*7;
                break;
            case 3: //months
                dayCount = period*30;
                break;
        }
        return dayCount;
    }
}
