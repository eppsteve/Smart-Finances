package com.stevesoft.smartfinances.ui.fragments;


import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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

import com.stevesoft.smartfinances.ui.adapters.AccountsCursorAdapter;
import com.stevesoft.smartfinances.R;
import com.stevesoft.smartfinances.ui.activities.MainActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountsFragment extends Fragment {

    ListView listView;
    AccountsCursorAdapter accountsAdapter;

    // Tracks current contextual action mode
    private ActionMode currentActionMode;

    // Tracks current menu item
    private int currentListItemIndex;

    public AccountsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        listView = (ListView) view.findViewById(R.id.listViewAccounts);
        Cursor myCursor = MainActivity.myDb.getAllAccounts();
        accountsAdapter = new AccountsCursorAdapter(getActivity(), myCursor);
        listView.setAdapter(accountsAdapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentActionMode != null) {
                    return true;
                }
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
        accountsAdapter.changeCursor(MainActivity.myDb.getAllAccounts());
        accountsAdapter.notifyDataSetChanged();
        MainActivity.myDb.close();
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

                    // Get selected item
                    View v = listView.getChildAt(currentListItemIndex);

                    // Get id of selected item from TextView in UI
                    TextView txtId = (TextView) v.findViewById(R.id.textViewAccountId);
                    final int id = Integer.parseInt(txtId.getText().toString());

                    // Show confirmation dialog
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete Account")
                            .setMessage("Are you sure? This cannot be undone.")
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {

                                    // Delete account
                                    if (MainActivity.myDb.deleteAccount(id) > 0 ) {
                                        // Switch to new cursor and update contents of ListView
                                        accountsAdapter.changeCursor(MainActivity.myDb.getAllAccounts());
                                        accountsAdapter.notifyDataSetChanged();
                                        MainActivity.myDb.close();
                                        Toast.makeText(getActivity(), "Account deleted!", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Toast.makeText(getActivity(), "Could not delete account", Toast.LENGTH_SHORT).show();
                                    }
                                }})
                            .setNegativeButton(android.R.string.no, null).show();
                    // Action picked, so close the contextual menu
                    mode.finish();
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
