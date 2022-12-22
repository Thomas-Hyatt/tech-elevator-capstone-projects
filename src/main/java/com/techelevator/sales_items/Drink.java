package com.techelevator.sales_items;

public class Drink extends Product{


    //Drink is extended from the parent class called Product
    //the Product class includes generic properties for all item types

    //method that brings in the item's key, name and price
    public Drink(String key, String name, double price) {

    //call the super parent class and gain access to the superclass constructor parameters
        super(key, name, price);
    }


    //method to display particular message associated with choosing a drink
    @Override
    public String getPurchaseMessage() {
        return "Glug Glug, Yum!";
    }
}
