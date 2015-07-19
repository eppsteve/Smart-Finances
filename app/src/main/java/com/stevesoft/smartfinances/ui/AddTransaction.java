package com.stevesoft.smartfinances.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.SimpleCursorAdapter;

import com.stevesoft.smartfinances.R;
import com.stevesoft.smartfinances.model.Transaction;

import java.util.Calendar;
import java.util.List;


public class AddTransaction extends ActionBarActivity {

    EditText txtDescription, txtPrice, txtCategory, txtDate, txtAccount;
    Button btnOK, btnPickDate;
    RadioButton rbIncome, rbExpense, rbTransfer;
    Spinner spinner;

    String description, date;
    double price;
    int category, account;

    // used for datepicker
    int _year, _month, _day;
    static final int DIALOG_ID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        final Calendar cal = Calendar.getInstance();
        _year = cal.get(Calendar.YEAR);
        _month = cal.get(Calendar.MONTH);
        _day = cal.get(Calendar.DAY_OF_MONTH);

        setUpGUI();
        insertTransaction();
    }

    private void setUpGUI(){
        txtDescription = (EditText) findViewById(R.id.editDescription);
        txtPrice = (EditText) findViewById(R.id.editPrice);
        txtCategory = (EditText) findViewById(R.id.editCategory);
        txtDate = (EditText) findViewById(R.id.editDate);
        //txtAccount = (EditText) findViewById(R.id.editAccount);
        btnOK = (Button) findViewById(R.id.btnOK);
        rbExpense = (RadioButton) findViewById(R.id.rbExpense);
        rbIncome = (RadioButton) findViewById(R.id.rbIncome);
        rbTransfer = (RadioButton) findViewById(R.id.rbTransfer);

        spinner = (Spinner) findViewById(R.id.spinner);
        Cursor cursor = MainActivity.myDb.getAllCategories();
        // we use a SimpleCursorAdapter because it stores multiple columns,
        // this way we can store the name and the id of each category
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item,
                cursor, new String[] {"NAME"}, new int[] {android.R.id.text1});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        btnPickDate = (Button) findViewById(R.id.btnPickDate);
        btnPickDate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                showDialog(DIALOG_ID);
            }
        });

        final Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //int pos = parent.getItemAtPosition(position);
                txtCategory.setText("" + id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
            _month = monthOfYear;
            _day = dayOfMonth +1;

            txtDate.setText(_day +"/"+ _month +"/"+ _year);
        }
    };

    public void insertTransaction(){
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                date = txtDate.getText().toString();
                description = txtDescription.getText().toString();
                price = Double.parseDouble(txtPrice.getText().toString());
                category = Integer.parseInt(txtCategory.getText().toString());
                //account = Integer.parseInt(txtAccount.getText().toString());
                account = 1;

                if (rbExpense.isChecked()) {
                    Transaction transaction = new Transaction(date, price, description, category, account);

                    boolean isInserted = MainActivity.myDb.insertExpense(transaction);
                    if (isInserted) {
                        Toast.makeText(AddTransaction.this, "Transaction inserted.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddTransaction.this, "Failed to insert transaction.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                } else if (rbIncome.isChecked()){
                    Transaction transaction = new Transaction(date, price, description, category, account);
                    boolean isInserted = MainActivity.myDb.insertIncome(transaction);
                    if (isInserted) {
                        Toast.makeText(AddTransaction.this, "Transaction inserted.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(AddTransaction.this, "Failed to insert transaction.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_transaction, menu);
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
}
