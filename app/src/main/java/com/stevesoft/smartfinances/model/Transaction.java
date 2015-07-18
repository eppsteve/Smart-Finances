package com.stevesoft.smartfinances.model;

/**
 * Created by steve on 7/18/15.
 */
public class Transaction {

    private int id;
    private String date;
    private double price;
    private String description;
    private int category_id;
    private int account_id;

    public Transaction(int id, String date, double price, String description, int category_id, int account_id){
        this.id = id;
        this.date = date;
        this.price = price;
        this.description = description;
        this.category_id = category_id;
        this.account_id = account_id;
    }

    public Transaction(String date, double price, String description, int category_id, int account_id){
        this.date = date;
        this.price = price;
        this.description = description;
        this.category_id = category_id;
        this.account_id = account_id;
    }


    /* setter methods */

    public void setDate(String date){
        this.date = date;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setCategory_id(int category_id){
        this.category_id = category_id;
    }

    public void setAccount_id(int account_id){
        this.account_id = account_id;
    }


    // getter methods
    public String getDate(){
        return this.date;
    }

    public double getPrice(){
        return this.price;
    }

    public String getDescription(){
        return this.description;
    }

    public int getCategory_id(){
        return this.category_id;
    }

    public int getAccount_id(){
        return this.account_id;
    }
}
