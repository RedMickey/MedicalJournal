package com.example.michel.mycalendar2;

import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;

import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class calculateRemainingRemindersTest {
    Calendar cal= Calendar.getInstance();

    List<PillReminderEntry> remainingReminders = new ArrayList<>(
            Arrays.asList(
                    new PillReminderEntry(1, "pill0", 1, "шт", new Date(), 1, new Date(), 1, false),
                    new PillReminderEntry(2, "pill1", 1, "шт", new Date(), 1, new Date(), 1, false),
                    new PillReminderEntry(3, "pill2", 1, "шт", new Date(), 1, new Date(), 1, false),
                    new PillReminderEntry(4, "pill3", 1, "шт", new Date(), 1, new Date(), 1, false),
                    new PillReminderEntry(5, "pill4", 1, "шт", new Date(), 1, new Date(), 1, false)
            )
    );
    //countOfRemainingReminders[0] - count of morning taking
    //countOfRemainingReminders[1] - count of day taking
    //countOfRemainingReminders[2] - count of evening taking
    //countOfRemainingReminders[3] - count of Remaining Reminders
    int[] calculatedResult1 = new int[]{1, 2, 2, 4};
    int[] calculatedResult2 = new int[]{4, 1, 0, 6};
    int[] calculatedResult3 = new int[]{0, 0, 0, 10};

    @Test
    public void test_calculateRemainingReminders_method1(){
        cal.set(Calendar.HOUR_OF_DAY, 7);
        remainingReminders.get(0).setDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 13);
        remainingReminders.get(1).setDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 16);
        remainingReminders.get(2).setDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 19);
        remainingReminders.get(3).setDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 21);
        remainingReminders.get(4).setDate(cal.getTime());

        Assert.assertArrayEquals(calculatedResult1, CalendarUtil.calculateRemainingReminders(remainingReminders, 9));
    }

    @Test
    public void test_calculateRemainingReminders_method2(){
        cal.set(Calendar.HOUR_OF_DAY, 4);
        remainingReminders.get(0).setDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 5);
        remainingReminders.get(1).setDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 6);
        remainingReminders.get(2).setDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 7);
        remainingReminders.get(3).setDate(cal.getTime());
        cal.set(Calendar.HOUR_OF_DAY, 14);
        remainingReminders.get(4).setDate(cal.getTime());

        Assert.assertArrayEquals(calculatedResult2, CalendarUtil.calculateRemainingReminders(remainingReminders, 11));
    }

    @Test
    public void test_calculateRemainingReminders_method3(){
        Assert.assertArrayEquals(calculatedResult3, CalendarUtil.calculateRemainingReminders(new ArrayList<PillReminderEntry>(), 10));
    }

}


