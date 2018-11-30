package com.example.michel.mycalendar2;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.utils.DatabaseHelper;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.ReminderTime;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class DatabaseAdapterTest{

    ReminderTime[] reminderTimesReference = new ReminderTime[]{
        new ReminderTime(2, "16:45"),
            new ReminderTime(3, "04:12"),
            new ReminderTime(6, "02:11"),
            new ReminderTime(9, "20:15")
    };

    @Before
    public void setUp(){
        DatabaseHelper databaseHelper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
        // создаем базу данных
        databaseHelper.create_db();
        DatabaseAdapter.AppContext = InstrumentationRegistry.getTargetContext();
    }

    @Test
    public void test_getCycleAndPillCombyByID_method1(){

        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        CycleAndPillComby cycleAndPillComby = databaseAdapter.getCycleAndPillCombyByID(1);

        databaseAdapter.close();

        Assert.assertArrayEquals( reminderTimesReference, cycleAndPillComby.pillReminderDBInsertEntry.getReminderTimes());
    }

    @Test
    public void test_getCycleAndPillCombyByID_method2(){
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        CycleAndPillComby cycleAndPillComby = databaseAdapter.getCycleAndPillCombyByID(112);

        databaseAdapter.close();

        Assert.assertNull( cycleAndPillComby.pillReminderDBInsertEntry);
    }

    @Test
    public void test_getCycleAndPillCombyByID_method3(){

        DatabaseAdapter dba = spy(DatabaseAdapter.class);
        doReturn(reminderTimesReference).when(dba).getPillReminderEntriesTime(anyInt(), anyString());

        DatabaseHelper databaseHelper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
        dba.setDatabaseHelper(databaseHelper);

        dba.open();
        CycleAndPillComby cycleAndPillComby = dba.getCycleAndPillCombyByID(1);
        //ReminderTime[] rr = dba.getPillReminderEntriesTime(1,"0000-00-00");
        dba.close();

        Assert.assertArrayEquals( reminderTimesReference, cycleAndPillComby.pillReminderDBInsertEntry.getReminderTimes());
    }
}
