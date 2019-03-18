package com.example.michel.mycalendar2.utils;

import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ConvertingUtils {

    public static UUID convertBytesToUUID(byte[] blob) throws NullPointerException{
        if (blob == null)
            throw new NullPointerException();
        ByteBuffer bbr = ByteBuffer.wrap(blob);
        long high = bbr.getLong();
        long low = bbr.getLong();

        return new UUID(high, low);
    }

    public static byte[] convertUUIDToBytes(UUID uuid){
        return ByteBuffer.allocate(16).putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits()).array();
    }

    /* bytes to hex str
    StringBuilder sb = new StringBuilder();
    for (byte b : ConvertingUtils.convertUUIDToBytes(idWeekSchedule)) {
            sb.append(String.format("%02x", b));
    }*/

    public static String convertDateToString(Date date, int param){
        String pattern = "yyyy-MM-dd HH:mm:ss";
        switch (param){
            case 2:
                pattern = "HH:mm:ss";
                break;
            case 3:
                pattern = "yyyy-MM-dd";
                break;
        }
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static Date convertStringToDate(String dateStr){
        Date date;
        String pattern = "yyyy-MM-dd HH:mm:ss";
        try {
            date = new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    public static Date convertStringToTime(String timeStr){
        Date time;
        String pattern = "HH:mm:ss";
        try {
            time = new SimpleDateFormat(pattern).parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            time = new Date();
        }
        return time;
    }

    public static Date convertStringToOnlyDate(String dateStr){
        Date date;
        String pattern = "yyyy-MM-dd";
        try {
            date = new SimpleDateFormat(pattern).parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }
}
