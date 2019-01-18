package com.example.michel.mycalendar2.app_async_tasks;

import android.os.AsyncTask;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.AddMeasurementActivity;
import com.example.michel.mycalendar2.activities.AddTreatmentActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.adapters.TimesOfTakingMedicineAdapter;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.models.CycleAndMeasurementComby;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderDBEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

public class AddMeasurementActivityCreationTask extends AsyncTask<Integer, Void, CycleAndMeasurementComby> {
    private AddMeasurementActivity view;
    private DateData pickDateButtonDateData;
    private TimesOfTakingMedicineAdapter timesOfTakingMedicineAdapter;

    public AddMeasurementActivityCreationTask(AddMeasurementActivity view, DateData pickDateButtonDateData, TimesOfTakingMedicineAdapter timesOfTakingMedicineAdapter){
        super();
        this.view = view;
        this.pickDateButtonDateData = pickDateButtonDateData;
        this.timesOfTakingMedicineAdapter = timesOfTakingMedicineAdapter;
    }

    @Override
    protected CycleAndMeasurementComby doInBackground(Integer... integers) {

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        CycleAndMeasurementComby cycleAndMeasurementComby = databaseAdapter.getCycleAndMeasurementCombyById(integers[0]);

        databaseAdapter.close();

        return cycleAndMeasurementComby;
    }

    @Override
    protected void onPostExecute(CycleAndMeasurementComby cycleAndMeasurementComby) {
        MeasurementReminderDBEntry mrdbe = cycleAndMeasurementComby.measurementReminderDBEntry;
        CycleDBInsertEntry cdbie = cycleAndMeasurementComby.cycleDBInsertEntry;

        ((TextView)view.findViewById(R.id.active_ind_tv_CaddT)).setText((mrdbe.getIsActive()==1?"Активное":"Завершённое"));
        ((Switch)view.findViewById(R.id.switch_active_type)).setChecked((mrdbe.getIsActive()==1?true:false));

        view.setOldMeasurementReminder(mrdbe);
        view.setIdWeekSchedule(cdbie.getIdWeekSchedule());

        ((Button)view.findViewById(R.id.pick_start_date_button)).setText(setDate(mrdbe.getStartDate().getDay(), mrdbe.getStartDate().getMonth(), mrdbe.getStartDate().getYear()));
        RadioGroup radioGroupCycleType = (RadioGroup) view.findViewById(R.id.cycle_type);
        String mapCycleTypesValue = DBStaticEntries.getKeyByValue2(DBStaticEntries.cycleTypes, cdbie.getIdCyclingType());
        switch(mapCycleTypesValue){
            case "every_day":
                ((RadioButton)radioGroupCycleType.getChildAt(0)).setChecked(true);
                LinearLayout everyDayPeriodLayout = (LinearLayout) view.findViewById(R.id.every_day_period);

                setCycleParams(cdbie,everyDayPeriodLayout, 0);
                break;
            case "specific_days":
                ((RadioButton)radioGroupCycleType.getChildAt(1)).setChecked(true);
                GridLayout gridLayout = (GridLayout) view.findViewById(R.id.specific_days_layout_id);
                for (int i=7; i< 14; i++){
                    ((CheckBox) gridLayout.getChildAt(i)).setChecked(cdbie.getWeekSchedule()[i-7]==1?true:false);
                }
                break;
            case "day_interval":
                ((RadioButton)radioGroupCycleType.getChildAt(2)).setChecked(true);
                LinearLayout once_aDateLayout = (LinearLayout) view.findViewById(R.id.once_a_date_layout);
                setCycleParams(cdbie, once_aDateLayout, 1);
                setCycleParams(cdbie, (LinearLayout) view.findViewById(R.id.day_interval_period), 0);
                break;
        }

        RadioGroup radioGroupRegardingMeals = (RadioGroup) view.findViewById(R.id.regarding_meals);
        switch (mrdbe.getIdHavingMealsType()){
            case 1:
                radioGroupRegardingMeals.check(R.id.before_meals);
                ((EditText)view.findViewById(R.id.count_of_minutes)).setText(String.valueOf(mrdbe.getHavingMealsTime()));
                break;
            case 2:
                radioGroupRegardingMeals.check(R.id.with_meals);
                break;
            case 3:
                radioGroupRegardingMeals.check(R.id.after_meals);
                ((EditText)view.findViewById(R.id.count_of_minutes)).setText(String.valueOf(mrdbe.getHavingMealsTime()));
                break;
        }

        LinearLayout timesOfTakingMeasurement = (LinearLayout) view.findViewById(R.id.times_of_taking_measurement);
        for(int i=0; i<mrdbe.getReminderTimes().length;i++){
            timesOfTakingMedicineAdapter.add(mrdbe.getReminderTimes()[i].getReminderTimeStr());
            timesOfTakingMedicineAdapter.notifyDataSetChanged();
            View item = timesOfTakingMedicineAdapter.getView(timesOfTakingMedicineAdapter.getCount()-1, null, null);
            timesOfTakingMeasurement.addView(item);
        }
        ((TextView)view.findViewById(R.id.annotation)).setText(mrdbe.getAnnotation());

    }

    private String setDate(int day, int month, int year){
        pickDateButtonDateData.setDay(day);
        pickDateButtonDateData.setMonth(month);
        pickDateButtonDateData.setYear(year);

        return String.valueOf(day)+" "+ CalendarUtil.getRusMonthName(month, 0)
                +" "+String.valueOf(year)+" г.";
    }

    private void setCycleParams(CycleDBInsertEntry cycleDBInsertEntry, LinearLayout linearLayout, int type){
        if(type==0){
            String periodDMType = DBStaticEntries.getKeyByValue2(DBStaticEntries.dateTypes, cycleDBInsertEntry.getPeriodDMType());
            ((EditText)linearLayout.getChildAt(1)).setText(String.valueOf(cycleDBInsertEntry.getPeriod()));

            Spinner spinnerType = (Spinner)linearLayout.getChildAt(2);
            int spinnerPosition = ((ArrayAdapter) spinnerType.getAdapter()).getPosition(periodDMType);
            spinnerType.setSelection(spinnerPosition);
        }
        else {
            String once_aPeriodDMType = DBStaticEntries.getKeyByValue2(DBStaticEntries.dateTypes, cycleDBInsertEntry.getOnce_aPeriodDMType());
            ((EditText)linearLayout.getChildAt(1)).setText(String.valueOf(cycleDBInsertEntry.getOnce_aPeriod()));

            Spinner spinnerType = (Spinner)linearLayout.getChildAt(2);
            int spinnerPosition = ((ArrayAdapter) spinnerType.getAdapter()).getPosition(once_aPeriodDMType);
            spinnerType.setSelection(spinnerPosition);
        }
    }
}
