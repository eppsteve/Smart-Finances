package com.stevesoft.smartfinances.ui;


import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.stevesoft.smartfinances.R;
import com.stevesoft.smartfinances.TransactionsCursorAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment {

    ListView listView;
    TransactionsCursorAdapter transactionsAdapter;


    public TransactionsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transactions, container, false);

        listView = (ListView) view.findViewById(R.id.listView);
        Cursor myCursor = (Cursor) MainActivity.myDb.getAllTransactions();
        transactionsAdapter = new TransactionsCursorAdapter(getActivity(), myCursor);
        listView.setAdapter(transactionsAdapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Switch to new cursor and update contents of ListView
        transactionsAdapter.changeCursor((Cursor) MainActivity.myDb.getAllTransactions());
    }
}
