package com.example.michel.mycalendar2.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.michel.mycalendar2.activities.MeasurementChartActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.auxiliary_fragments.StatisticListItemViewHolder;
import com.example.michel.mycalendar2.models.measurement.MeasurementStatEntry;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.util.List;

public class StatisticListAdapter extends RecyclerView.Adapter<StatisticListItemViewHolder> {
    private List<MeasurementStatEntry> measurementStatEntries;
    private Context context;

    public StatisticListAdapter(Context context, List<MeasurementStatEntry> measurementStatEntries){
        this.measurementStatEntries = measurementStatEntries;
        this.context = context;
    }

    @NonNull
    @Override
    public StatisticListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.measurement_statistic_item, parent, false);
        return new StatisticListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticListItemViewHolder holder, int position) {
        final MeasurementStatEntry mse = measurementStatEntries.get(position);

        if (mse.getIsActive()==1){
            holder.activeIndTv.setText("Активен");
            holder.activeIndIv.setImageResource(R.drawable.ic_red_circle);
        }
        else {
            holder.activeIndTv.setText("Завершён");
            holder.activeIndIv.setImageResource(R.drawable.ic_grey_circle);
        }

        String ending = " раз в день";
        if (mse.getNumberOfDoingAction()<11||mse.getNumberOfDoingAction()>20){
            if (checkLastDigitOn234(mse.getNumberOfDoingAction()))
                ending=" раза в день";
        }

        String curValueStr = "";
        String standardValueStr = "";
        switch (mse.getIdMeasurementType()){
            case 1:
                holder.measurementNameTvRmi.setText(DBStaticEntries.getMeasurementTypeById(1).getName());
                holder.measurementIvRmi.setImageResource(R.drawable.ic_thermometer);
                String[] curValueAndStandardValueStrs = createCurValueAndStandardValueStrs(mse);
                curValueStr = curValueAndStandardValueStrs[0];
                standardValueStr = curValueAndStandardValueStrs[1];

                break;
            case 2:
                holder.measurementNameTvRmi.setText(DBStaticEntries.getMeasurementTypeById(2).getName());
                holder.measurementIvRmi.setImageResource(R.drawable.ic_tonometer);

                if (mse.getAverageCurValues()[0]!=-10000){
                    curValueStr+="Нижнее: " + String.format("%.1f", mse.getAverageCurValues()[0]) + " " + mse.getMeasurementValueTypeStr();
                }
                if (mse.getAverageCurValues()[1]!=-10000){
                    curValueStr+="\n Верхнее: " + String.format("%.1f", mse.getAverageCurValues()[1]) + " " + mse.getMeasurementValueTypeStr();
                }

                if (mse.getStandardValues()[0]!=-10000){
                    standardValueStr+="Нижнее: " + String.format("%.1f", mse.getStandardValues()[0]) + " " + mse.getMeasurementValueTypeStr();
                }
                if (mse.getStandardValues()[1]!=-10000){
                    standardValueStr+="\n Верхнее: " + String.format("%.1f", mse.getStandardValues()[1]) + " " + mse.getMeasurementValueTypeStr();
                }
                break;
        }

        holder.countOfTakingMeasurements.setText(String.valueOf(mse.getNumberOfDoingAction())+ending);
        holder.timePeriodRmi.setText("с "+mse.getStartDate()+" по "+mse.getEndDate());
        switch (mse.getHavingMealsType()){
            case 1:
                holder.havingMealsTypeRmi.setImageResource(R.drawable.icons8_50_21);
                break;
            case 2:
                holder.havingMealsTypeRmi.setImageResource(R.drawable.icons8_50_61);
                break;
            case 3:
                holder.havingMealsTypeRmi.setImageResource(R.drawable.icons8_50_4);
                break;
        }
        holder.countOfTakingMeasurementsDone.setText(String.valueOf(mse.getNumberOfDoingActionLeft()));

        holder.standardValueTv.setText(standardValueStr);
        holder.currentValueTv.setText(curValueStr);

        holder.moreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MeasurementChartActivity.class);
                /*intent.putExtra("id", mse.getId());
                intent.putExtra("startDateStr", mse.getStartDate());
                intent.putExtra("endDateStr", mse.getEndDate());
                intent.putExtra("averageCurValues", mse.getAverageCurValues());
                intent.putExtra("standardValues", mse.getStandardValues());*/
                intent.putExtra("mse", mse);
                context.startActivity(intent);
            }
        });

        if (position+1 == measurementStatEntries.size()){
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.bottomMargin=0;
            holder.itemView.setLayoutParams(layoutParams);
        }
    }

    @Override
    public int getItemCount() {
        return measurementStatEntries.size();
    }

    private boolean checkLastDigitOn234(int x){
        int buf = x%10;
        return (buf>1&&buf<5)?true:false;
    }

    private String[] createCurValueAndStandardValueStrs(MeasurementStatEntry mse){
        String[] curValueAndStandardValueStrs = new String[]{"", ""};

        if (mse.getAverageCurValues()[0]!=-10000){
            curValueAndStandardValueStrs[0]+=String.format("%.1f", mse.getAverageCurValues()[0]) + " " + mse.getMeasurementValueTypeStr();
        }
        if (mse.getAverageCurValues()[1]!=-10000){
            curValueAndStandardValueStrs[0]+=" - " + String.format("%.1f", mse.getAverageCurValues()[1]) + " " + mse.getMeasurementValueTypeStr();
        }

        if (mse.getStandardValues()[0]!=-10000){
            curValueAndStandardValueStrs[1]+=String.format("%.1f", mse.getStandardValues()[0]) + " " + mse.getMeasurementValueTypeStr();
        }
        if (mse.getStandardValues()[1]!=-10000){
            curValueAndStandardValueStrs[1]+=" - " + String.format("%.1f", mse.getStandardValues()[1]) + " " + mse.getMeasurementValueTypeStr();
        }
        return curValueAndStandardValueStrs;
    }
}
