package com.stevesoft.smartfinances.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.stevesoft.smartfinances.AccountsCursorAdapter;
import com.stevesoft.smartfinances.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccountsFragment extends Fragment {

    ListView listView;
    AccountsCursorAdapter accountsAdapter;

    public AccountsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accounts, container, false);

        listView = (ListView) view.findViewById(R.id.listViewAccounts);
        Cursor myCursor = (Cursor) MainActivity.myDb.getAllAccounts();
        accountsAdapter = new AccountsCursorAdapter(getActivity(), myCursor);
        listView.setAdapter(accountsAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Switch to new cursor and update contents of ListView
        accountsAdapter.changeCursor((Cursor) MainActivity.myDb.getAllAccounts());
    }
}
