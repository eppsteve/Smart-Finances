package com.stevesoft.smartfinances;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

import com.stevesoft.smartfinances.model.Account;
import com.stevesoft.smartfinances.model.Transaction;

/**
 * Created by steve on 7/18/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "finance.db";


    public static final String CREATE_ACCOUNT_TABLE = "CREATE TABLE ACCOUNT (_id INTEGER PRIMARY KEY, NAME TEXT, AMOUNT REAL, CURRENCY TEXT)";
    public static final String CREATE_CATEGORY_TABLE = "CREATE TABLE CATEGORY (_id INTEGER PRIMARY KEY, NAME TEXT)";
    public static final String CREATE_TRANSACTION_TABLE = "CREATE TABLE TRANSACTIONS (_id INTEGER PRIMARY KEY, DATE TEXT, PRICE REAL, DESCRIPTION TEXT, " +
            "CATEGORY_ID INTEGER, ACCOUNT_ID INTEGER NOT NULL, "+
            "TYPE STRING CHECK (TYPE IN ('INCOME','EXPENSE', 'TRANSFER') ) NOT NULL DEFAULT ('EXPENSE'), "+
            "TO_ACCOUNT INT REFERENCES ACCOUNT (_id), "+
            "FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY(_id), " +
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
        //db.execSQL("INSERT INTO ACCOUNT (NAME, AMOUNT, CURRENCY) VALUES ('My Account', 5000, 'EUR')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Food')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Transport')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Clothing')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Entertainment')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Household')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Bills')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Healthcare')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Other Expenses')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Income')");
        db.execSQL("INSERT INTO CATEGORY (NAME) VALUES ('Transfer')");
        //db.execSQL("INSERT INTO TRANSACTIONS (DATE, PRICE, DESCRIPTION, CATEGORY_ID, ACCOUNT_ID, TYPE) VALUES ('2015-08-01', -24, 'Super Market', 1, 1, 'EXPENSE')");
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
        contentValues.put("TYPE", transaction.getType());

        contentValues.put("TO_ACCOUNT", transaction.getDestinationAccount());

        long result = db.insert("TRANSACTIONS", null, contentValues);
        //db.execSQL("INSERT INTO TRANSACTIONS (DATE, PRICE, DESCRIPTION, CATEGORY_ID, ACCOUNT_ID) VALUES ('"+transaction.getDate() +"', "+transaction.getPrice()+", '"+transaction.getDescription()+"', "+transaction.getCategory_id()+", "+transaction.getAccount_id()+")");

        // Update account's balance
        String query = "UPDATE ACCOUNT SET AMOUNT = AMOUNT + "+transaction.getPrice()+" WHERE _id = "+ transaction.getAccount_id();
        db.execSQL(query);


        if (result==-1)
            return false;
        else
            return true;
    }


    public boolean transferFunds(Transaction transaction){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("DATE", transaction.getDate());
        contentValues.put("PRICE", transaction.getPrice());
        contentValues.put("DESCRIPTION", transaction.getDescription());
        contentValues.put("CATEGORY_ID", transaction.getCategory_id());
        contentValues.put("ACCOUNT_ID", transaction.getAccount_id());
        contentValues.put("TYPE", transaction.getType());
        contentValues.put("TO_ACCOUNT", transaction.getDestinationAccount());

        // Execute query
        long result1 = db.insert("TRANSACTIONS", null, contentValues);

        // Update source account balance (SELECT ACCOUNT_ID FROM TRANSACTIONS ORDER BY _id DESC LIMIT 1)
        String query1 = "UPDATE ACCOUNT SET AMOUNT = AMOUNT - "+transaction.getPrice()+" WHERE _id = "+ transaction.getAccount_id();
        db.execSQL(query1);

        // Update destination account balance
        String query2 = "UPDATE ACCOUNT SET AMOUNT = AMOUNT + "+transaction.getPrice()+" WHERE _id = "+ transaction.getDestinationAccount();
        db.execSQL(query2);

        if (result1 ==-1)
            return false;
        else
            return true;
    }

    public boolean insertAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", account.getName());
        contentValues.put("AMOUNT", account.getAmount());
        contentValues.put("CURRENCY", account.getCurrency());
        long result = db.insert("ACCOUNT", null, contentValues);

        if (result==-1)
            return false;
        else
            return true;
    }

    public int deleteTransaction(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("TRANSACTIONS", "_id = " +id, null);
    }


    public Cursor getAllTransactions(){

        // Get access to the underlying writeable database
        SQLiteDatabase db = this.getWritableDatabase();

        // Query for items from the database and get a cursor back
        Cursor cursor = db.rawQuery("SELECT " +
                "TRANSACTIONS._id, TRANSACTIONS.DATE, TRANSACTIONS.PRICE, TRANSACTIONS.DESCRIPTION, " +
                "CATEGORY.NAME AS CATEGORY_NAME, TRANSACTIONS.TYPE, ACCOUNT.NAME AS ACCOUNT_NAME, TRANSACTIONS.TO_ACCOUNT " +
                "FROM TRANSACTIONS " +
                "INNER JOIN CATEGORY ON CATEGORY._ID = TRANSACTIONS.CATEGORY_ID " +
                "INNER JOIN ACCOUNT ON TRANSACTIONS.ACCOUNT_ID = ACCOUNT._id " +
                "ORDER BY DATE DESC ", null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }


    public Cursor getAllAccounts(){
        // Get access to the underlying writeable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Query for items from the database and get a cursor back
        Cursor cursor = db.rawQuery("SELECT * FROM ACCOUNT", null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public Cursor getAllCategories(){
        // Get access to the underlying writeable database
        SQLiteDatabase db = this.getWritableDatabase();
        // Query for items from the database and get a cursor back
        Cursor cursor = db.rawQuery("SELECT * FROM CATEGORY", null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    // gets all the expenses of the current month BY CATEGORY
    public Cursor getThisMonthExpenses(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT TRANSACTIONS.DATE, SUM(PRICE*(-1)) AS PRICE, CATEGORY.NAME AS CATEGORY " +
                "FROM TRANSACTIONS " +
                "JOIN CATEGORY ON TRANSACTIONS.CATEGORY_id = CATEGORY._id " +
                "WHERE (DATE BETWEEN date('now','start of month') AND date('now','start of month', '+1 months', '-1 day')) " +
                "AND TRANSACTIONS.TYPE = 'EXPENSE' " +
                "GROUP BY CATEGORY.NAME", null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    // returns the balance of the current month
    public double getThisMonthBalance() {
        double balance = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(PRICE) AS BALANCE, DATE " +
                "FROM TRANSACTIONS " +
                "WHERE TRANSACTIONS.TYPE NOT IN ('TRANSFER') AND "+
                "( DATE BETWEEN date('now','start of month') AND date('now','start of month', '+1 months', '-1 day') )", null);
        if (cursor != null) {
            cursor.moveToFirst();
            balance = cursor.getFloat(0);
        }
        return balance;
    }

    public float getThisMonthIncome(){
        float income = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(PRICE) AS BALANCE " +
                "FROM TRANSACTIONS  " +
                "WHERE TRANSACTIONS.TYPE = 'INCOME' "+
                "AND (DATE BETWEEN date('now','start of month') AND date('now','start of month', '+1 months', '-1 day') ) " +
                "    AND PRICE > 0", null);
        if (cursor != null) {
            cursor.moveToFirst();
            income = cursor.getFloat(0);
        }
        return income;
    }

    // returns the sum of expenses for the current month
    public float getThisMonthExpense(){
        float expense = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(PRICE) AS BALANCE " +
                "FROM TRANSACTIONS  " +
                "WHERE TRANSACTIONS.TYPE = 'EXPENSE' AND "+
                "(DATE BETWEEN date('now','start of month') AND date('now','start of month', '+1 months', '-1 day') ) " +
                "    AND PRICE < 0", null);
        if (cursor != null) {
            cursor.moveToFirst();
            expense = cursor.getFloat(0) * (-1);
        }
        return expense;
    }

    public Cursor getDailyExpenses(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUBSTR(DATE,9,10) AS DATE, SUM(PRICE*(-1)) AS PRICE " +
                "FROM TRANSACTIONS " +
                "WHERE TYPE = 'EXPENSE' AND (DATE BETWEEN date('now', 'start of month') " +
                "AND date('now','start of month', '+1 months', '-1 day')) " +
                "GROUP BY DATE "+
                "ORDER BY DATE ", null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }

    public Cursor getMonthlyExpenses(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUBSTR(DATE,7,2) as MONTH, SUM(PRICE*(-1)) AS PRICE " +
                "FROM TRANSACTIONS  " +
                "WHERE TYPE = 'EXPENSE' AND (DATE BETWEEN date('now', 'start of year') AND " +
                "                           date('now','start of year', '+1 year', '-1 day'))  " +
                "GROUP BY MONTH   " +
                "ORDER BY DATE ", null);
        if (cursor != null)
            cursor.moveToFirst();
        return cursor;
    }
}
