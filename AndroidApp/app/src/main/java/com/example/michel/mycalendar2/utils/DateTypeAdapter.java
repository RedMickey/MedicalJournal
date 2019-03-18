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
                return parsedDate;
            }catch (Exception e){
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                try {
                    Date parsedTime = timeFormat.parse(dateStr);
                    return parsedTime;
                }
                catch (Exception e2){
                    SimpleDateFormat onlyDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date parsedOnlyDate = onlyDateFormat.parse(dateStr);
                        return parsedOnlyDate;
                    }
                    catch (Exception e3){
                        return null;
                    }
                }
            }
        }
        else
            return null;
    }
}
