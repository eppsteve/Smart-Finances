package com.stevesoft.smartfinances.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.stevesoft.smartfinances.R;
import com.stevesoft.smartfinances.model.Transaction;


public class AddTransaction extends ActionBarActivity {

    EditText txtDescription, txtPrice, txtCategory, txtDate, txtAccount;
    Button btnOK;
    //DatabaseHelper myDb;

    String description, date;
    double price;
    int category, account;
    //Transaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);

        txtDescription = (EditText) findViewById(R.id.editDescription);
        txtPrice = (EditText) findViewById(R.id.editPrice);
        txtCategory = (EditText) findViewById(R.id.editCategory);
        txtDate = (EditText) findViewById(R.id.editDate);
        //txtAccount = (EditText) findViewById(R.id.editAccount);
        btnOK = (Button) findViewById(R.id.btnOK);
        final Button btnCancel = (Button) findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        insertTransaction();
    }

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

                Transaction transaction = new Transaction(date, price, description, category, account);

                boolean isInserted = MainActivity.myDb.insertTransaction(transaction);
                if (isInserted) {
                    Toast.makeText(AddTransaction.this, "Transaction inserted.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(AddTransaction.this, "Failed to insert transaction.", Toast.LENGTH_SHORT).show();
                    finish();
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
