package com.example.michel.rest_api.utils;

import java.nio.ByteBuffer;
import java.util.UUID;

public class ConvertingUtils {
    public int[] boolArrToIntArr(boolean[] boolArr){
        int[] intArr = new int[boolArr.length];
        for(int i=0; i<boolArr.length; i++){
            intArr[i] = boolArr[i]==true?1:0;
        }
        return intArr;
    }

    public UUID convertBytesToUUID(byte[] blob) throws NullPointerException{
        if (blob == null)
            throw new NullPointerException();
        ByteBuffer bbr = ByteBuffer.wrap(blob);
        long high = bbr.getLong();
        long low = bbr.getLong();

        return new UUID(high, low);
    }

    public byte[] convertUUIDToBytes(UUID uuid){
        return ByteBuffer.allocate(16).putLong(uuid.getMostSignificantBits())
                .putLong(uuid.getLeastSignificantBits()).array();
    }
}
