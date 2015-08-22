package com.stevesoft.smartfinances.ui;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.stevesoft.smartfinances.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment {

    LineChart mChart;
    Integer[] xData;
    Float[] yData;

    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        mChart = (LineChart) view.findViewById(R.id.line_chart);
        mChart.setDrawGridBackground(false);

        // get data from db
        Cursor c = MainActivity.myDb.getDailyExpenses();
        int count = c.getCount();
        ArrayList<Integer> date = new ArrayList<Integer>();
        ArrayList<Float> amount = new ArrayList<Float>();

        if (c.moveToFirst()){
            do {
                date.add(c.getInt(c.getColumnIndex("DATE")));
                amount.add(c.getFloat(c.getColumnIndex("PRICE")));
            } while (c.moveToNext());
        }

        //convert arraylists to arrays
        xData = date.toArray(new Integer[date.size()]);
        yData = amount.toArray(new Float[amount.size()]);

        setUpLineChart(count);

        return view;
    }

    private void setUpLineChart(int count){
        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("No data for this chart.");

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines

        //leftAxis.setAxisMaxValue(220f);
        leftAxis.setAxisMinValue(0);
        leftAxis.setStartAtZero(false);
        leftAxis.setShowOnlyMinMax(true);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setEnabled(false);
        //leftAxis.setYOffset(20f);
        //leftAxis.enableGridDashedLine(10f, 10f, 0f);

        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(true);

        mChart.getAxisRight().setEnabled(false);

        // add data
        setData(count);

//        mChart.setVisibleXRange(20);
//        mChart.setVisibleYRange(20f, AxisDependency.LEFT);
//        mChart.centerViewTo(20, 50, AxisDependency.LEFT);

        mChart.animateX(2500, Easing.EasingOption.EaseInOutQuart);
//        mChart.invalidate();

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();
        l.setEnabled(false);

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();
    }

    private void setData(int count){

        // xVals contains the days of the month
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < 32; i++) {
            xVals.add((i) + "");
        }

        // yVals contains the prices
        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++) {
            yVals.add(new Entry( yData[i], xData[i] ));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "Expenses");
        // set1.setFillAlpha(110);
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(true);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);
//        set1.setDrawFilled(true);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        // set data
        mChart.setData(data);
    }


}
