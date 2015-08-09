package com.stevesoft.smartfinances.ui;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.stevesoft.smartfinances.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private PieChart mChart;
    private Float[] yData; // = {20, 50};
    private String[] xData; // = {"Sony", "Hawai", "LG", "Apple", "Samsung"};

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mChart = (PieChart) view.findViewById(R.id.chart);

        // get current month expenses by category from db
        ArrayList<String> categories = new ArrayList<String>();
        ArrayList<Float> amount = new ArrayList<Float>();
        Cursor c = MainActivity.myDb.getThisMonthExpenses();
        c.moveToFirst();
        while (!c.isAfterLast()){
            // add cursor data to arraylists
            categories.add(c.getString(c.getColumnIndex("CATEGORY")));
            amount.add(c.getFloat(c.getColumnIndex("PRICE")));
            c.moveToNext();
        }

        //convert arraylists to arrays
        xData = categories.toArray(new String[categories.size()]);
        yData = amount.toArray(new Float[amount.size()]);

        setUpGraph();


        return view;
    }

    private void setUpGraph(){
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
        PieDataSet dataset = new PieDataSet(yVals1, "Market Share");
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

        for (int c: ColorTemplate.PASTEL_COLORS)
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


}
