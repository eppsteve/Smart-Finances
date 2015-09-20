package com.stevesoft.smartfinances.model;

/**
 * Created by steve on 7/18/15.
 *
 */
public class Account {

    private int id;
    private String name;
    private double amount;
    private String currency;

    public Account(int id, String name, double amount, String currency){
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.currency = currency;
    }

    public Account(String name, double amount, String currency){
        this.name = name;
        this.amount = amount;
        this.currency = currency;
    }

    public Account(String name, double amount){
        this.name = name;
        this.amount = amount;

    }

    // setters
    public void setName(){
        this.name = name;
    }

    public void setAmount(){
        this.amount = amount;
    }

    public void setCurrency(){
        this.currency = currency;
    }

    // getters
    public String getName(){
        return this.name;
    }

    public double getAmount(){
        return this.amount;
    }

    public String getCurrency(){
        return this.currency;
    }

}
