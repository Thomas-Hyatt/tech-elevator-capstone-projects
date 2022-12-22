package com.techelevator;

import com.techelevator.sales_items.Product;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class UserInterface {

    //// Menus
    private final Scanner userInput = new Scanner(System.in);
    private final DecimalFormat df = new DecimalFormat("0.00");

    // Main Menu
    public int displayMainMenu() {
        System.out.println();
        System.out.println("(1) Display Vending Machine Items");
        System.out.println("(2) Purchase");
        System.out.println("(3) Exit");
        System.out.println();

        System.out.print("Please enter a number above to proceed: ");
        return Integer.parseInt(userInput.nextLine());
    }

    // Purchase Menu
    public int displayPurchaseMenu(double currentBalance) {
        System.out.println();
        System.out.println("Current Money Provided: $" + df.format(currentBalance));
        System.out.println();
        System.out.println("(1) Feed Money");
        System.out.println("(2) Select Product");
        System.out.println("(3) Finish Transaction");
        System.out.println();
        System.out.print("Please enter a number above to proceed: ");
        return Integer.parseInt(userInput.nextLine());
    }

    // Product Menu
    public void displayProductMenu(ArrayList<String> productMenu, Product[] productArray) {
        System.out.println();
        for (String productInfoLine: productMenu) {
            int productStock = -1;
            String productStockString = "PRODUCT NOT FOUND!";
            String productKey = productInfoLine.split("\\|")[0];
            for (Product product: productArray) {
                if (product.getKey().equals(productKey)) {
                    productStock = product.getstock();
                    break;
                }
            }
            if (productStock > -1) {
                productStockString = String.valueOf(productStock);
            }
            System.out.println(productInfoLine + "|" + "Stock:" + productStockString);
        }
        System.out.println();
    }

    //// Messages

    // Startup Messages
    public void displayStartupMessage() {
        System.out.println();
        System.out.println("Welcome to Vendo-Matic 800! Happy Shopping!");
    }

    // Purchase Messages
    public String displaySelectProductMessage() {
        System.out.println();
        System.out.print("Please enter the key for the item you'd like to purchase: ");
        return userInput.nextLine();
    }

    public void displayOutOfStockMessage() {
        System.out.println();
        System.out.println("This item is out of stock!");
    }

    public void displayInsufficientBalanceMessage() {
        System.out.println();
        System.out.println("INSUFFICIENT BALANCE!");
    }

    public void displayPurchaseMessage(Product product) {
        System.out.println();
        System.out.println(product.getPurchaseMessage());
    }

    // Deposit Messages
    public int displayFeedMoneyMessage(double currentBalance) {
        System.out.println();
        System.out.println("Your current balance is: $" + df.format(currentBalance));
        System.out.print("Please enter the amount(whole dollar) of money you wish to deposit: $");
        try {
            return Integer.parseInt(userInput.nextLine());
        } catch(Exception invalidInputException) {
            return -1;
        }
    }

    public void displayInvalidInputMessage() {
        System.out.println();
        System.out.println("INVALID INPUT!");
    }

    public void displayDepositSuccessMessage(double depositAmount) {
        System.out.println();
        System.out.println("Transaction success. An amount of $" + df.format(depositAmount) + " has been successfully deposited to your account.");
    }

    // Exit Messages
    public void displayExitMessage(double changeAmount,int[] numberOfCoins) {
        System.out.println();
        System.out.println("Dispensing your total change of $" + df.format(changeAmount) + " ...");
        System.out.println(numberOfCoins[0] + " Quarters, " + numberOfCoins[1] + " Dimes, " + numberOfCoins[2] + "Nickels");
        System.out.println("Dispense complete.");
        System.out.println();
        System.out.println("Enjoy your purchases! Have a great day!");
    }
}
