package com.example.michel.mycalendar2.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeAdapter extends TypeAdapter<Date> {
    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        if (value == null)
            out.nullValue();
        else
            out.value(value.getTime());
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        if (in != null){
            String dateStr = in.nextString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            try {
                Date parsedDate = dateFormat.parse(dateStr);
                return new Date(parsedDate.getTime());
            }catch (Exception e){
                return new Date(0);
            }
        }
        else
            return null;
    }
}
