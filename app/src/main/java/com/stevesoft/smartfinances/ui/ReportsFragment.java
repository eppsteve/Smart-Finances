package com.stevesoft.smartfinances.ui;


import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.stevesoft.smartfinances.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportsFragment extends Fragment {

    LineChart mChart;
    BarChart mChart2;
    Integer[] xData, xDataMonth;
    Float[] yData, yDataMonthExpense;

    public ReportsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        mChart = (LineChart) view.findViewById(R.id.line_chart);
        mChart2 = (BarChart) view.findViewById(R.id.bar_chart);
        mChart.setDrawGridBackground(false);
        mChart2.setDrawGridBackground(false);

        // LINE CHART //
        // get data from db for line chart
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

        //convert arraylists to arrays for line chart
        xData = date.toArray(new Integer[date.size()]);
        yData = amount.toArray(new Float[amount.size()]);

        setUpLineChart(count);


        // BAR CHART //
        Cursor cur = MainActivity.myDb.getMonthlyExpenses();
        ArrayList<Integer> month = new ArrayList<>();
        ArrayList<Float> month_expense = new ArrayList<>();
        if (cur != null) {
            cur.moveToFirst();
            while (!cur.isAfterLast()) {
                month.add(cur.getInt(0));
                month_expense.add(cur.getFloat(1));
                cur.moveToNext();
            }
        }
        //convert arraylists to arrays for line chart
        xDataMonth = month.toArray(new Integer[date.size()]);
        yDataMonthExpense = month_expense.toArray(new Float[amount.size()]);

        setUpBarChart();

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

    private void setUpBarChart(){

        mChart.setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        // mChart.setDrawXLabels(false);

        mChart.setDrawGridBackground(false);
        // mChart.setDrawYLabels(false);

        //mTf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xAxis.setTypeface(mTf);
        xAxis.setDrawGridLines(false);
        xAxis.setSpaceBetweenLabels(2);

        //ValueFormatter custom = new MyValueFormatter();

        YAxis leftAxis = mChart.getAxisLeft();
        //leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(8, false);
        //leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(15f);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        //rightAxis.setTypeface(mTf);
        rightAxis.setLabelCount(8, false);
        //rightAxis.setValueFormatter(custom);
        rightAxis.setSpaceTop(15f);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(11f);
        l.setXEntrySpace(4f);
        // l.setExtra(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });
        // l.setCustom(ColorTemplate.VORDIPLOM_COLORS, new String[] { "abc",
        // "def", "ghj", "ikl", "mno" });

        setBarChartData(12, 50);

        // mChart.setDrawLegend(false);
    }

    private void setBarChartData(int count, float range) {
        String[] mMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mMonths[i % 12]);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < yDataMonthExpense.length; i++) {
            Log.e("kmkm", xDataMonth[i]+"");
            if (yDataMonthExpense[i] != null)
            yVals1.add(new BarEntry(yDataMonthExpense[i], xDataMonth[i]-1));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "");
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData barData = new BarData(xVals, dataSets);
        // data.setValueFormatter(new MyValueFormatter());
        barData.setValueTextSize(10f);
        //data.setValueTypeface(mTf);

        mChart2.setData(barData);
    }
}
