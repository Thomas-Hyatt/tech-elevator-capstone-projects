package com.techelevator.sales_items;

//parent class for our individual item types - this is the class that our item types extends from
public abstract class Product {


    private static int INITIAL_STOCK_VALUE = 5;

    //properties of our snack items
    private double itemPrice;
    private String key;
    private int stock;
    private String name;

    //method to establish an item from the given CSV file - we used this. to assign the value
    // of the paramaters above to the elements of the method
    public Product(String key, String name, double price) {
        this.key = key;
        this.name = name;
        this.itemPrice = price;
        this.stock = INITIAL_STOCK_VALUE;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public String getKey() {
        return key;
    }

    public int getstock() {
        return stock;
    }

    public String getName() {
        return name;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public abstract String getPurchaseMessage();

}
