package com.stevesoft.smartfinances.ui;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    private PieChart mChart;    // chart for showing expenses of current month
    private Float[] yData;      // used for values of each category
    private String[] xData;     // used for category names

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        TextView txtNetWorth = (TextView) view.findViewById(R.id.textViewNetWorth);
        TextView txtThisMonth = (TextView) view.findViewById(R.id.textViewThisMonth);
        TextView txtThisMonthBalance = (TextView) view.findViewById(R.id.textViewThisMonthBalance);
        mChart = (PieChart) view.findViewById(R.id.chart);

        // Get current month
        Calendar cal= Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
        String month_name = month_date.format(cal.getTime());
        txtThisMonth.setText(month_name);   // set month name
        txtThisMonthBalance.setText(MainActivity.myDb.getThisMonthBalance() +""); // set month balance

        // display net worth to textview
        Cursor cur = MainActivity.myDb.getNetWorth();
        if (cur.moveToFirst()){
            do {
                txtNetWorth.setText(txtNetWorth.getText() +""+
                        cur.getDouble(cur.getColumnIndex("BALANCE")) +" "
                        + cur.getString(cur.getColumnIndex("CURRENCY"))
                        + "\n");
            } while (cur.moveToNext());
        }

        // get current month expenses by category from db
        ArrayList<String> categories = new ArrayList<String>();
        ArrayList<Float> amount = new ArrayList<Float>();
        Cursor c = MainActivity.myDb.getThisMonthExpenses();

        if (c.moveToFirst()){
            do{
                categories.add(c.getString(c.getColumnIndex("CATEGORY")));
                Log.e("CATEGORY_added:", c.getString(c.getColumnIndex("CATEGORY")));
                amount.add(c.getFloat(c.getColumnIndex("PRICE")));
                Log.e("PRICE_added:", "" + c.getFloat(c.getColumnIndex("PRICE")));
            } while (c.moveToNext());
        }


//        c.moveToFirst();

//        while (!c.isAfterLast()){
//            // add cursor data to arraylists
//            categories.add(c.getString(c.getColumnIndex("CATEGORY")));
//            //Log.e("CATEGORY_added:", c.getString(c.getColumnIndex("CATEGORY")));
//            amount.add(c.getFloat(c.getColumnIndex("PRICE")));
//            //Log.e("PRICE_added:", ""+c.getFloat(c.getColumnIndex("PRICE")));
//            c.moveToNext();
//        }

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
