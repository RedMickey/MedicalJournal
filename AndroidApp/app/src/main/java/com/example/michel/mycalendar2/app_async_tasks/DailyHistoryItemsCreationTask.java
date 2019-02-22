package com.example.michel.mycalendar2.app_async_tasks;

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
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.dao.MeasurementReminderDao;
import com.example.michel.mycalendar2.dao.PillReminderDao;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminder;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderEntry;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;
import com.example.michel.mycalendar2.utils.utilModels.PillAndMeasurementReminderEntries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyHistoryItemsCreationTask extends AsyncTask<DateData, Void, List<PillAndMeasurementReminderEntries>>{
    private View mView;
    private boolean isCanceledDialog = false;

    public DailyHistoryItemsCreationTask(View view){
        super();
        this.mView = view;
    }

    @Override
    protected List<PillAndMeasurementReminderEntries> doInBackground(DateData... dateData) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        PillReminderDao pillReminderDao = new PillReminderDao(databaseAdapter.open().getDatabase());
        MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.getDatabase());

        List<PillReminderEntry> pillReminderEntryList;
        List<MeasurementReminderEntry> measurementReminderEntryList;
        if (dateData[0].getYear()!=0){
            pillReminderEntryList = pillReminderDao.getPillReminderEntriesBetweenDates(dateData[0], dateData[1], 0);
            measurementReminderEntryList = measurementReminderDao.getMeasurementReminderEntriesBetweenDates(dateData[0], dateData[1], 0);
        }
        else {
            pillReminderEntryList = pillReminderDao.getPillReminderEntriesBetweenDates(dateData[0], dateData[1], 1);
            measurementReminderEntryList = measurementReminderDao.getMeasurementReminderEntriesBetweenDates(dateData[0], dateData[1], 1);
        }

        PillReminderEntry[] pillReminderEntryArr = pillReminderEntryList.toArray(new PillReminderEntry[pillReminderEntryList.size()]);
        MeasurementReminderEntry[] measurementReminderEntryArr = measurementReminderEntryList.toArray(new MeasurementReminderEntry[measurementReminderEntryList.size()]);
        databaseAdapter.close();

        List<PillAndMeasurementReminderEntries> pillAndMeasurementReminderEntriesList = new ArrayList<PillAndMeasurementReminderEntries>();
        if (measurementReminderEntryArr.length>0||pillReminderEntryArr.length>0) {
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            int ip=0, im=0;
            //DateData endDate = new DateData();
            if (measurementReminderEntryArr.length>0&&pillReminderEntryArr.length>0){
                cal1.setTime(measurementReminderEntryArr[measurementReminderEntryArr.length-1].getDate().
                        compareTo(pillReminderEntryArr[pillReminderEntryArr.length-1].getDate())<0?
                        measurementReminderEntryArr[measurementReminderEntryArr.length-1].getDate():
                        pillReminderEntryArr[pillReminderEntryArr.length-1].getDate());
                cal2.setTime(measurementReminderEntryArr[0].getDate().
                        compareTo(pillReminderEntryArr[0].getDate())>0?
                        measurementReminderEntryArr[0].getDate():
                        pillReminderEntryArr[0].getDate());
            }
            else if(measurementReminderEntryArr.length>0){
                cal1.setTime(measurementReminderEntryArr[measurementReminderEntryArr.length-1].getDate());
                cal2.setTime(measurementReminderEntryArr[0].getDate());
            }
            else {
                cal1.setTime(pillReminderEntryArr[pillReminderEntryArr.length-1].getDate());
                cal2.setTime(pillReminderEntryArr[0].getDate());
            }

            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);
            Date endDate = cal1.getTime();
            /*endDate.setYear(cal1.get(Calendar.YEAR));
            endDate.setMonth(cal1.get(Calendar.MONTH));
            endDate.setDay(cal1.get(Calendar.DAY_OF_MONTH));*/

            int t = cal2.getTime().compareTo(endDate);
            while (cal2.getTime().compareTo(endDate)>=0){
                PillAndMeasurementReminderEntries pamre = new PillAndMeasurementReminderEntries();

                if (ip<pillReminderEntryArr.length){
                    cal1.setTime(pillReminderEntryArr[ip].getDate());
                    while (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)&&
                            cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)) {
                        pamre.pillReminderEntries.add(pillReminderEntryArr[ip]);
                        ip++;
                        try {
                            cal1.setTime(pillReminderEntryArr[ip].getDate());
                        }catch (ArrayIndexOutOfBoundsException aiobe){
                            break;
                        }
                    }
                }

                if (im<measurementReminderEntryArr.length){
                    cal1.setTime(measurementReminderEntryArr[im].getDate());
                    while (cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)&&
                            cal1.get(Calendar.MONTH)==cal2.get(Calendar.MONTH)) {
                        pamre.measurementReminderEntries.add(measurementReminderEntryArr[im]);
                        //if (im<measurementReminderEntryArr.length)
                        im++;
                        try {
                            cal1.setTime(measurementReminderEntryArr[im].getDate());
                        }
                        catch (ArrayIndexOutOfBoundsException aiobe){
                            break;
                        }
                    }
                }

                if (pamre.measurementReminderEntries.size() != 0 || pamre.pillReminderEntries.size() != 0)
                    pillAndMeasurementReminderEntriesList.add(pamre);

                cal2.add(Calendar.DAY_OF_MONTH, -1);
            }

        }

        return pillAndMeasurementReminderEntriesList;
    }

    @Override
    protected void onPostExecute(List<PillAndMeasurementReminderEntries> pillAndMeasurementReminderEntriesList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
        String todayDateStr = dateFormat.format(new Date());

        for (final PillAndMeasurementReminderEntries pamre: pillAndMeasurementReminderEntriesList) {
            LayoutInflater inflater = LayoutInflater.from(mView.getContext());
            LinearLayout tasksLayout = (LinearLayout)mView.findViewById(R.id.history_linear_layout);
            View dailyHistoryEntryView = inflater.inflate(R.layout.daily_history_item, null, false);
            String dateString = pamre.measurementReminderEntries.size()!=0?dateFormat.format(pamre.measurementReminderEntries.get(0).getDate())
                    :dateFormat.format(pamre.pillReminderEntries.get(0).getDate());
            if (dateString.equals(todayDateStr))
                ((TextView) dailyHistoryEntryView.findViewById(R.id.date_tv)).setText("Сегодня");
            else
                ((TextView) dailyHistoryEntryView.findViewById(R.id.date_tv)).setText(dateString);
            LinearLayout historyDayLayout= (LinearLayout) dailyHistoryEntryView.findViewById(R.id.history_day_item);

            createViewItem(pamre, historyDayLayout, inflater);

            tasksLayout.addView(dailyHistoryEntryView);
        }
        if(pillAndMeasurementReminderEntriesList.size()==0)
        {
            LinearLayout tasksLayout = (LinearLayout)mView.findViewById(R.id.history_linear_layout);
            TextView taskNote = new TextView(mView.getContext());
            LinearLayout.LayoutParams taskNoteParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            taskNote.setText("Empty");
            taskNote.setLayoutParams(taskNoteParams);
            taskNote.setGravity(Gravity.CENTER);
            tasksLayout.addView(taskNote);
        }
    }

    private void createViewItem(final PillAndMeasurementReminderEntries pillAndMeasurementReminderEntries, LinearLayout historyDayLayout,
                                LayoutInflater inflater){
        List<PillReminderEntry> pillReminderEntries = pillAndMeasurementReminderEntries.pillReminderEntries;
        List<MeasurementReminderEntry> measurementReminderEntries = pillAndMeasurementReminderEntries.measurementReminderEntries;
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
                            if (pre.isLate())
                                imageTimeExpired.setImageResource(android.R.color.transparent);
                        }
                        else {
                            pillReminderDao.updateIsDonePillReminderEntry( 0, pre.getId(), "");
                            if (pre.isLateCheck())
                                imageTimeExpired.setImageResource(R.drawable.ic_time_expired);
                        }
                        databaseAdapter.close();
                    }
                });
                historyDayLayout.addView(pillReminderEntryView);
            }
        }

        if(measurementReminderEntries.size()>0)
        {
            calendar2.setTime(measurementReminderEntries.get(0).getDate());
            for (final MeasurementReminderEntry mre:measurementReminderEntries) {
                View measurementReminderEntryView = inflater.inflate(R.layout.pill_reminder_entry, null, false);
                measurementReminderEntryView.setBackgroundColor(mView.getResources().getColor(R.color.meas_remind_ent_bg));

                ((TextView) measurementReminderEntryView.findViewById(R.id.reminder_name_tv)).setText(mre.getMeasurementTypeName());
                switch (mre.getIdMeasurementType()){
                    case 1:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_thermometer);
                        break;
                    case 2:
                        ((ImageView) measurementReminderEntryView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_tonometer);
                        break;
                }

                final TextView reminderTimeTv = (TextView) measurementReminderEntryView.findViewById(R.id.reminder_time_tv);
                reminderTimeTv.setText(new SimpleDateFormat("HH:mm").format(mre.getDate()));

                final TextView reminderCountTypeTv = (TextView) measurementReminderEntryView.findViewById(R.id.reminder_count_type_tv);
                if (mre.getIsDone()==1)
                    setUpReminderCountTypeTv(reminderCountTypeTv, mre);

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
                if(mre.getIsDone()==1)
                    isDoneChb.setChecked(true);

                /*isDoneChb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(mView.getContext(),"yes",Toast.LENGTH_SHORT).show();
                    }
                });*/
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

                            userValue1.setText(mre.getValue1()==-10000?"":String.valueOf(mre.getValue1()));
                            userValue2.setText(mre.getValue2()==-10000?"":String.valueOf(mre.getValue2()));

                            ((TextView) dialogView.findViewById(R.id.value_type_name)).setText(mre.getMeasurementValueTypeName());

                            switch (mre.getIdMeasurementType()){
                                case 1:
                                    ((ImageView) dialogView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_thermometer);
                                    userValue2.setVisibility(View.GONE);
                                    break;
                                case 2:
                                    ((ImageView) dialogView.findViewById(R.id.reminder_ic_iv)).setImageResource(R.drawable.ic_tonometer);
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
                                                    setUpReminderCountTypeTv(reminderCountTypeTv, mre);

                                                    DatabaseAdapter databaseAdapter = new DatabaseAdapter();
                                                    MeasurementReminderDao measurementReminderDao = new MeasurementReminderDao(databaseAdapter.open().getDatabase());
                                                    measurementReminderDao.updateIsDoneMeasurementReminderEntry(1, mre.getId(), curTime+":00",
                                                            mre.getValue1(), mre.getValue2(), 0);
                                                    databaseAdapter.close();
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
                                reminderCountTypeTv.setText("");
                            }
                            else
                                isCanceledDialog = false;

                        }
                    }
                });
                historyDayLayout.addView(measurementReminderEntryView);
            }
        }
    }

    private void setUpReminderCountTypeTv(final TextView reminderCountTypeTv, final MeasurementReminderEntry mre){
        if (mre.getValue1()!=-10000)
        {
            String valueStr = String.valueOf(mre.getValue1());
            if (mre.getValue2()!=-10000)
            {
                valueStr = valueStr + " - " + String.valueOf(mre.getValue2()) + " " + mre.getMeasurementValueTypeName();
            }
            else
                valueStr+= " " + mre.getMeasurementValueTypeName();
            reminderCountTypeTv.setText(valueStr);
        }
    }

}
