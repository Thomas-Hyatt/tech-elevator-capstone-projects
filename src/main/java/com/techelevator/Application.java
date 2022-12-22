package com.techelevator;

import com.techelevator.sales_items.*;

import java.util.ArrayList;
import java.util.Map;

public class Application {
    public static void main(String[] args) {

    // Initialize
        // Construct Product Intel
        String productIntelFileString = "vendingmachine.csv";
        ProductIntelConstructor PIC = new ProductIntelConstructor(productIntelFileString);

        // Construct Vending Machine
        VendingMachine vendoMatic800 = new VendingMachine();
        vendoMatic800.turnOn(PIC.getProductMenu(),
                             PIC.getProductNameMap(),
                             PIC.getProductPriceMap(),
                             PIC.getProductTypeMap());
        // Construct User Account
        Account userAccount = new Account();
        // Construct UI
        UserInterface UI = new UserInterface();
        // Misc - log file
        String logPath = "log.txt";

        // Main Program - this block of code is all about how and when we move
        // through the various menus within the vending machine
        UI.displayStartupMessage();
        boolean exitProgram = false;
        while (!exitProgram) {
            int mainMenuUserInput =  UI.displayMainMenu();
            switch (mainMenuUserInput) {
                case 1:
                    UI.displayProductMenu(PIC.getProductMenu(), vendoMatic800.getProductArray());
                    break;

                case 2:
                    boolean exitPurchaseMenu = false;
                    while (!exitPurchaseMenu) {
                        int purchaseMenuUserInput = UI.displayPurchaseMenu(userAccount.getBalance());
                        switch (purchaseMenuUserInput) {

                            case 1:
                                int depositAmount = UI.displayFeedMoneyMessage(userAccount.getBalance());
                                double accountBalanceBeforeDepo = userAccount.getBalance();
                                userAccount.deposit(depositAmount);
                                if (userAccount.getBalance() > accountBalanceBeforeDepo) {
                                    userAccount.logUserAction(logPath, "FEED MONEY:", depositAmount, userAccount.getBalance());
                                    UI.displayDepositSuccessMessage(depositAmount);
                                } else {
                                    UI.displayInvalidInputMessage();
                                }
                                break;

                            case 2:
                                UI.displayProductMenu(PIC.getProductMenu(),
                                                      vendoMatic800.getProductArray());
                                String userInputKey = UI.displaySelectProductMessage();

                                if (PIC.getProductNameMap().containsKey(userInputKey)) {
                                    Product product = vendoMatic800.getProduct(userInputKey);
                                    if (product.getstock()>0) {
                                        if (userAccount.getBalance() >= product.getItemPrice()) {
                                            UI.displayPurchaseMessage(product);
                                            vendoMatic800.dispenseItem(product.getKey());
                                            userAccount.withdraw(product.getItemPrice());
                                            userAccount.logUserAction(logPath, product.getName() + " " + product.getKey(), product.getItemPrice(), userAccount.getBalance());
                                        } else {
                                            UI.displayInsufficientBalanceMessage();
                                        }
                                    } else {
                                        UI.displayOutOfStockMessage();
                                    }
                                    break;
                                } else {
                                    UI.displayInvalidInputMessage();
                                }
                                break;

                            case 3:
                                exitPurchaseMenu = true;
                                break;

                            default:
                                UI.displayInvalidInputMessage();
                        }
                    }
                    break;

                case 3:
                    double changeAmount = userAccount.getBalance();
                    int[] numberOfCoinsFromChange = vendoMatic800.dispenseChange(changeAmount);
                    userAccount.withdraw(userAccount.getBalance());
                    userAccount.logUserAction(logPath, "GIVE CHANGE:", changeAmount, userAccount.getBalance());

                    UI.displayExitMessage(changeAmount, numberOfCoinsFromChange);

                    exitProgram = true;
                    break;

                default:
                    UI.displayInvalidInputMessage();
            }
        }

    }
}