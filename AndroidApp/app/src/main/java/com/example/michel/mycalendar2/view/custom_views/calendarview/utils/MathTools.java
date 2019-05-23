package com.example.michel.mycalendar2.view.custom_views.calendarview.utils;

public class MathTools {
    public static int floorMod(int value, int mod) {
        int ret = value%mod;
        if (ret < 0) ret += mod;
        return ret;
    }
}