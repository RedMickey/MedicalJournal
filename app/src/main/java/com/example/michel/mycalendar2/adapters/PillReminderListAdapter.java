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
import com.example.michel.mycalendar2.models.pill.PillReminder;

import java.util.List;

public class PillReminderListAdapter extends ArrayAdapter<PillReminder>{
    private LayoutInflater inflater;
    private int layout;
    private List<PillReminder> pillReminder;

    public PillReminderListAdapter(Context context, int resource, List<PillReminder> pillReminder) {
        super(context, resource, pillReminder);
        this.pillReminder = pillReminder;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view=inflater.inflate(this.layout, parent, false);
        PillReminder pr = pillReminder.get(position);
        ((TextView)view.findViewById(R.id.pill_name_tv_rmi)).setText(pr.getPillName());
        if (pr.getIsActive()==1){
            ((TextView)view.findViewById(R.id.active_ind_tv)).setText("Активен");
            ((ImageView)view.findViewById(R.id.active_ind_iv)).setImageResource(R.drawable.ic_red_circle);
        }
        else {
            ((TextView)view.findViewById(R.id.active_ind_tv)).setText("Завершён");
            ((ImageView)view.findViewById(R.id.active_ind_iv)).setImageResource(R.drawable.ic_grey_circle);
        }
        ((TextView)view.findViewById(R.id.pill_count_tv_rmi))
                .setText(String.valueOf(pr.getPillCount())+" "+pr.getPillCountType());
        String ending = " раз в день";
        if (pr.getCountOfTakingMedicine()<11||pr.getCountOfTakingMedicine()>20){
            if (checkLastDigitOn234(pr.getCountOfTakingMedicine()))
                ending=" раза в день";
        }
        ((TextView)view.findViewById(R.id.count_of_taking_medicine))
                .setText(String.valueOf(pr.getCountOfTakingMedicine())+ending);
        ((TextView)view.findViewById(R.id.time_period_rmi))
                .setText("с "+pr.getStartDate()+" по "+pr.getEndDate());
        ImageView havingMealsType = (ImageView)view.findViewById(R.id.having_meals_type_rmi);
        switch (pr.getHavingMealsType()){
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
        ((TextView)view.findViewById(R.id.count_of_taking_medicine_left))
                .setText(String.valueOf(pr.getCountOfTakingMedicineLeft()));

        return view;
    }

    private boolean checkLastDigitOn234(int x){
        int buf = x%10;
        return (buf>1&&buf<5)?true:false;
    }
}
