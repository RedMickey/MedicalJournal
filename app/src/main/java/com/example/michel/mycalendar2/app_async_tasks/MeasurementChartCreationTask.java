package com.example.michel.mycalendar2.app_async_tasks;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;

import com.example.michel.mycalendar2.activities.MeasurementChartActivity;
import com.example.michel.mycalendar2.activities.R;
import com.example.michel.mycalendar2.auxiliary_fragments.ChartMarkerView;
import com.example.michel.mycalendar2.calendarview.adapters.DatabaseAdapter;
import com.example.michel.mycalendar2.calendarview.utils.CalendarUtil;
import com.example.michel.mycalendar2.utils.DBStaticEntries;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MeasurementChartCreationTask extends AsyncTask<Integer, Void, List<float[]>> {
    private MeasurementChartActivity view;
    private int dateMonth;
    private int dateYear;
    private int idMeasurementType;
    private String reminderTimeStr;
    private String measurementValueTypeStr;

    public MeasurementChartCreationTask(MeasurementChartActivity mca, int idMeasurementType,
                                        String measurementValueTypeStr, String reminderTimeStr){
        super();
        this.view = mca;
        this.idMeasurementType = idMeasurementType;
        this.measurementValueTypeStr = measurementValueTypeStr;
        this.reminderTimeStr = reminderTimeStr;
    }

    @Override
    protected List<float[]> doInBackground(Integer... integers) {
        DatabaseAdapter databaseAdapter = new DatabaseAdapter();
        databaseAdapter.open();
        List<float[]> measurementReminderEntryValues = databaseAdapter.getMeasurementReminderEntriesPerMonth(integers[0], integers[1]);
        databaseAdapter.close();
        dateMonth = integers[1];
        dateYear = integers[2];
        return measurementReminderEntryValues;
    }

    @Override
    protected void onPostExecute(List<float[]> measurementData) {
        LineChart chart = (LineChart) view.findViewById(R.id.measurement_chart);
        Calendar cal = Calendar.getInstance();
        //cal.set(Calendar.YEAR, dateYear);
        //cal.set(Calendar.MONTH, dateMonth);
        int[] daysOfMonth = new int[CalendarUtil.getDaysInMonth(dateMonth-1, dateYear)];
        List<Entry> zeroEntries = new ArrayList<Entry>();
        List<Entry> dataEntries1 = new ArrayList<Entry>();
        List<Entry> dataEntries2 = new ArrayList<Entry>();

        for(int i = 0; i<daysOfMonth.length;i++){
            daysOfMonth[i] = i+1;
            zeroEntries.add(new Entry(daysOfMonth[i], 30));
        }

        /*for (float[] dayStatData: measurementData) {
            dataEntries1.add(new Entry(dayStatData[3], dayStatData[1]));
        }*/

        String labelStr1 = "";
        String labelStr2 = "";
        switch (idMeasurementType){
            case 1:
                labelStr1 = DBStaticEntries.getMeasurementTypeById(idMeasurementType).getName() + "  ";
                for (float[] dayStatData: measurementData) {
                    dataEntries1.add(new Entry(dayStatData[3], dayStatData[1]));
                }
                break;
            case 2:
                for (float[] dayStatData: measurementData) {
                    dataEntries1.add(new Entry(dayStatData[3], dayStatData[2]));
                    dataEntries2.add(new Entry(dayStatData[3], dayStatData[1]));
                }
                labelStr1 = "Верхнее давление  ";
                labelStr2 = "Нижнее давление  ";
                break;
        }


        LineDataSet zeroDataSet = new LineDataSet(zeroEntries, ""); // add entries to dataset
        zeroDataSet.setLineWidth(2.5f);
        zeroDataSet.setCircleRadius(3);
        zeroDataSet.setColors(new int[]{R.color.transparent}, view);
        zeroDataSet.setCircleColors(new int[]{R.color.transparent}, view);
        zeroDataSet.setCircleColorHole(ContextCompat.getColor(view, R.color.transparent));
        zeroDataSet.setDrawValues(false);
        zeroDataSet.setDrawHighlightIndicators(false);

        LineDataSet dataSet1 = new LineDataSet(dataEntries1, labelStr1); // add entries to dataset
        dataSet1.setLineWidth(2.5f);
        dataSet1.setCircleRadius(3);
        dataSet1.setDrawValues(false);

        LineDataSet dataSet2 = new LineDataSet(dataEntries2, labelStr2); // add entries to dataset
        dataSet2.setLineWidth(2.5f);
        dataSet2.setCircleRadius(3);
        dataSet2.setColors(new int[]{R.color.material_green_200}, view);
        dataSet2.setCircleColors(new int[]{R.color.material_green_200}, view);
        dataSet2.setDrawValues(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet1);
        if (dataEntries2.size() != 0)
            dataSets.add(dataSet2);
        dataSets.add(zeroDataSet);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setScaleEnabled(false);

        // no description text
        chart.getDescription().setEnabled(false);

        chart.getAxisLeft().setDrawGridLines(false);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);

        chart.getAxisRight().setDrawAxisLine(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisLeft().setDrawAxisLine(false);
        chart.animateX(1200, Easing.EasingOption.Linear);

        Legend l = chart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(14f);
        l.setTextColor(Color.BLACK);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        LineData lineData = new LineData(dataSets);
        //lineData.setValueFormatter(new MyYAxisValueFormatter());

        ChartMarkerView cmv = new ChartMarkerView(view, R.layout.chart_marker_view, measurementValueTypeStr);

        // Set the marker to the chart
        cmv.setChartView(chart);
        chart.setMarker(cmv);

        chart.setData(lineData);
        chart.invalidate(); // refresh

    }
}
