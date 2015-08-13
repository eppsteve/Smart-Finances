package com.stevesoft.smartfinances;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;


import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by steve on 7/19/15.
 */
public class TransactionsCursorAdapter extends CursorAdapter {

    public TransactionsCursorAdapter(Context context, Cursor cursor){
        super(context, cursor, 0);
    }

    // The newView method is used to inflate a new view and return it,
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_row, parent, false);
    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find fields to populate in inflated template
        TextView txtDescription = (TextView) view.findViewById(R.id.textViewDescription);
        TextView txtCategory = (TextView) view.findViewById(R.id.textViewCategory);
        TextView txtPrice = (TextView) view.findViewById(R.id.textViewPrice);
        //TextView txtDate = (TextView) view.findViewById(R.id.textViewDate);
        TextView txtMonth = (TextView) view.findViewById(R.id.textViewMonth);
        TextView txtDay = (TextView) view.findViewById(R.id.textViewDay);
        TextView txtYear = (TextView) view.findViewById(R.id.textViewYear);
        TextView txtAccount = (TextView) view.findViewById(R.id.textViewAccount);
        TextView txtType = (TextView) view.findViewById(R.id.textViewType);
        TextView txtToAccount = (TextView) view.findViewById(R.id.textViewToAccount);

        String to_account = "";

        // Extract properties from cursor
        //int _id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));
        String category_name = cursor.getString(cursor.getColumnIndexOrThrow("CATEGORY_NAME"));
        String account_name = cursor.getString(cursor.getColumnIndexOrThrow("ACCOUNT_NAME"));
        String type = cursor.getString(cursor.getColumnIndexOrThrow("TYPE"));
        if (!cursor.isNull(7)) {
            to_account = cursor.getString(cursor.getColumnIndexOrThrow("TO_ACCOUNT"));
        }

        // Populate fields with extracted properties
        txtDescription.setText(description);
        txtCategory.setText(category_name);
        txtPrice.setText("" + price);
        txtAccount.setText("| " +account_name);
        txtType.setText(type.toLowerCase());

        // populate date textviews
        // the date format is yyyy/mm/dd
        if (date.length() > 1){
            int month = Integer.parseInt(TextUtils.substring(date, 5, 7));
            String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
            Log.e("month", month+"");
            txtMonth.setText(months[month -1]);

            String day = TextUtils.substring(date, 8, 10);
            txtDay.setText(day);
            String year = TextUtils.substring(date, 0, 4);
            txtYear.setText(year);
        }

        Log.e("TYPE", type);
        if (type.equals("INCOME"))
            txtPrice.setTextColor(context.getResources().getColor(R.color.green));
        else if (type.equals("EXPENSE"))
            txtPrice.setTextColor(context.getResources().getColor(R.color.red));
        else if (type.equals("TRANSFER")) {
            txtPrice.setTextColor(context.getResources().getColor(R.color.light_blue));
            txtToAccount.setVisibility(View.VISIBLE);
            txtToAccount.setText("-> " + to_account);
        }
    }
}
