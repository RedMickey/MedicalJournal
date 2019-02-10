package com.example.michel.mycalendar2.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class ConvertingUtils {

    public static UUID convertBytesToUUID(byte[] blob){
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
}
