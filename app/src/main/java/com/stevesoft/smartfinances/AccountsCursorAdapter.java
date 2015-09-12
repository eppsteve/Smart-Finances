package com.stevesoft.smartfinances;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

/**
 * Created by Steve Alogaris on 7/19/15.
 * steve.alogaris@outlook.com
 */
public class AccountsCursorAdapter extends CursorAdapter {

    public AccountsCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_row_accounts, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtAcountName = (TextView) view.findViewById(R.id.textViewAcountName);
        TextView txtAmount = (TextView) view.findViewById(R.id.textViewAmount);
        TextView txtCurrency = (TextView) view.findViewById(R.id.textViewCurrency);
        TextView txtAccountId = (TextView) view.findViewById(R.id.textViewAccountId);

        int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("NAME"));
        double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("AMOUNT"));
        String currency = cursor.getString(cursor.getColumnIndexOrThrow("CURRENCY"));

        txtAcountName.setText(name);
        txtAmount.setText(""+amount);
        txtCurrency.setText(currency);
        txtAccountId.setText(id +"");
    }
}
