package com.example.michel.mycalendar2.activities;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.calendarview.CellConfig;
import com.example.michel.mycalendar2.calendarview.adapters.CalendarViewExpAdapter;
import com.example.michel.mycalendar2.calendarview.adapters.DayAdapter;
import com.example.michel.mycalendar2.calendarview.data.DateData;
import com.example.michel.mycalendar2.calendarview.data.DayDifference;
import com.example.michel.mycalendar2.calendarview.listeners.OnDateClickListener;
import com.example.michel.mycalendar2.calendarview.listeners.OnExpDateClickListener;
import com.example.michel.mycalendar2.calendarview.listeners.OnMonthScrollListener;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.calendarview.views.ExpCalendarView;
import com.example.michel.mycalendar2.calendarview.views.MonthExpFragment;
import com.example.michel.mycalendar2.calendarview.views.WeekDayViewPager;
import com.example.michel.mycalendar2.models.pill.PillReminderEntry;
import com.example.michel.mycalendar2.services.AlarmService;
import com.example.michel.mycalendar2.utils.AlarmReceiver;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;

public class MainFragment extends Fragment{

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private View view;
    private TextView YearMonthTv;
    private ExpCalendarView calendarView;
    private DateData selectedDate;
    private Calendar calendar;

    private SlidingUpPanelLayout slidingUpPanelLayout;
    private LinearLayout calendarLayout;

    private WeekDayViewPager weekDayViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.main_calendar_view, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                /*Intent intent = new Intent(new Intent(getContext(), AlarmService.class));
                intent.putExtra("isActual", 1);
                getActivity().startService(intent);*/

                //Intent deleteIntent = new Intent(this, MyService.class);
                //deleteIntent.setAction("ru.startandroid.notifications.action_delete");
                //PendingIntent deletePendingIntent = PendingIntent.getService(this, 0, deleteIntent, 0);

                Intent notificationIntent2 = new Intent(getContext(), AlarmService.class);
                notificationIntent2.putExtra("isActual", 1);
                notificationIntent2.putExtra("notifId", 0);
                int isActual = notificationIntent2.getIntExtra("isActual",0);
                PendingIntent pendingIntent = PendingIntent.getService(getContext(), 0, notificationIntent2, PendingIntent.FLAG_CANCEL_CURRENT);

                Intent notificationIntent = new Intent(getContext(), AddTreatmentActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(getContext(),
                        0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                Notification builder = new Notification.Builder(getContext())
                        .setContentIntent(contentIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Title")
                        .setContentText("Notification text")
                        .addAction(android.R.drawable.ic_delete, "Reject", pendingIntent)
                        .addAction(android.R.drawable.ic_input_add, "Accept", pendingIntent)
                        .build();

                /*NotificationCompat.Builder builder =
                        new NotificationCompat.Builder(getContext())
                                .setContentIntent(contentIntent)
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("Title")
                                .setContentText("Notification text")
                                .setAutoCancel(true)

                                .addAction(android.R.drawable.ic_delete, "Delete", contentIntent);*/

                builder.flags|= Notification.FLAG_AUTO_CANCEL;

                NotificationManager notificationManager =
                        (NotificationManager) getContext().getSystemService(getContext().NOTIFICATION_SERVICE);
                // Альтернативный вариант
                // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

                notificationManager.notify(0, builder);
                }

        });

        calendar = Calendar.getInstance();

        // Get slidingUpPanelLayout
        slidingUpPanelLayout = (SlidingUpPanelLayout) view.findViewById(R.id.sliding_layout);

        calendarLayout = (LinearLayout) view.findViewById(R.id.calendar_layout);

        weekDayViewPager = (WeekDayViewPager) view.findViewById((R.id.day_view));

        //      Get instance.
        calendarView = ((ExpCalendarView) view.findViewById(R.id.calendar_view));


      /*  new Handler().post(new Runnable() {
            @Override
            public void run() {
                calendarView.setCurrentItem(500);
            }
        });
*/
        //calendarView.setCurrentItem(500);
        YearMonthTv = (TextView) view.findViewById(R.id.YYMM_Tv);
        YearMonthTv.setText(CalendarUtil.getDateString(Calendar.getInstance().get(Calendar.YEAR),(Calendar.getInstance().get(Calendar.MONTH) + 1)));

//      Set up listeners.
        calendarView.setOnDateClickListener(new OnExpDateClickListener()).setOnMonthScrollListener(new OnMonthScrollListener() {
            @Override
            public void onMonthChange(int year, int month) {
                YearMonthTv.setText(CalendarUtil.getDateString(year, month));
            }

            @Override
            public void onMonthScroll(float positionOffset) {
                //Log.i("listener", "onMonthScroll:" + positionOffset);
            }
        });

        calendarView.setOnDateClickListener(new OnDateClickListener() {
            @Override
            public void onDateClick(View view, DateData date) {
                DateData preData = calendarView.getMarkedDates().getAll().get(0);
                //Calendar calendarNext = Calendar.getInstance();
                //DateData dateBuff = new DateData(preData.getYear(),preData.getMonth(),preData.getDay());

                calendar.set(preData.getYear(), preData.getMonth()-1, preData.getDay());

                DayDifference dayDifference = CalendarUtil.calculateDaysBetweenDates(preData, date);

                if (dayDifference.isSwipeRight())
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                else
                    calendar.add(Calendar.DAY_OF_MONTH, -1);

                ((DayAdapter)weekDayViewPager.getAdapter()).setDayClickedDate(date);

                calendarView.getMarkedDates().removeAdd();
                for(int i = 0; i<dayDifference.getDays();i++)
                {
                    ((DayAdapter)weekDayViewPager.getAdapter()).setDayClicked(true);
                    if (dayDifference.isSwipeRight()){
                        calendar.add(Calendar.DAY_OF_MONTH, 1);

                        ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(
                                new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)));

                        weekDayViewPager.setCurrentItem(weekDayViewPager.getCurrentItem()+1);
                    }

                    else {
                        calendar.add(Calendar.DAY_OF_MONTH, -1);

                        ((DayAdapter)weekDayViewPager.getAdapter()).setCurrentDate(
                                new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)));

                        weekDayViewPager.setCurrentItem(weekDayViewPager.getCurrentItem()-1);
                    }

