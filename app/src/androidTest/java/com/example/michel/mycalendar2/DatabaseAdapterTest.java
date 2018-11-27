package com.example.michel.mycalendar2;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.utils.DatabaseHelper;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.ReminderTime;

import org.junit.Assert;
import org.junit.Test;

public class DatabaseAdapterTest {


    ReminderTime[] reminderTimesReference = new ReminderTime[]{
        new ReminderTime(2, "16:45"),
            new ReminderTime(3, "04:12"),
            new ReminderTime(6, "02:11"),
            new ReminderTime(9, "20:15")
    };

    @Test
    public void test_getCycleAndPillCombyByID_method(){
        DatabaseHelper databaseHelper = new DatabaseHelper(getApplicationContext());
        // создаем базу данных
        databaseHelper.create_db();
        DatabaseAdapter.AppContext = getApplicationContext();

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        CycleAndPillComby cycleAndPillComby = databaseAdapter.getCycleAndPillCombyByID(1);

        databaseAdapter.close();

        Assert.assertArrayEquals(reminderTimesReference, cycleAndPillComby.pillReminderDBInsertEntry.getReminderTimes());
    }

}
