package com.example.michel.mycalendar2.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;

import java.text.SimpleDateFormat;
import java.util.List;

public class ReminderTimeListAdapter extends ArrayAdapter<MeasurementReminderEntry> {
    private LayoutInflater inflater;
    private int layout;
    private List<MeasurementReminderEntry> measurementReminderEntries;
    private SimpleDateFormat dateFormat;

    public ReminderTimeListAdapter(Context context, int resource, List<MeasurementReminderEntry> measurementReminderEntries){
        super(context, resource, measurementReminderEntries);
        this.measurementReminderEntries = measurementReminderEntries;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        dateFormat = new SimpleDateFormat("HH:mm");
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(this.layout, parent, false);
        MeasurementReminderEntry mre = measurementReminderEntries.get(position);

        ((TextView) view.findViewById(R.id.reminder_time)).setText(dateFormat.format(mre.getDate()));

        return view;
    }
}
