package com.techelevator;

import com.techelevator.sales_items.*;

import java.util.ArrayList;
import java.util.Map;

public class VendingMachine {

    //Created an array of products to hold the fixed number of items from the CSV file
    private Product[] productArray;

    //created final variables for our coin amounts to feed into our dispense change method
    private static final int VALUE_OF_QUARTER_IN_PENNIES = 25;

    private static final int VALUE_OF_DIME_IN_PENNIES = 10;

    private static final int VALUE_OF_NICKEL_IN_PENNIES = 5;

    // default constructor used

    // start the vending machine by constructing the product array
    public void turnOn(ArrayList<String> productMenu, Map<String, String> productNameMap, Map<String, Double> productPriceMap, Map<String, String> productTypeMap) {

        this.productArray = new Product[productMenu.size()];
        int arrayCounter = 0;
        // Constructs Product Array with each Map Key
        for (Map.Entry<String,String> entry : productTypeMap.entrySet()) {
            switch (entry.getValue()) {
                case "Chip":
                    productArray[arrayCounter] = new Chips(entry.getKey(), productNameMap.get(entry.getKey()), productPriceMap.get(entry.getKey()));
                    break;
                case "Candy":
                    productArray[arrayCounter] = new Candy(entry.getKey(), productNameMap.get(entry.getKey()), productPriceMap.get(entry.getKey()));
                    break;
                case "Drink":
                    productArray[arrayCounter] = new Drink(entry.getKey(), productNameMap.get(entry.getKey()), productPriceMap.get(entry.getKey()));
                    break;
                case "Gum":
                    productArray[arrayCounter] = new Gum(entry.getKey(), productNameMap.get(entry.getKey()), productPriceMap.get(entry.getKey()));
                    break;
            }
            arrayCounter++;
        }
    }


    //method for dispensing the desired item from the vending machine
    public void dispenseItem(String key) {
    //created a for each loop to loop through the array to align the dispensed
    // product key with the overall list of items
        for (Product product : productArray) {
            if (product.getKey().equals(key)) {
               //if statement to confirm that the desired item is in stock and available to the customer,
                // else displays a message to say the item is out of stock (out of stock logic is in the user interface class)
                if (product.getstock() > 0) {
                    product.setStock(product.getstock()-1);
                }
            }
        }
    }


    //dispense change method - this method is in place to ensure that the customer can retrieve change if a positive balance is in the vending machine after purchasing items
    // The variables were created within this class as final because the value of quarters, dimes and nickels doesn't change
    public int[] dispenseChange(double changeAmount) {
        int remainingAmountInPennies = (int)(changeAmount*100);
        int numberOfQuarters = (remainingAmountInPennies != 0 ? remainingAmountInPennies/VALUE_OF_QUARTER_IN_PENNIES : 0);
        remainingAmountInPennies -= (remainingAmountInPennies != 0 ? numberOfQuarters*VALUE_OF_QUARTER_IN_PENNIES : 0);
        int numberOfDimes = (remainingAmountInPennies != 0 ? remainingAmountInPennies/VALUE_OF_DIME_IN_PENNIES : 0);
        remainingAmountInPennies -= (remainingAmountInPennies != 0 ? numberOfDimes*VALUE_OF_DIME_IN_PENNIES : 0);
        int numberOfNickels = (remainingAmountInPennies != 0 ? remainingAmountInPennies/VALUE_OF_NICKEL_IN_PENNIES : 0);

        return new int[] {numberOfQuarters, numberOfDimes, numberOfNickels};
    }

// Getters
    // returns the product array
    public Product[] getProductArray() {
        return this.productArray;
    }

    // returns a product from the product array based on the product key
    public Product getProduct(String key) {

        for (Product product: productArray) {
            if (product.getKey().equals(key)) {
                return product;
            }
        }

        return null;
    }



}
