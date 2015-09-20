package com.stevesoft.smartfinances.ui.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.stevesoft.smartfinances.R;
import com.stevesoft.smartfinances.model.Account;

public class NewAccountActivity extends AppCompatActivity {

    private static final String SETTINGS = "smartfinances_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("New Account");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setUpGUI();
    }

    private void setUpGUI(){
        final TextView txtAccountName = (TextView) findViewById(R.id.txtAccountName);
        final TextView txtInitialBalance = (TextView) findViewById(R.id.txtInitialBalance);
//      final Spinner spCurrency = (Spinner) findViewById(R.id.spCurrency);
        Button btnCancel = (Button) findViewById(R.id.btnCancel);
        Button btnSave = (Button) findViewById(R.id.btnSave);

//        String currencies[] = {"EUR", "USD", "GBP"};
//        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, currencies);
//        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
//        spCurrency.setAdapter(spinnerArrayAdapter);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = txtAccountName.getText().toString();
                double amount = Double.parseDouble(txtInitialBalance.getText().toString());
//              String currency = spCurrency.getSelectedItem().toString();

                //Get currency from SharedPreferences
                SharedPreferences prefs = getSharedPreferences(SETTINGS, MODE_PRIVATE);
                String currency = prefs.getString("CURRENCY", "EUR");
                Account account = new Account(name, amount, currency);

                if (MainActivity.myDb.insertAccount(account))
                    Toast.makeText(NewAccountActivity.this, "Account Created.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(NewAccountActivity.this, "Failed to create new account.", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

}
