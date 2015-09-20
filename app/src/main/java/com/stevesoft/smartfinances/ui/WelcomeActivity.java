package com.stevesoft.smartfinances.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.stevesoft.smartfinances.R;
import com.stevesoft.smartfinances.model.Account;

public class WelcomeActivity extends AppCompatActivity {

    private static final String SETTINGS = "smartfinances_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final Spinner spCurrency = (Spinner) findViewById(R.id.welcome_spinner_currency);
        final Button btn = (Button) findViewById(R.id.dummy_button);
        final EditText txtInitial_balance = (EditText) findViewById(R.id.welcome_initial_balance);
        final EditText txtAccount_name = (EditText) findViewById(R.id.welcome_editText_account_name);

        String currencies[] = {"EUR", "USD", "GBP", "ARS", "AUD", "BGN", "BRL", "CAD", "CHE",
                "COP", "CZK", "DKK", "HKD", "HUF", "INR", "KRW", "NOK", "PLN", "RON", "RUB", "SEK", };
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getBaseContext(), android.R.layout.simple_spinner_item, currencies);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spCurrency.setAdapter(spinnerArrayAdapter);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // save new account to db
                String currency = spCurrency.getSelectedItem().toString();
                double balance = Double.parseDouble(txtInitial_balance.getText().toString());
                String name = txtAccount_name.getText().toString();
                Account account = new Account(name, balance, currency);

                if (MainActivity.myDb.insertAccount(account))
                    Toast.makeText(getBaseContext(), "Account Created.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "Failed to create new account.", Toast.LENGTH_SHORT).show();

                // Get shared preferences
                SharedPreferences prefs = getSharedPreferences(SETTINGS, MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                // do not show welcome activity again
                edit.putBoolean(getString(R.string.pref_previously_started), Boolean.TRUE);
                // Save currency
                edit.putString("CURRENCY", currency);
                edit.apply();

                // start main activity
                startActivity(new Intent(getBaseContext(), MainActivity.class));
                finish();
            }
        });
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
