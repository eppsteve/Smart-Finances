package com.stevesoft.smartfinances.ui;


import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
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
                case R.id.menu_edit:
                    Toast.makeText(getActivity(), "Editing!", Toast.LENGTH_SHORT).show();
                    mode.finish(); // Action picked, so close the contextual menu
                    return true;
                case R.id.menu_delete:
                    // Trigger the deletion here

                    // Get transaction id from textview in listView
                    View v = listView.getChildAt(currentListItemIndex);
                    TextView txtId = (TextView) v.findViewById(R.id.textViewTransactionId);
                    int id = Integer.parseInt(txtId.getText().toString());
                    Log.e("PRICE", id+"");

                    // delete transaction
                    if (MainActivity.myDb.deleteTransaction(id) > 0 ) {
                        // update listView

                        Toast.makeText(getActivity(), "Transaction deleted!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getActivity(), "Could not delete transaction", Toast.LENGTH_SHORT).show();
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
