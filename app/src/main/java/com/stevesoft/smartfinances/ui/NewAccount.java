package com.stevesoft.smartfinances.ui;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stevesoft.smartfinances.R;
import com.stevesoft.smartfinances.model.Account;

public class NewAccount extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        setTitle("New Account");
        setUpGUI();
    }

    private void setUpGUI(){
        final TextView txtAccountName = (TextView) findViewById(R.id.txtAccountName);
        final TextView txtInitialBalance = (TextView) findViewById(R.id.txtInitialBalance);
        final Spinner spCurrency = (Spinner) findViewById(R.id.spCurrency);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnSave = (Button) findViewById(R.id.btnSave);

        String currencies[] = {"EUR", "USD", "GBP"};
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_spinner_item, currencies);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spCurrency.setAdapter(spinnerArrayAdapter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = txtAccountName.getText().toString();
                double amount = Double.parseDouble(txtInitialBalance.getText().toString());
                String currency = spCurrency.getSelectedItem().toString();

                Account account = new Account(name, amount, currency);

                if (MainActivity.myDb.insertAccount(account))
                    Toast.makeText(NewAccount.this, "Account Created.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(NewAccount.this, "Failed to create new account.", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_account, menu);
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
