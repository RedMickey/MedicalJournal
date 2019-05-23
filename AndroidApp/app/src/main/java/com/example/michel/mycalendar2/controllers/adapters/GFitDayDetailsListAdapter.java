package com.example.michel.mycalendar2.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.utils.ConvertingUtils;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.HealthFields;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class GFitDayDetailsListAdapter extends ArrayAdapter<DataPoint> {
    private LayoutInflater inflater;
    private int layout;
    private List<DataPoint> dataPointList;
    private String measurementValueTypeStr;
    private int fitMeasurementType;
    private int imageResId;
    private Field dataField;
    private boolean isTimeInterval;

    public GFitDayDetailsListAdapter(Context context, int resource, List<DataPoint> dataPointList){
        super(context, resource, dataPointList);
        this.dataPointList = dataPointList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.measurementValueTypeStr = "";
        this.fitMeasurementType = -1;
    }

    public GFitDayDetailsListAdapter(Context context, int resource, List<DataPoint> dataPointList,
                                     int fitMeasurementType, String measurementValueTypeStr){
        super(context, resource, dataPointList);
        this.dataPointList = dataPointList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
        this.fitMeasurementType = fitMeasurementType;
        this.measurementValueTypeStr = measurementValueTypeStr;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(this.layout, parent, false);
        DataPoint dataPoint = dataPointList.get(position);
        ((ImageView)view.findViewById(R.id.measurement_iv_gf)).setImageResource(imageResId);
        SimpleDateFormat dfFirstDate = new SimpleDateFormat("d MMMM, HH:mm");
        if (isTimeInterval){
            SimpleDateFormat dfSecondDate = new SimpleDateFormat("HH:mm");
            String dateStr1 = dfFirstDate.format(new Date(dataPoint.getStartTime(TimeUnit.MILLISECONDS)));
            String dateStr2 = dfSecondDate.format(new Date(dataPoint.getEndTime(TimeUnit.MILLISECONDS)));
            ((TextView)view.findViewById(R.id.date_tv)).setText(dateStr1 + " - "  + dateStr2);
            if (fitMeasurementType == 110){
                ((TextView)view.findViewById(R.id.value_tv))
                        .setText(String.format("%.2f %s", dataPoint.getValue(dataField).asFloat()/1000f, measurementValueTypeStr));
            }
            else {
                TextView valueTV = (TextView)view.findViewById(R.id.value_tv);
                try {
                    valueTV.setText(String.format("%.0f %s", dataPoint.getValue(dataField).asFloat(), measurementValueTypeStr));
                }
                catch (IllegalStateException ex){
                    valueTV.setText(String.format("%d %s", dataPoint.getValue(dataField).asInt(),
                            createCountTypeEnding(dataPoint.getValue(dataField).asInt())));
                }
            }
        }
        else {
            ((TextView)view.findViewById(R.id.date_tv))
                    .setText(dfFirstDate.format(new Date(dataPoint.getEndTime(TimeUnit.MILLISECONDS))));
            if (fitMeasurementType == 2){
                ((TextView)view.findViewById(R.id.value_tv))
                        .setText(String.format("%.0f/%.0f %s", dataPoint.getValue(HealthFields.FIELD_BLOOD_PRESSURE_SYSTOLIC).asFloat(),
                                dataPoint.getValue(HealthFields.FIELD_BLOOD_PRESSURE_DIASTOLIC).asFloat(), measurementValueTypeStr));
            }
            else {
                ((TextView)view.findViewById(R.id.value_tv))
                        .setText(String.format("%.0f %s", dataPoint.getValue(dataField).asFloat(), measurementValueTypeStr));
            }
        }

        return view;
    }

    private String createCountTypeEnding(double value){
        switch (fitMeasurementType){
            case 8:
                return ConvertingUtils.smartEnding((int)value, new String[]{"", "а", "ов"}, measurementValueTypeStr);
            default:
                return measurementValueTypeStr;
        }
    }

    public String getMeasurementValueTypeStr() {
        return measurementValueTypeStr;
    }

    public void setMeasurementValueTypeStr(String measurementValueTypeStr) {
        this.measurementValueTypeStr = measurementValueTypeStr;
    }

    public int getFitMeasurementType() {
        return fitMeasurementType;
    }

    public void setFitMeasurementType(int fitMeasurementType) {
        this.fitMeasurementType = fitMeasurementType;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public Field getDataField() {
        return dataField;
    }

    public void setDataField(Field dataField) {
        this.dataField = dataField;
    }

    public boolean isTimeInterval() {
        return isTimeInterval;
    }

    public void setIsTimeInterval(boolean timeInterval) {
        isTimeInterval = timeInterval;
    }
}
