package com.techelevator.sales_items;

import jdk.swing.interop.SwingInterOpUtils;


        //Candy is extended from the parent class called Product
        //the Product class includes generic properties for all item types
    public class Candy extends Product {


        //method that brings in the item's key, name and price
    public Candy(String key, String name, double price) {

        //call the super parent class and gain access to the superclass constructor parameters
        super(key, name, price);
    }

        //method to display particular message associated with choosing Candy
    @Override
    public String getPurchaseMessage() {
        return "Munch Munch, Yum!";
    }
}