                    //preData = calendarView.getMarkedDates().getAll().get(0);
                    //calendar.set(preData.getYear(), preData.getMonth()-1, preData.getDay());
                }
                if (dayDifference.isSwipeRight())
                    calendar.add(Calendar.DAY_OF_MONTH, -1);
                else
                    calendar.add(Calendar.DAY_OF_MONTH, 1);
                calendarView.markDate(new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DAY_OF_MONTH)));
                CellConfig.selectedDate = calendarView.getMarkedDates().getAll().get(0);
                DateData s = CellConfig.selectedDate;


            }
        });

        slidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
            }
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
                switch (newState){
                    case EXPANDED:
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                        calendarLayout.setLayoutParams(params);

                        CellConfig.Week2MonthPos = CellConfig.middlePosition;
                        CellConfig.ifMonth = true;
                        calendarView.expand();
                        break;
                    case COLLAPSED:
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        calendarLayout.setLayoutParams(params);

                        CellConfig.Month2WeekPos = CellConfig.middlePosition;
                        int pos = CellConfig.middlePosition;
                        CellConfig.ifMonth = false;
                        DateData f = CellConfig.selectedDate;
                        CellConfig.weekAnchorPointDate = CellConfig.selectedDate;
                        YearMonthTv.setText(CalendarUtil.getDateString(CellConfig.selectedDate.getYear(), CellConfig.selectedDate.getMonth()));
                        calendarView.expand();
                        break;
                }
            }
        });
        //imageInit();

        final Calendar calendar = Calendar.getInstance();
        if (CellConfig.selectedDate==null)
            CellConfig.selectedDate = new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));
        calendarView.markDate(CellConfig.selectedDate);

        weekDayViewPager.setDayAdapter(new DayAdapter(getChildFragmentManager()));
        weekDayViewPager.init(calendarView);

        weekDayViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.i("Item", String.valueOf(weekDayViewPager.getCurrentItem()));
                Log.i("Pos", String.valueOf(position));

                if (!((DayAdapter)weekDayViewPager.getAdapter()).isDayClicked()) {
                    DateData date = calendarView.getMarkedDates().getAll().get(0);
                    calendar.set(date.getYear(), date.getMonth()-1, date.getDay());
                    boolean y = ((DayAdapter)weekDayViewPager.getAdapter()).isDayClicked();

                    if (position > weekDayViewPager.LastPage) {
                        calendarView.getMarkedDates().removeAdd();
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        calendarView.markDate(new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
                        calendar.add(Calendar.DAY_OF_MONTH, 1);
                        ((DayAdapter) weekDayViewPager.getAdapter()).setCurrentDate(
                                new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));

                        MonthExpFragment fr = (MonthExpFragment) ((CalendarViewExpAdapter) calendarView.getAdapter()).getCurrentFragment();

                        if (!fr.getMonthViewExpd().getAdapter().listContainItemByDate(calendarView.getMarkedDates().getAll().get(0))) {
                            Log.i("IsContein", "does not contain");
                            calendarView.setCurrentItem(calendarView.getCurrentItem() + 1);
                        }
                    }
                    if (position < weekDayViewPager.LastPage) {
                        calendarView.getMarkedDates().removeAdd();
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        calendarView.markDate(new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));
                        calendar.add(Calendar.DAY_OF_MONTH, -1);
                        ((DayAdapter) weekDayViewPager.getAdapter()).setCurrentDate(
                                new DateData(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH)));

                        MonthExpFragment fr = (MonthExpFragment) ((CalendarViewExpAdapter) calendarView.getAdapter()).getCurrentFragment();

                        if (!fr.getMonthViewExpd().getAdapter().listContainItemByDate(calendarView.getMarkedDates().getAll().get(0))) {
                            Log.i("IsContein", "does not contain");
                            calendarView.setCurrentItem(calendarView.getCurrentItem() - 1);
                        }
                    }
                }
                ((DayAdapter)weekDayViewPager.getAdapter()).setDayClicked(false);
                weekDayViewPager.LastPage = position;

                //Log.i("DateP", date.getMonthString()+ " " + date.getDayString());
//                DateData date2 = calendarView.getMarkedDates().getAll().get(0);
                //Log.i("DateN", date2.getMonthString()+ " " + date2.getDayString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //calendarView.setCurrentItem(500);
        return view;
    }
/*
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeAllViews();
            }
        }
    }
    */

    @Override
    public void onResume() {
        super.onResume();
        if (!(weekDayViewPager.getAdapter() == null)) {

            weekDayViewPager.getAdapter().notifyDataSetChanged();


        }
       // weekDayViewPager.init(calendarView);

        //slidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        //calendarView.expand();
    }

    @Override
    public void onPause() {
        super.onPause();
        ((DayAdapter)weekDayViewPager.getAdapter()).cleanI();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        CellConfig.resetAllDatas();
    }
}
