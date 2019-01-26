package com.example.michel.mycalendar2.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimestampTypeAdapter extends TypeAdapter<Timestamp> {
    @Override
    public void write(JsonWriter out, Timestamp value) throws IOException {
        if (value == null)
            out.nullValue();
        else
            out.value(value.getTime());
    }

    @Override
    public Timestamp read(JsonReader in) throws IOException {
        if (in != null){
            String timestampStr = in.nextString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            try {
                Date parsedDate = dateFormat.parse(timestampStr);
                return new Timestamp(parsedDate.getTime());
            }catch (Exception e){
                return new Timestamp(0);
            }
        }
        else
            return null;
    }
}
