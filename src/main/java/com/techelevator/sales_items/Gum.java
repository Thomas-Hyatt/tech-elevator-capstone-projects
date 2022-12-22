package com.techelevator.sales_items;



        //Gum is extended from the parent class called Product
        //the Product class includes generic properties for all item types
    public class Gum extends Product {

        //method that brings in the item's key, name and price
    public Gum(String key, String name, double price) {

        //call the super parent class and gain access to the superclass constructor parameters
        super(key, name, price);
    }

        //method to display particular message associated with choosing gum
    @Override
    public String getPurchaseMessage() {
        return "Chew Chew, Yum!";
    }
}
