package com.stevesoft.smartfinances;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

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
        TextView txtDate = (TextView) view.findViewById(R.id.textViewDate);

        // Extract properties from cursor
        int _id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String date = cursor.getString(cursor.getColumnIndexOrThrow("DATE"));
        double price = cursor.getDouble(cursor.getColumnIndexOrThrow("PRICE"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPTION"));

        int category_id = cursor.getInt(cursor.getColumnIndexOrThrow("CATEGORY_ID"));
        int account_id = cursor.getInt(cursor.getColumnIndexOrThrow("ACCOUNT_ID"));

        // Populate fields with extracted properties
        txtDescription.setText(description);
        txtCategory.setText(""+category_id);
        txtPrice.setText(""+price);
        txtDate.setText(date);
    }
}
