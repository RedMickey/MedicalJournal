package com.example.michel.mycalendar2.adapters;

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
import com.example.michel.mycalendar2.utils.utilModels.MeasurementType;

import java.util.List;

public class MeasurementTypesListAdapter extends ArrayAdapter<MeasurementType> {
    private LayoutInflater inflater;
    private int layout;
    private List<MeasurementType> measurementTypes;

    public MeasurementTypesListAdapter(Context context, int resource, List<MeasurementType> measurementTypes){
        super(context, resource, measurementTypes);
        this.measurementTypes = measurementTypes;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(this.layout, parent, false);
        MeasurementType measurementType = measurementTypes.get(position);
        switch (measurementType.getName()){
            case "Температура":
                ((ImageView) view.findViewById(R.id.measurement_type_iv)).setImageResource(R.drawable.ic_thermometer);
                break;
            case "Давление":
                ((ImageView) view.findViewById(R.id.measurement_type_iv)).setImageResource(R.drawable.ic_tonometer2);
                break;
            case "Пульс":
                ((ImageView) view.findViewById(R.id.measurement_type_iv)).setImageResource(R.drawable.ic_pulse);
                break;
            case "Уровень сахара в крови":
                ((ImageView) view.findViewById(R.id.measurement_type_iv)).setImageResource(R.drawable.ic_glucometer);
                break;
            case "Вес":
                ((ImageView) view.findViewById(R.id.measurement_type_iv)).setImageResource(R.drawable.ic_weight);
                break;
            case "Сожженные калории":
                ((ImageView) view.findViewById(R.id.measurement_type_iv)).setImageResource(R.drawable.ic_burning);
                break;
            case "Потребленные калории":
                ((ImageView) view.findViewById(R.id.measurement_type_iv)).setImageResource(R.drawable.ic_food);
                break;
            case "Шаги":
                ((ImageView) view.findViewById(R.id.measurement_type_iv)).setImageResource(R.drawable.ic_footprint);
                break;
        }
        ((TextView) view.findViewById(R.id.measurement_name)).setText(measurementType.getName());

        return view;
    }
}
