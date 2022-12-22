package com.techelevator.sales_items;


//Chips is extended from the parent class called Product
//the Product class includes generic properties for all item types
public class Chips extends Product {


    //method that brings in the item's key, name and price
    public Chips(String key, String name, double price) {

        //call the super parent class and gain access to the superclass constructor parameters
        super(key, name, price);
    }


    //method to display particular message associated with choosing Chips
    @Override
    public String getPurchaseMessage() {
        return "Crunch Crunch, Yum!";
    }


    }

