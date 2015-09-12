package com.stevesoft.smartfinances.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stevesoft.smartfinances.R;
import com.stevesoft.smartfinances.TransactionsCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment {

    ListView listView;
    TransactionsCursorAdapter transactionsAdapter;

    // Tracks current contextual action mode
    private ActionMode currentActionMode;

    // Tracks current menu item
    private int currentListItemIndex;

    public TransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        listView = (ListView) view.findViewById(R.id.listView);
        Cursor myCursor = MainActivity.myDb.getAllTransactions();
        transactionsAdapter = new TransactionsCursorAdapter(getActivity(), myCursor);
        listView.setAdapter(transactionsAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentActionMode != null) { return true; }
                currentListItemIndex = position;
                currentActionMode = getActivity().startActionMode(modeCallBack);
                view.setSelected(true);
                return true;
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Switch to new cursor and update contents of ListView
        transactionsAdapter.changeCursor(MainActivity.myDb.getAllTransactions());
    }

    // Define the callback when ActionMode is activated
    private ActionMode.Callback modeCallBack = new ActionMode.Callback(){

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Actions");
            getActivity().getMenuInflater().inflate(R.menu.actions_textview, menu);
            return true;
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete:

                    // Get selected row
                    View v = listView.getChildAt(currentListItemIndex);

                    // Get transaction id from TextView in listView
                    TextView txtId = (TextView) v.findViewById(R.id.textViewTransactionId);
                    int id = Integer.parseInt(txtId.getText().toString());

                    // Get type of transaction
                    TextView txtType = (TextView) v.findViewById(R.id.textViewType);
                    String type = txtType.getText().toString();

                    // Get amount of transaction
                    TextView txtAmount = (TextView) v.findViewById(R.id.textViewPrice);
                    double price = Double.parseDouble(txtAmount.getText().toString());

                    // Get account id of transaction
                    TextView txtAccountId = (TextView) v.findViewById(R.id.textViewAccountId);
                    int account_id = Integer.parseInt(txtAccountId.getText().toString());

                    // Delete transaction
                    if (MainActivity.myDb.deleteTransaction(id) > 0 ) {
                        // update listView

                        Toast.makeText(getActivity(), "Transaction deleted!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), "Could not delete transaction", Toast.LENGTH_SHORT).show();
                    }


                    // Determine transaction type and update db
                    String query;
                    switch (type) {
                        case "income":
                            query = "UPDATE ACCOUNT SET AMOUNT = AMOUNT - "+price+" WHERE _id = "+ account_id;
                            MainActivity.myDb.getWritableDatabase().execSQL(query);
                            MainActivity.myDb.close();
                            break;
                        case "expense":
                            // expenses are stored as negative numbers in db
                            query = "UPDATE ACCOUNT SET AMOUNT = AMOUNT - "+price+" WHERE _id = "+ account_id;
                            MainActivity.myDb.getWritableDatabase().execSQL(query);
                            MainActivity.myDb.close();
                            break;
                        case "transfer":
                            // find destination account
                            TextView txtDestination = (TextView) v.findViewById(R.id.textViewToAccount);
                            int dest_account_id = Integer.parseInt(txtDestination.getText().toString());

                            // remove money from destination account
                            query = "UPDATE ACCOUNT SET AMOUNT = AMOUNT - "+price+" WHERE _id = "+ dest_account_id;
                            MainActivity.myDb.getWritableDatabase().execSQL(query);
                            MainActivity.myDb.close();

                            // add money back to source account
                            query = "UPDATE ACCOUNT SET AMOUNT = AMOUNT + "+price+" WHERE _id = "+ account_id;
                            MainActivity.myDb.getWritableDatabase().execSQL(query);
                            MainActivity.myDb.close();
                            break;
                    }

                    mode.finish(); // Action picked, so close the contextual menu
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            currentActionMode = null; // Clear current action mode
        }

        // Called each time the action mode is shown.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;  // Return false if nothing is done
        }
    };

}
