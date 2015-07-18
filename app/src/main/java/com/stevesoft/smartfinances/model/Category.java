package com.stevesoft.smartfinances.model;

/**
 * Created by steve on 7/18/15.
 */
public class Category {

    private int id;
    private String name;

    public Category(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Category(String name){
        this.name = name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }
}
