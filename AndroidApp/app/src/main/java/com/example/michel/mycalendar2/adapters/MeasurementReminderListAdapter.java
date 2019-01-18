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
import com.example.michel.mycalendar2.models.measurement.MeasurementReminder;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.util.List;

public class MeasurementReminderListAdapter extends ArrayAdapter<MeasurementReminder> {
    private LayoutInflater inflater;
    private int layout;
    private List<MeasurementReminder> measurementReminders;

    public MeasurementReminderListAdapter(Context context, int resource, List<MeasurementReminder> measurementReminders){
        super(context, resource, measurementReminders);
        this.measurementReminders = measurementReminders;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(this.layout, parent, false);
        MeasurementReminder mr = measurementReminders.get(position);
        //((TextView)view.findViewById(R.id.pill_name_tv_rmi)).setText(mr.getPillName());

        if (mr.getIsActive()==1){
            ((TextView)view.findViewById(R.id.active_ind_tv)).setText("Активен");
            ((ImageView)view.findViewById(R.id.active_ind_iv)).setImageResource(R.drawable.ic_red_circle);
        }
        else {
            ((TextView)view.findViewById(R.id.active_ind_tv)).setText("Завершён");
            ((ImageView)view.findViewById(R.id.active_ind_iv)).setImageResource(R.drawable.ic_grey_circle);
        }

        String ending = " раз в день";
        if (mr.getNumberOfDoingAction()<11||mr.getNumberOfDoingAction()>20){
            if (checkLastDigitOn234(mr.getNumberOfDoingAction()))
                ending=" раза в день";
        }

        TextView measurementNameTV = (TextView)view.findViewById(R.id.measurement_name_tv_rmi);
        ImageView measurementTypeIV = (ImageView)view.findViewById(R.id.measurement_iv_rmi);
        switch (mr.getIdMeasurementType()){
            case 1:
                measurementNameTV.setText(DBStaticEntries.getMeasurementTypeById(1).getName());
                measurementTypeIV.setImageResource(R.drawable.ic_thermometer);
                break;
            case 2:
                measurementNameTV.setText(DBStaticEntries.getMeasurementTypeById(2).getName());
                measurementTypeIV.setImageResource(R.drawable.ic_tonometer);
                break;
        }

        ((TextView)view.findViewById(R.id.count_of_taking_measurements))
                .setText(String.valueOf(mr.getNumberOfDoingAction())+ending);
        ((TextView)view.findViewById(R.id.time_period_rmi))
                .setText("с "+mr.getStartDate()+" по "+mr.getEndDate());
        ImageView havingMealsType = (ImageView)view.findViewById(R.id.having_meals_type_rmi);
        switch (mr.getHavingMealsType()){
            case 1:
                ((ImageView) view.findViewById(R.id.having_meals_type_rmi)).setImageResource(R.drawable.icons8_50_21);
                break;
            case 2:
                ((ImageView) view.findViewById(R.id.having_meals_type_rmi)).setImageResource(R.drawable.icons8_50_61);
                break;
            case 3:
                ((ImageView) view.findViewById(R.id.having_meals_type_rmi)).setImageResource(R.drawable.icons8_50_4);
                break;
        }
        ((TextView)view.findViewById(R.id.count_of_taking_measurements_left))
                .setText(String.valueOf(mr.getNumberOfDoingActionLeft()));

        return view;
    }

    private boolean checkLastDigitOn234(int x){
        int buf = x%10;
        return (buf>1&&buf<5)?true:false;
    }
}
