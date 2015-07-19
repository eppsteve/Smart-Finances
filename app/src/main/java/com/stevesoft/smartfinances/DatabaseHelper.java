package com.stevesoft.smartfinances;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.stevesoft.smartfinances.model.Transaction;

/**
 * Created by steve on 7/18/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "finance.db";


    public static final String CREATE_ACCOUNT_TABLE = "CREATE TABLE ACCOUNT (_id INTEGER PRIMARY KEY, NAME TEXT, AMOUNT REAL, CURRENCY TEXT)";
    public static final String CREATE_CATEGORY_TABLE = "CREATE TABLE CATEGORY (_id INTEGER PRIMARY KEY, NAME TEXT)";
    public static final String CREATE_TRANSACTION_TABLE = "CREATE TABLE TRANSACTIONS (_id INTEGER PRIMARY KEY, DATE TEXT, PRICE REAL, DESCRIPTION TEXT, " +
            "CATEGORY_ID INTEGER, ACCOUNT_ID INTEGER, FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(_id), " +
            "FOREIGN KEY (ACCOUNT_ID) REFERENCES ACCOUNT(_id) )";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ACCOUNT_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
        db.execSQL(CREATE_TRANSACTION_TABLE);

        // insert sample date
        db.execSQL("INSERT INTO ACCOUNT (NAME, AMOUNT, CURRENCY) VALUES ('My Account', 0, 'Euro')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Food')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Transport')");
        db.execSQL("INSERT INTO TRANSACTIONS (DATE, PRICE, DESCRIPTION, CATEGORY_ID, ACCOUNT_ID) VALUES ('Today', 20, 'Super Market', 1, 1)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TRANSACTION");
        db.execSQL("DROP TABLE IF EXISTS ACCOUNT");
        db.execSQL("DROP TABLE IF EXISTS TRANSACTION");
        onCreate(db);
    }

    public boolean insertTransaction(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("DATE", transaction.getDate());
        contentValues.put("PRICE", transaction.getPrice());
        contentValues.put("DESCRIPTION", transaction.getDescription());
        contentValues.put("CATEGORY_ID", transaction.getCategory_id());
        contentValues.put("ACCOUNT_ID", transaction.getAccount_id());
        long result = db.insert("TRANSACTIONS", null, contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }

    public Cursor getAllTransactions(){

        // Get access to the underlying writeable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Query for items from the database and get a cursor back
        Cursor cursor = db.rawQuery("SELECT _id, DATE, PRICE, DESCRIPTION, CATEGORY_ID, ACCOUNT_ID FROM TRANSACTIONS", null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }
}
