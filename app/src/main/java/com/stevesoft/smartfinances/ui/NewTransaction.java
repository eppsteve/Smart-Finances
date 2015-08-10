package com.stevesoft.smartfinances.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stevesoft.smartfinances.R;
import com.stevesoft.smartfinances.model.Transaction;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewTransaction extends ActionBarActivity {

    // used for datepicker
    int _year, _month, _day;
    static final int DIALOG_ID = 0;

    EditText txtDate, txtPrice, txtDescription;
    TextView lblWithdraw, lblToAccount;
    RadioButton rbExpense, rbIncome, rbTransfer;
    Spinner spCategory, spWithdraw, spToAccount;

    // id of selected category
    int category_id;

    // id of selected account
    int account_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_transaction);

        final Calendar cal = Calendar.getInstance();
        _year = cal.get(Calendar.YEAR);
        _month = cal.get(Calendar.MONTH);
        _day = cal.get(Calendar.DAY_OF_MONTH);

        setUpGUI();
        setTitle("New Transaction");
    }

    private void setUpGUI(){
        lblWithdraw = (TextView) findViewById(R.id.lblWithdraw);
        lblToAccount = (TextView) findViewById(R.id.lblToAccount);
        txtDescription = (EditText) findViewById(R.id.txtDescription);
        txtPrice = (EditText) findViewById(R.id.txtPrice);
        txtDate = (EditText) findViewById(R.id.txtDate);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        rbExpense = (RadioButton) findViewById(R.id.rbExpense);
        rbIncome = (RadioButton) findViewById(R.id.rbIncome);
        rbTransfer = (RadioButton) findViewById(R.id.rbTransfer);
        spCategory = (Spinner) findViewById(R.id.spinnerCategory);
        spWithdraw = (Spinner) findViewById(R.id.spinnerWithdraw);
        spToAccount = (Spinner) findViewById(R.id.spinnerToAccount);

        // get all categories from database
        Cursor cursor = MainActivity.myDb.getAllCategories();
        // simple cursor adapter allows us to store both the name and the id of each category
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,
                cursor, new String[] {"NAME"}, new int[] {android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //bind adapter to spinner
        spCategory.setAdapter(adapter);

        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //int pos = parent.getItemAtPosition(position);
                category_id = Integer.parseInt("" + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // populate account spinners with all accounts from the database
        Cursor acCursor = MainActivity.myDb.getAllAccounts();
        SimpleCursorAdapter acAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,
                acCursor, new String[] {"NAME"}, new int[] {android.R.id.text1});
        acAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWithdraw.setAdapter(acAdapter);
        spToAccount.setAdapter(acAdapter);

        spWithdraw.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //int pos = parent.getItemAtPosition(position);
                account_id = Integer.parseInt("" + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showDialog(DIALOG_ID);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertTransaction();
            }
        });

        rbIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblWithdraw.setText("Deposit To");
                // hide 'ToAccount' views
                lblToAccount.setVisibility(View.GONE);
                spToAccount.setVisibility(View.GONE);
            }
        });

        rbExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblWithdraw.setText("Widraw From");
                // hide 'ToAccount' views
                lblToAccount.setVisibility(View.GONE);
                spToAccount.setVisibility(View.GONE);
            }
        });

        rbTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lblWithdraw.setText("From Account");
                lblToAccount.setVisibility(View.VISIBLE);
                spToAccount.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        if (id== DIALOG_ID)
            return new DatePickerDialog(this, dpickerListener, _year, _month, _day);
        return null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            _year = year;
            _month = monthOfYear +1;
            _day = dayOfMonth;

            // format month and day integers with leading zero digits
            txtDate.setText(_year +"-"+ String.format("%02d", _month) +"-"+ String.format("%02d", _day));
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertTransaction(){
        String tr_date = txtDate.getText().toString();

        String tr_description = txtDescription.getText().toString();
        double tr_price = Double.parseDouble(txtPrice.getText().toString());
        //category = Integer.parseInt(txtCategory.getText().toString());
        //account = Integer.parseInt(txtAccount.getText().toString());

        if (rbExpense.isChecked()) {
            tr_price = 0 - tr_price;
            Transaction transaction = new Transaction(tr_date, tr_price, tr_description, category_id, account_id);

            boolean isInserted = MainActivity.myDb.insertTransaction(transaction);
            if (isInserted) {
                Toast.makeText(NewTransaction.this, "Transaction inserted.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(NewTransaction.this, "Failed to insert transaction.", Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (rbIncome.isChecked()){
            Transaction transaction = new Transaction(tr_date, tr_price, tr_description, category_id, account_id);
            boolean isInserted = MainActivity.myDb.insertTransaction(transaction);
            if (isInserted) {
                Toast.makeText(NewTransaction.this, "Transaction inserted.", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(NewTransaction.this, "Failed to insert transaction.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
