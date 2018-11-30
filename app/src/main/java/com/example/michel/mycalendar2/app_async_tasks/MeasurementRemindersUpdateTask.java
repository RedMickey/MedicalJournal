package com.example.michel.mycalendar2.app_async_tasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.models.CycleAndMeasurementComby;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.measurement.MeasurementReminderDBEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MeasurementRemindersUpdateTask extends AsyncTask<CycleAndMeasurementComby, Void, Void> {
    //private boolean needUpdateMeasurement;
    private Context appContext;

    public MeasurementRemindersUpdateTask(Context context){
        super();
        //this.needUpdateMeasurement = needUpdateMeasurement;
        appContext = context;
    }

    @Override
    protected Void doInBackground(CycleAndMeasurementComby... cycleAndMeasurementCombies) {
        MeasurementReminderDBEntry mrdbe = cycleAndMeasurementCombies[0].measurementReminderDBEntry;
        CycleDBInsertEntry cdbie = cycleAndMeasurementCombies[0].cycleDBInsertEntry;
        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        dbAdapter.open();

        dbAdapter.updateCycle(cdbie.getIdCycle(), cdbie.getIdWeekSchedule(),
                cdbie.getPeriod(), cdbie.getPeriodDMType(),
                cdbie.getOnce_aPeriod(), cdbie.getOnce_aPeriodDMType(),
                cdbie.getIdCyclingType(), cdbie.getWeekSchedule()
        );

        dbAdapter.updateMeasurementReminder( mrdbe.getIdMeasurementReminder(),
                mrdbe.getStartDate().getDateString(),
                mrdbe.getIdCycle(), mrdbe.getIdHavingMealsType(),
                mrdbe.getHavingMealsTime(), mrdbe.getAnnotation(),
                mrdbe.getIsActive(), mrdbe.getReminderTimes().length
        );

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String curDate = sdf.format(cal.getTime());
        dbAdapter.deleteMeasurementReminderEntriesAfterDate(mrdbe.getIdMeasurementReminder(), curDate);
        // need check for change start date

        dbAdapter.deleteReminderTimeByReminderId(mrdbe.getIdMeasurementReminder(),1);

        for (int i=0; i<mrdbe.getReminderTimes().length; i++){
            dbAdapter.insertReminderTime(mrdbe.getReminderTimes()[i].getReminderTimeStr(), mrdbe.getIdMeasurementReminder(), 1);
        }

        if (mrdbe.getIsActive()==1) {
            switch (cdbie.getIdCyclingType()){
                case 1:
                    for(int i=0; i<cdbie.getDayCount();i++){
                        for (int j=0; j<mrdbe.getReminderTimes().length; j++){
                            dbAdapter.insertMeasurementReminderEntries(
                                    sdf.format(new Date(cal.getTimeInMillis())),
                                    mrdbe.getIdMeasurementReminder(),
                                    mrdbe.getReminderTimes()[j].getReminderTimeStr()
                            );
                        }
                        cal.add(Calendar.DATE, 1);
                    }
                    break;
                case 2:
                    for(int i=0; i<cdbie.getDayCount();i++){
                        if(cdbie.getWeekSchedule()[cal.get(Calendar.DAY_OF_WEEK)-1]==1){
                            for (int j=0; j<mrdbe.getReminderTimes().length; j++){
                                dbAdapter.insertMeasurementReminderEntries(
                                        sdf.format(cal.getTime()),
                                        mrdbe.getIdMeasurementReminder(),
                                        mrdbe.getReminderTimes()[j].getReminderTimeStr()
                                );
                            }
                        }
                        cal.add(Calendar.DATE, 1);
                    }
                    break;
                case 3:
                    for(int i=0; i<cdbie.getDayCount();i+=cdbie.getDayInterval()){
                        for (int j=0; j<mrdbe.getReminderTimes().length; j++){
                            dbAdapter.insertMeasurementReminderEntries(
                                    sdf.format(new Date(cal.getTimeInMillis())),
                                    mrdbe.getIdMeasurementReminder(),
                                    mrdbe.getReminderTimes()[j].getReminderTimeStr()
                            );
                        }
                        cal.add(Calendar.DATE, cdbie.getDayInterval());
                    }
                    break;
            }
        }

        dbAdapter.close();

        return null;
    }
}
