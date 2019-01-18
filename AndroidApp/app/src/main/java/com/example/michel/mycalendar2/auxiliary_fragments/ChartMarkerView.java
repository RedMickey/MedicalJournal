package com.example.michel.mycalendar2.auxiliary_fragments;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.michel.mycalendar2.activities.R;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

public class ChartMarkerView extends MarkerView {
    private final TextView tvContent;
    private String measurementValueTypeStr;
    private RelativeLayout chartMarkerLayout;
    private boolean isDisplayed = true;
    private float zeroY;

    public ChartMarkerView(Context context, int layoutResource, String measurementValueTypeStr, float zeroY) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
        chartMarkerLayout = (RelativeLayout) findViewById(R.id.chart_marker_layout);
        this.measurementValueTypeStr = measurementValueTypeStr;
        this.zeroY = zeroY;
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            tvContent.setText(Utils.formatNumber(ce.getHigh(), 1, true));
        } else {
            if (e.getY()!=zeroY){
                tvContent.setText(Utils.formatNumber(e.getY(), 1, true) +
                        " " + measurementValueTypeStr);
                if (!isDisplayed){
                    chartMarkerLayout.setVisibility(VISIBLE);
                    isDisplayed = true;
                }
            }
            else {
                //chartMarkerLayout.setBackground(ContextCompat.getDrawable(this.getContext(), R.color.transparent));
                if (isDisplayed){
                    chartMarkerLayout.setVisibility(GONE);
                    isDisplayed = false;
                }
            }
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
