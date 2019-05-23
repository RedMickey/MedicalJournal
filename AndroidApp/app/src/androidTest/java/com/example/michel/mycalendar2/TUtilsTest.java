package com.example.michel.mycalendar2;

import android.support.test.InstrumentationRegistry;

import com.example.michel.mycalendar2.dao.DatabaseAdapter;
import com.example.michel.mycalendar2.view.custom_views.calendarview.data.DateData;
import com.example.michel.mycalendar2.dao.DatabaseHelper;
import com.example.michel.mycalendar2.models.CycleDBInsertEntry;
import com.example.michel.mycalendar2.models.ReminderTime;
import com.example.michel.mycalendar2.models.pill.PillReminderDBInsertEntry;
import com.example.michel.mycalendar2.utils.DBStaticEntries;
import com.example.michel.mycalendar2.utils.TUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class TUtilsTest {
    TUtils tUtils;

    @Before
    public void setUp(){
        tUtils = spy(TUtils.class);
        DatabaseHelper databaseHelper = new DatabaseHelper(InstrumentationRegistry.getTargetContext());
        // создаем базу данных
        databaseHelper.create_db();
        DatabaseAdapter.AppContext = InstrumentationRegistry.getTargetContext();
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();

        DBStaticEntries.cycleTypes = databaseAdapter.getCycleTypes();
        DBStaticEntries.dateTypes = databaseAdapter.getDateTypes();
        DBStaticEntries.doseTypes = databaseAdapter.getDoseTypes();
        DBStaticEntries.measurementTypes = databaseAdapter.getMeasurementTypes();

        databaseAdapter.close();
    }

    @Test
    public void test_setDateParams_method1(){
        CycleDBInsertEntry cycleDBInsertEntryRef = new CycleDBInsertEntry();
        cycleDBInsertEntryRef.setPeriod(10);
        cycleDBInsertEntryRef.setPeriodDMType(DBStaticEntries.dateTypes.get("нед."));
        cycleDBInsertEntryRef.setDayCount(70);

        CycleDBInsertEntry cycleDBInsertEntry = new CycleDBInsertEntry();

        tUtils.setDateParams(cycleDBInsertEntry, 10, "нед.", 0);
        Assert.assertEquals(cycleDBInsertEntryRef, cycleDBInsertEntry);
    }

    @Test
    public void test_setDateParams_method2(){
        CycleDBInsertEntry cycleDBInsertEntryRef = new CycleDBInsertEntry();
        cycleDBInsertEntryRef.setOnce_aPeriod(10);
        cycleDBInsertEntryRef.setOnce_aPeriodDMType(DBStaticEntries.dateTypes.get("дн."));
        cycleDBInsertEntryRef.setDayInterval(10);

        CycleDBInsertEntry cycleDBInsertEntry = new CycleDBInsertEntry();

        tUtils.setDateParams(cycleDBInsertEntry, 10, "дн.", 1);
        Assert.assertEquals(cycleDBInsertEntryRef, cycleDBInsertEntry);
    }

    @Test
    public void test_createPillReminders_method1(){
        CycleDBInsertEntry cycleDBInsertEntryRef = new CycleDBInsertEntry(
                14, 1, null, null, 1, null, 14
        );
        cycleDBInsertEntryRef.setIdCycle(9);
        doReturn(cycleDBInsertEntryRef).when(tUtils).setDateParams(any(CycleDBInsertEntry.class), anyInt(), anyString(), anyInt());

        PillReminderDBInsertEntry prdbie = new PillReminderDBInsertEntry(
                9, "таблетка1", 1, 1, new DateData(2018, 12, 10), 9,
                null,null, "", 1, new ReminderTime[]{new ReminderTime("12:00")}
        );

        int res = tUtils.createPillReminders(new CycleDBInsertEntry(), prdbie, 14, "шт.", 0);

        Assert.assertEquals(14, res);
    }

    @Test
    public void test_createPillReminders_method2(){
        CycleDBInsertEntry cycleDBInsertEntry = new CycleDBInsertEntry();
        cycleDBInsertEntry.setIdCycle(9);
        cycleDBInsertEntry.setIdCyclingType(1);

        PillReminderDBInsertEntry prdbie = new PillReminderDBInsertEntry(
                9, "таблетка1", 1, 1, new DateData(2018, 12, 10), 9,
                null,null, "", 1, new ReminderTime[]{new ReminderTime("12:00")}
        );

        int res = tUtils.createPillReminders(cycleDBInsertEntry, prdbie, 14, "дн.", 0);

        Assert.assertEquals(14, res);
    }

    @Test
    public void test_test_createPillReminders_method3(){
        Calendar cal = Calendar.getInstance();
        CycleDBInsertEntry cycleDBInsertEntry1 = new CycleDBInsertEntry(
                14, 1, null, null, 1, null, 14
        );
        PillReminderDBInsertEntry prdbie = new PillReminderDBInsertEntry(
                9, "таблетка1", 1, 1, new DateData(2018, 12, 10), 9,
                null,null, "", 1,
                new ReminderTime[]{
                        new ReminderTime("12:00"),
                        new ReminderTime("15:00")
                }
        );

        cal.set(prdbie.getStartDate().getYear(),
                prdbie.getStartDate().getMonth()-1,
                prdbie.getStartDate().getDay());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        dbAdapter.open();
        for(int i=0; i<cycleDBInsertEntry1.getDayCount();i++){
            for (int j=0; j<prdbie.getReminderTimes().length; j++){
                dbAdapter.insertPillReminderEntries(
                        sdf.format(new Date(cal.getTimeInMillis())),
                        prdbie.getIdPillReminder(),
                        prdbie.getReminderTimes()[j].getReminderTimeStr()
                );
            }
            cal.add(Calendar.DATE, 1);
        }
        int count = dbAdapter.getCountOfPillReminderEntries(9);

        dbAdapter.close();

        Assert.assertEquals(28, count);
    }

    @Test
    public void test_test_createPillReminders_method4(){
        CycleDBInsertEntry cycleDBInsertEntry = new CycleDBInsertEntry();
        cycleDBInsertEntry.setIdCycle(9);
        cycleDBInsertEntry.setIdCyclingType(1);

        PillReminderDBInsertEntry prdbie = new PillReminderDBInsertEntry(
                9, "таблетка1", 1, 1, new DateData(2018, 12, 10), 9,
                null,null, "", 1,
                new ReminderTime[]{
                        new ReminderTime("12:00"),
                        new ReminderTime("15:00")
                }
        );

        tUtils.createPillReminders(cycleDBInsertEntry, prdbie, 14, "дн.", 0);

        DatabaseAdapter dbAdapter = new DatabaseAdapter();
        dbAdapter.open();

        int count = dbAdapter.getCountOfPillReminderEntries(9);

        dbAdapter.close();

        Assert.assertEquals(28, count);
    }

}
