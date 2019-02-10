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

import com.example.michel.mycalendar2.activities.AddTreatmentActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.adapters.TimesOfTakingMedicineAdapter;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;
import com.example.michel.mycalendar2.utils.DBStaticEntries;

import java.util.UUID;

public class AddTreatmentActivityCreationTask extends AsyncTask<UUID, Void, CycleAndPillComby> {
    private AddTreatmentActivity view;
    private DateData pickDateButtonDateData;
    private TimesOfTakingMedicineAdapter timesOfTakingMedicineAdapter;

    public AddTreatmentActivityCreationTask(AddTreatmentActivity view, DateData pickDateButtonDateData, TimesOfTakingMedicineAdapter timesOfTakingMedicineAdapter){
        super();
        this.view = view;
        this.pickDateButtonDateData = pickDateButtonDateData;
        this.timesOfTakingMedicineAdapter = timesOfTakingMedicineAdapter;
    }

    @Override
    protected CycleAndPillComby doInBackground(UUID... uuids) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        CycleAndPillComby cycleAndPillComby = databaseAdapter.getCycleAndPillCombyByID(uuids[0]);

        databaseAdapter.close();

        return cycleAndPillComby;
    }

    @Override
    protected void onPostExecute(CycleAndPillComby cycleAndPillComby) {
        PillReminderDBInsertEntry prdbie = cycleAndPillComby.pillReminderDBInsertEntry;
        CycleDBInsertEntry cdbie = cycleAndPillComby.cycleDBInsertEntry;

        ((TextView)view.findViewById(R.id.active_ind_tv_CaddT)).setText((prdbie.getIsActive()==1?"Активное":"Завершённое"));
        ((Switch)view.findViewById(R.id.switch_active_type)).setChecked((prdbie.getIsActive()==1?true:false));
        ((TextView)view.findViewById(R.id.edit_text_medicine_name)).setText(prdbie.getPillName());
        view.setOldPillReminder(prdbie);
        view.setIdWeekSchedule(cdbie.getIdWeekSchedule());
        ((TextView)view.findViewById(R.id.edit_text_dose)).setText(String.valueOf(prdbie.getPillCount()));
        String mapDoseTypesValue = DBStaticEntries.getKeyByValue2(DBStaticEntries.doseTypes, prdbie.getIdPillCountType());
        Spinner spinnerDoseType = (Spinner)view.findViewById(R.id.dose_type);
        int spinnerPosition = ((ArrayAdapter) spinnerDoseType.getAdapter()).getPosition(mapDoseTypesValue);
        spinnerDoseType.setSelection(spinnerPosition);
        ((Button)view.findViewById(R.id.pick_start_date_button)).setText(setDate(prdbie.getStartDate().getDay(), prdbie.getStartDate().getMonth(), prdbie.getStartDate().getYear()));
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
        switch (prdbie.getIdHavingMealsType()){
            case 1:
                radioGroupRegardingMeals.check(R.id.before_meals);
                ((EditText)view.findViewById(R.id.count_of_minutes)).setText(String.valueOf(prdbie.getHavingMealsTime()));
                break;
            case 2:
                radioGroupRegardingMeals.check(R.id.with_meals);
                break;
            case 3:
                radioGroupRegardingMeals.check(R.id.after_meals);
                ((EditText)view.findViewById(R.id.count_of_minutes)).setText(String.valueOf(prdbie.getHavingMealsTime()));
                break;
        }

        LinearLayout timesOfTakingMedicine = (LinearLayout) view.findViewById(R.id.times_of_taking_medicine);
        for(int i=0; i<prdbie.getReminderTimes().length;i++){
            timesOfTakingMedicineAdapter.add(prdbie.getReminderTimes()[i].getReminderTimeStr());
            timesOfTakingMedicineAdapter.notifyDataSetChanged();
            View item = timesOfTakingMedicineAdapter.getView(timesOfTakingMedicineAdapter.getCount()-1, null, null);
            timesOfTakingMedicine.addView(item);
        }
        ((TextView)view.findViewById(R.id.annotation)).setText(prdbie.getAnnotation());

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
