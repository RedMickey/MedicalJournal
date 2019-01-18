package com.example.michel.mycalendar2.auxiliary_fragments;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.R;

public class StatisticListItemViewHolder extends RecyclerView.ViewHolder {
    public ImageView measurementIvRmi;
    public TextView measurementNameTvRmi;
    public TextView activeIndTv;
    public ImageView activeIndIv;
    public TextView countOfTakingMeasurements;
    public TextView timePeriodRmi;
    public ImageView havingMealsTypeRmi;
    public TextView countOfTakingMeasurementsDone;
    public TextView currentValueTv;
    public TextView standardValueTv;
    public Button moreInfoButton;

    public StatisticListItemViewHolder(View itemView) {
        super(itemView);
        measurementIvRmi = (ImageView)itemView.findViewById(R.id.measurement_iv_rmi);
        measurementNameTvRmi = (TextView)itemView.findViewById(R.id.measurement_name_tv_rmi);
        activeIndTv = (TextView)itemView.findViewById(R.id.active_ind_tv);
        activeIndIv = (ImageView)itemView.findViewById(R.id.active_ind_iv);
        countOfTakingMeasurements = (TextView)itemView.findViewById(R.id.count_of_taking_measurements);
        timePeriodRmi = (TextView)itemView.findViewById(R.id.time_period_rmi);
        havingMealsTypeRmi = (ImageView)itemView.findViewById(R.id.having_meals_type_rmi);
        countOfTakingMeasurementsDone = (TextView)itemView.findViewById(R.id.count_of_taking_measurements_done);
        currentValueTv = (TextView)itemView.findViewById(R.id.current_value_tv);
        standardValueTv = (TextView)itemView.findViewById(R.id.standard_value_tv);
        moreInfoButton = (Button)itemView.findViewById(R.id.more_info_button);
    }
}
