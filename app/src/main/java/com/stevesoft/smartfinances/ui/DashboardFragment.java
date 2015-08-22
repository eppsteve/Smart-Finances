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
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.stevesoft.smartfinances.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment implements OnChartValueSelectedListener{

    private PieChart mChart;    // chart for showing expenses of current month by category
    private Float[] yData;      // used for values of each category
    private String[] xData;     // used for category names

    protected HorizontalBarChart mBarChart;     // chart for showing income/expenses

    private float thisMonthIncome;
    private float thisMonthExpenses;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        // initialize views
        TextView txtThisMonth = (TextView) view.findViewById(R.id.textViewThisMonth);
        TextView txtThisMonthBalance = (TextView) view.findViewById(R.id.textViewThisMonthBalance);
        TextView txtThisMonthIncome = (TextView) view.findViewById(R.id.lbl_income);
        TextView txtThisMonthExpense = (TextView) view.findViewById(R.id.lbl_expense);
        mChart = (PieChart) view.findViewById(R.id.chart);
        mBarChart = (HorizontalBarChart) view.findViewById(R.id.barChart);

        thisMonthExpenses = MainActivity.myDb.getThisMonthExpense();
        thisMonthIncome = MainActivity.myDb.getThisMonthIncome();
        txtThisMonthIncome.setText(thisMonthIncome +" \u20ac");
        txtThisMonthExpense.setText(thisMonthExpenses +" \u20ac");

        // Get current month balance
        Calendar cal= Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        // set month name
        txtThisMonth.setText(txtThisMonth.getText() + month_name);
        // round double to 2 decimal places
        // set month balance
        txtThisMonthBalance.setText(String.format("%.2f", MainActivity.myDb.getThisMonthBalance()) + "EUR");


        // get current month expenses by category from db
        ArrayList<String> categories = new ArrayList<String>();
        ArrayList<Float> amount = new ArrayList<Float>();
        Cursor c = MainActivity.myDb.getThisMonthExpenses();

        if (c.moveToFirst()){
            do{
                categories.add(c.getString(c.getColumnIndex("CATEGORY")));
                amount.add(c.getFloat(c.getColumnIndex("PRICE")));
            } while (c.moveToNext());
        }

        //convert arraylists to arrays
        xData = categories.toArray(new String[categories.size()]);
        yData = amount.toArray(new Float[amount.size()]);

        setUpPieGraph();
        setUpBarGraph();
        return view;
    }

    private void setUpPieGraph(){
        mChart.setUsePercentValues(true);
        mChart.setDescription("");
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(58f);
        mChart.setTransparentCircleRadius(61f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);

        mChart.setCenterText("Expenses\nThis month");

        addData();

        mChart.animateY(1500, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);

        Legend l = mChart.getLegend();
        l.setEnabled(false);

        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display message when value selected
                if (e == null)
                    return;

                Toast.makeText(getActivity(), xData[e.getXIndex()] + " = " + e.getVal() + "EUR",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    // adds data to Pie Chart
    private void addData(){
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        for (int i=0; i<yData.length; i++) {
            yVals1.add(new Entry(yData[i], i));
            Log.e("yDATA: ", yData[i].toString());
        }

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i=0; i<xData.length; i++)
            xVals.add(xData[i]);

        // create pie data set
        PieDataSet dataset = new PieDataSet(yVals1, "Expenses");
        dataset.setSliceSpace(3);
        dataset.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c: ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c: ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c: ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c: ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataset.setColors(colors);


        // instantiate pie data object
        PieData data = new PieData(xVals, dataset);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // update pie chart
        mChart.invalidate();
    }

    private void setUpBarGraph(){
        //mBarChart.setOnChartValueSelectedListener(this);
        // mChart.setHighlightEnabled(false);

        mBarChart.setDrawBarShadow(false);

        mBarChart.setDrawValueAboveBar(true);

        mBarChart.setDescription("");

        // if more than 2 entries are displayed in the chart, no values will be
        // drawn
        mBarChart.setMaxVisibleValueCount(2);

        // scaling can now only be done on x- and y-axis separately
        mBarChart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // mBarChart.setDrawBarShadow(true);

        mBarChart.setDrawGridBackground(false);

        //tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XAxis xl = mBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xl.setTypeface(tf);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);
        xl.setGridLineWidth(0.3f);

        YAxis yl = mBarChart.getAxisLeft();
        //yl.setTypeface(tf);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        //yl.setDrawTopYLabelEntry(false);
        yl.setGridLineWidth(0.3f);
        yl.setEnabled(false);

//        yl.setInverted(true);

        YAxis yr = mBarChart.getAxisRight();
        //yr.setTypeface(tf);
        yr.setDrawAxisLine(false);
        yr.setDrawTopYLabelEntry(true);
        yr.setDrawGridLines(false);
//        yr.setInverted(true);
        yr.setEnabled(false);

        setBarChartData();
        mBarChart.animateY(2500);
        mBarChart.getLegend().setEnabled(false);
    }

    private void setBarChartData(){
        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add(0, "Income");
        xVals.add(1, "Expenses");
        yVals1.add(new BarEntry(thisMonthIncome, 0));
        yVals1.add(new BarEntry(thisMonthExpenses, 1));

        BarDataSet set1 = new BarDataSet(yVals1, "");

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        //data.setValueTypeface(tf);

        int[] colors = {getResources().getColor(R.color.green), getResources().getColor(R.color.red)};
        set1.setColors(colors);
        mBarChart.setData(data);

    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
