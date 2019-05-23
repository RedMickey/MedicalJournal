package com.example.michel.mycalendar2;

import android.support.test.InstrumentationRegistry;

import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.dao.DatabaseHelper;
import com.example.michel.mycalendar2.models.CycleAndPillComby;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
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
        doReturn(reminderTimesReference).when(dba).getReminderEntriesTime(anyInt(), anyString(),eq(0));

        DatabaseHelper databaseHelper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
        dba.setDatabaseHelper(databaseHelper);

        dba.open();
        CycleAndPillComby cycleAndPillComby = dba.getCycleAndPillCombyByID(1);
        dba.close();

        Assert.assertArrayEquals( reminderTimesReference, cycleAndPillComby.pillReminderDBInsertEntry.getReminderTimes());
    }

    @Test
    public void test_getPillReminderEntriesByDate_method1(){
        PillReminderEntry[] pillReminderEntriesRef = new PillReminderEntry[2];
        Calendar cal = Calendar.getInstance();
        cal.clear();
        cal.set(2018, 11, 1, 11, 0, 0);
        Date date = cal.getTime();
        cal.set(2018, 11, 1, 11, 13, 0);
        Date havingMealsTime = cal.getTime();
        pillReminderEntriesRef[0] = new PillReminderEntry(
           3, "птабл3", 2, "шт",
                date, 1, havingMealsTime, 0, true
        );
        cal.set(2018, 11, 1, 11, 25, 0);
        Date date2 = cal.getTime();
        cal.set(2018, 11, 1, 11, 41, 0);
        Date havingMealsTime2 = cal.getTime();
        pillReminderEntriesRef[1] = new PillReminderEntry(
                10, "таблетка1", 1, "капли",
                date2, 3, havingMealsTime2, 0, true
        );

        DatabaseAdapter dba = new DatabaseAdapter();
        dba.open();
        List<PillReminderEntry> pillReminderEntries = dba.getPillReminderEntriesByDate(new DateData(2018,12,1));
        PillReminderEntry[] pres = pillReminderEntries.toArray(new PillReminderEntry[pillReminderEntries.size()]);
        dba.close();

        Assert.assertArrayEquals(pillReminderEntriesRef, pres);
    }

    @Test
    public void test_getPillReminderEntriesByDate_method2(){
        DatabaseAdapter dba = new DatabaseAdapter();
        dba.open();
        List<PillReminderEntry> pillReminderEntries = dba.getPillReminderEntriesByDate(new DateData(2018,12,2));
        dba.close();

        Assert.assertEquals(0, pillReminderEntries.size());
    }
}
