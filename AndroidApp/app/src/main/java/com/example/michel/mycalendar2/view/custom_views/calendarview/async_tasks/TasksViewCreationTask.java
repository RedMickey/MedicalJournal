package com.example.michel.mycalendar2.view.custom_views.calendarview.async_tasks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.controllers.app_async_tasks.synchronization.SynchronizationReminderEntriesTask;
import com.example.michel.mycalendar2.services.authentication.AccountGeneralUtils;
import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.view.custom_views.calendarview.data.DateData;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;
import com.example.michel.mycalendar2.utils.ConvertingUtils;
import com.example.michel.mycalendar2.utils.utilModels.PillAndMeasurementReminderEntries;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class TasksViewCreationTask extends AsyncTask<DateData, Void, PillAndMeasurementReminderEntries>{

    private View mView;
    private boolean isCanceledDialog = false;

    public TasksViewCreationTask(View view){
        super();
        this.mView = view;
    }

    @Override
    protected PillAndMeasurementReminderEntries doInBackground(DateData... dateData) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        PillReminderDao pillReminderDao = new PillReminderDao(databaseAdapter.open().getDatabase());
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.getDatabase());
        List<PillReminderEntry> pillReminderEntries = pillReminderDao.getPillReminderEntriesByDate(dateData[0], -1);
        List<MeasurementReminderEntry> measurementReminderEntries = measurementReminderDao.getMeasurementReminderEntriesByDate(dateData[0], -1);
        databaseAdapter.close();

        return new PillAndMeasurementReminderEntries(pillReminderEntries, measurementReminderEntries);
    }

    @Override
    protected void onPostExecute(PillAndMeasurementReminderEntries pillAndMeasurementReminderEntries) {
        List<PillReminderEntry> pillReminderEntries = pillAndMeasurementReminderEntries.pillReminderEntries;
        List<MeasurementReminderEntry> measurementReminderEntries = pillAndMeasurementReminderEntries.measurementReminderEntries;
        LinearLayout tasksLayout = (LinearLayout)mView.findViewById(R.id.pill_reminder_entries_layout);

        LayoutInflater inflater = LayoutInflater.from(mView.getContext());
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        if(pillReminderEntries.size()>0)
        {
            calendar2.setTime(pillReminderEntries.get(0).getDate());

            for (final PillReminderEntry pre:pillReminderEntries) {
                View pillReminderEntryView = inflater.inflate(R.layout.pill_reminder_entry, null, false);
                ((TextView) pillReminderEntryView.findViewById(R.id.reminder_name_tv)).setText(pre.getPillName());
                final TextView reminderTimeTv = (TextView) pillReminderEntryView.findViewById(R.id.reminder_time_tv);
                reminderTimeTv.setText(new SimpleDateFormat("HH:mm").format(pre.getDate()));
                ((TextView) pillReminderEntryView.findViewById(R.id.reminder_count_type_tv))
                        .setText(String.valueOf(pre.getPillCount())+" "+pre.getPillCountType());
                switch (pre.getHavingMealsType()){
                    case 1:
                        ((ImageView) pillReminderEntryView.findViewById(R.id.having_meals_iv)).setImageResource(R.drawable.icons8_50_21);
                        break;
                    case 2:
                        ((ImageView) pillReminderEntryView.findViewById(R.id.having_meals_iv)).setImageResource(R.drawable.icons8_50_61);
                        break;
                    case 3:
                        ((ImageView) pillReminderEntryView.findViewById(R.id.having_meals_iv)).setImageResource(R.drawable.icons8_50_4);
                        break;
                }
                final ImageView imageTimeExpired = ((ImageView) pillReminderEntryView.findViewById(R.id.late_indication));
                if(pre.isLate())
                    imageTimeExpired.setImageResource(R.drawable.ic_time_expired);
                CheckBox isDoneChb = (CheckBox) pillReminderEntryView.findViewById(R.id.is_done_chb);

                if (calendar2.get(Calendar.YEAR)>calendar1.get(Calendar.YEAR))
                    isDoneChb.setEnabled(false);
                else if(calendar2.get(Calendar.DAY_OF_YEAR)>calendar1.get(Calendar.DAY_OF_YEAR))
                    isDoneChb.setEnabled(false);

                if(pre.getIsDone()==1)
                    isDoneChb.setChecked(true);
                isDoneChb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                        PillReminderDao pillReminderDao = new PillReminderDao(databaseAdapter.open().getDatabase());
                        if (b){
                            Calendar cal = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                            String curTime = sdf.format(cal.getTime());
                            reminderTimeTv.setText(curTime);
                            pillReminderDao.updateIsDonePillReminderEntry(1, pre.getId(), curTime+":00");
                            if (AccountGeneralUtils.curUser.getId()!=1) {
                                SynchronizationReminderEntriesTask synchronizationReminderEntriesTask = new SynchronizationReminderEntriesTask(
                                        mView.getContext(), pre.getId(), 1
                                );
                                synchronizationReminderEntriesTask.execute();
                            }
                            if (pre.isLate())
                                imageTimeExpired.setImageResource(android.R.color.transparent);
                        }
                        else {
                            pillReminderDao.updateIsDonePillReminderEntry( 0, pre.getId(), "");
                            if (AccountGeneralUtils.curUser.getId()!=1) {
                                SynchronizationReminderEntriesTask synchronizationReminderEntriesTask = new SynchronizationReminderEntriesTask(
                                        mView.getContext(), pre.getId(), 1
                                );
                                synchronizationReminderEntriesTask.execute();
                            }
                            if (pre.isLateCheck())
                                imageTimeExpired.setImageResource(R.drawable.ic_time_expired);
                        }
                        //databaseAdapter.updateIsDonePillReminderEntry(b ? 1 : 0, pre.getId());
                        databaseAdapter.close();
                    }
                });
                tasksLayout.addView(pillReminderEntryView);
            }
        }

        if(measurementReminderEntries.size()>0)
        {
            calendar2.setTime(measurementReminderEntries.get(0).getDate());

            for (final MeasurementReminderEntry mre:measurementReminderEntries) {
                View measurementReminderEntryView = inflater.inflate(R.layout.pill_reminder_entry, null, false);
                measurementReminderEntryView.setBackgroundColor(mView.getResources().getColor(R.color.meas_remind_ent_bg));

                boolean isInteger = true;
                ((TextView) measurementReminderEntryView.findViewById(R.id.reminder_name_tv)).setText(mre.getMeasurementTypeName());
                switch (mre.getIdMeasurementType()){
                    case 1:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_thermometer);
                        isInteger = false;
                        break;
                    case 2:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_tonometer2);
                        isInteger = true;
                        break;
                    case 3:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_pulse);
                        isInteger = true;
                        break;
                    case 4:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_glucometer);
                        isInteger = false;
                        break;
                    case 5:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_weight);
                        isInteger = false;
                        break;
                    case 6:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_burning);
                        isInteger = false;
                        break;
                    case 7:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_food);
                        isInteger = false;
                        break;
                    case 8:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_footprint);
                        isInteger = true;
                        break;
                }

                final TextView reminderTimeTv = (TextView) measurementReminderEntryView.findViewById(R.id.reminder_time_tv);
                reminderTimeTv.setText(new SimpleDateFormat("HH:mm").format(mre.getDate()));

                final TextView reminderCountTypeTv = (TextView) measurementReminderEntryView.findViewById(R.id.reminder_count_type_tv);

                switch (mre.getHavingMealsType()){
                    case 1:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.having_meals_iv)).setImageResource(R.drawable.icons8_50_21);
                        break;
                    case 2:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.having_meals_iv)).setImageResource(R.drawable.icons8_50_61);
                        break;
                    case 3:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.having_meals_iv)).setImageResource(R.drawable.icons8_50_4);
                        break;
                }
                final ImageView imageTimeExpired = ((ImageView) measurementReminderEntryView.findViewById(R.id.late_indication));
                if(mre.isLate())
                    imageTimeExpired.setImageResource(R.drawable.ic_time_expired);
                final CheckBox isDoneChb = (CheckBox) measurementReminderEntryView.findViewById(R.id.is_done_chb);

                if (calendar2.get(Calendar.YEAR)>calendar1.get(Calendar.YEAR))
                    isDoneChb.setEnabled(false);
                else if(calendar2.get(Calendar.DAY_OF_YEAR)>calendar1.get(Calendar.DAY_OF_YEAR))
                    isDoneChb.setEnabled(false);

                if(mre.getIsDone()==1){
                    setUpReminderCountTypeTv(reminderCountTypeTv, mre, isInteger);
                    isDoneChb.setChecked(true);
                }

                isDoneChb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                        if (b){
                            //openDialog(mre, reminderTimeTv, isDoneChb);

                            AlertDialog.Builder builder = new AlertDialog.Builder(mView.getContext());
                            LayoutInflater inflater = LayoutInflater.from(mView.getContext());

                            View dialogView = inflater.inflate(R.layout.alertdialog_measurement_input_view, null, false);

                            final EditText userValue1 = (EditText) dialogView.findViewById(R.id.value1_et);
                            final EditText userValue2 = (EditText) dialogView.findViewById(R.id.value2_et);
                            final boolean isInteger;

                            userValue1.setText(mre.getValue1()==-10000?"":String.valueOf(mre.getValue1()));
                            userValue2.setText(mre.getValue2()==-10000?"":String.valueOf(mre.getValue2()));

                            switch (mre.getIdMeasurementType()){
                                case 1:
                                    ((ImageView) dialogView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_thermometer);
                                    userValue2.setVisibility(View.GONE);
                                    ((TextView) dialogView.findViewById(R.id.value_type_name)).setText(mre.getMeasurementValueTypeName());
                                    isInteger = false;
                                    break;
                                case 2:
                                    ((ImageView) dialogView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_tonometer2);
                                    ((TextView) dialogView.findViewById(R.id.value_type_name)).setText(mre.getMeasurementValueTypeName());
                                    isInteger = true;
                                    break;
                                case 3:
                                    ((ImageView) dialogView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_pulse);
                                    userValue2.setVisibility(View.GONE);
                                    ((TextView) dialogView.findViewById(R.id.value_type_name)).setText(mre.getMeasurementValueTypeName());
                                    isInteger = true;
                                    break;
                                case 4:
                                    ((ImageView) dialogView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_glucometer);
                                    userValue2.setVisibility(View.GONE);
                                    ((TextView) dialogView.findViewById(R.id.value_type_name)).setText(mre.getMeasurementValueTypeName());
                                    isInteger = false;
                                    break;
                                case 5:
                                    ((ImageView) dialogView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_weight);
                                    userValue2.setVisibility(View.GONE);
                                    ((TextView) dialogView.findViewById(R.id.value_type_name)).setText(mre.getMeasurementValueTypeName());
                                    isInteger = false;
                                    break;
                                case 6:
                                    ((ImageView) dialogView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_burning);
                                    userValue2.setVisibility(View.GONE);
                                    ((TextView) dialogView.findViewById(R.id.value_type_name)).setText(mre.getMeasurementValueTypeName());
                                    isInteger = false;
                                    break;
                                case 7:
                                    ((ImageView) dialogView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_food);
                                    userValue2.setVisibility(View.GONE);
                                    ((TextView) dialogView.findViewById(R.id.value_type_name)).setText(mre.getMeasurementValueTypeName());
                                    isInteger = false;
                                    break;
                                case 8:
                                    ((ImageView) dialogView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_footprint1b);
                                    userValue2.setVisibility(View.GONE);
                                    ((TextView) dialogView.findViewById(R.id.value_type_name)).setText("шагов");
                                    isInteger = true;
                                    break;
                                    default:
                                        isInteger = true;
                                        break;
                            }

                            builder.setView(dialogView)
                                    .setTitle(mre.getMeasurementTypeName())
                                    .setCancelable(false)
                                    .setPositiveButton("Добавить",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    Calendar cal = Calendar.getInstance();
                                                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                                                    String curTime = sdf.format(cal.getTime());
                                                    reminderTimeTv.setText(curTime);
                                                    try {
                                                        mre.setValue1(Double.parseDouble(userValue1.getText().toString()));
                                                    }
                                                    catch (NumberFormatException nfe){
                                                        Log.e("Err", nfe.getMessage());
                                                        Toast.makeText(mView.getContext(),"Введите значения",Toast.LENGTH_LONG).show();
                                                        isCanceledDialog = true;
                                                        isDoneChb.setChecked(false);
                                                        return;
                                                    }

                                                    if (userValue2.getVisibility()!=View.GONE)
                                                        try {
                                                            mre.setValue2(Double.parseDouble(userValue2.getText().toString()));
                                                        }
                                                        catch (NumberFormatException nfe){
                                                            Log.e("Err", nfe.getMessage());
                                                            Toast.makeText(mView.getContext(),"Введите значения",Toast.LENGTH_LONG).show();
                                                            isCanceledDialog = true;
                                                            isDoneChb.setChecked(false);
                                                            return;
                                                        }
                                                    setUpReminderCountTypeTv(reminderCountTypeTv, mre, isInteger);

                                                    DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                                                    MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.open().getDatabase());
                                                    measurementReminderDao.updateIsDoneMeasurementReminderEntry(1, mre.getId(), curTime+":00",
                                                            mre.getValue1(), mre.getValue2(), 0);
                                                    databaseAdapter.close();
                                                    if (AccountGeneralUtils.curUser.getId()!=1) {
                                                        SynchronizationReminderEntriesTask synchronizationReminderEntriesTask = new SynchronizationReminderEntriesTask(
                                                                mView.getContext(), mre.getId(), 2
                                                        );
                                                        synchronizationReminderEntriesTask.execute();
                                                    }
                                                    if (mre.isLate())
                                                        imageTimeExpired.setImageResource(android.R.color.transparent);
                                                }
                                            })
                                    .setNegativeButton("Отмена",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog,int id) {
                                                    isCanceledDialog = true;
                                                    dialog.cancel();
                                                    isDoneChb.setChecked(false);
                                                }
                                            });

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                        else{
                            if (!isCanceledDialog) {
                                //Toast.makeText(mView.getContext(), "no", Toast.LENGTH_SHORT).show();
                                if (mre.isLateCheck())
                                    imageTimeExpired.setImageResource(R.drawable.ic_time_expired);
                                DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                                MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.open().getDatabase());
                                measurementReminderDao.updateIsDoneMeasurementReminderEntry(0, mre.getId(), "",
                                        mre.getValue1(), mre.getValue2(), 1);
                                databaseAdapter.close();
                                if (AccountGeneralUtils.curUser.getId()!=1) {
                                    SynchronizationReminderEntriesTask synchronizationReminderEntriesTask = new SynchronizationReminderEntriesTask(
                                            mView.getContext(), mre.getId(), 2
                                    );
                                    synchronizationReminderEntriesTask.execute();
                                }
                                reminderCountTypeTv.setText("");
                            }
                            else
                                isCanceledDialog = false;

                            }
                    }
                });
                tasksLayout.addView(measurementReminderEntryView);
            }
        }

        if(pillReminderEntries.size()==0&&measurementReminderEntries.size()==0)
        {
            TextView taskNote = new TextView(mView.getContext());
            LinearLayout.LayoutParams taskNoteParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            taskNote.setText("Задачи еще не добавлены!");
            taskNote.setLayoutParams(taskNoteParams);
            taskNote.setGravity(Gravity.CENTER);
            taskNote.setTextSize(17);
            tasksLayout.addView(taskNote);
        }

    }

    private void setUpReminderCountTypeTv(final TextView reminderCountTypeTv, final MeasurementReminderEntry mre, boolean isInteger){
        String valueStr = "";
        if (isInteger){
            if (mre.getValue2()!=-10000)
            {
                valueStr = String.format("%.0f - %.0f %s", mre.getValue1(), mre.getValue2(),
                        createCountTypeEnding(mre, mre.getValue2()));

            }
            else
                valueStr = String.format("%.0f %s", mre.getValue1(),
                        createCountTypeEnding(mre, mre.getValue1()));
        }
        else{
            valueStr = String.format("%.1f %s", mre.getValue1(),
                    createCountTypeEnding(mre, mre.getValue1()));
        }
        reminderCountTypeTv.setText(valueStr);
    }

    private String createCountTypeEnding(MeasurementReminderEntry mre, double value){
        switch (mre.getIdMeasurementType()){
            case 8:
                return ConvertingUtils.smartEnding((int)value, new String[]{"", "а", "ов"}, mre.getMeasurementValueTypeName());
                default:
                    return mre.getMeasurementValueTypeName();
        }
    }
}
