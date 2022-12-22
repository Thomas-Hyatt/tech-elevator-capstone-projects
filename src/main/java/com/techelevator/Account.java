package com.techelevator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Account {

    // Instance Variables
    private double balance;
    private UserInterface UI;

    private DateTimeFormatter dateFormatter;
    private DateTimeFormatter timeFormatter;
    private final DecimalFormat df = new DecimalFormat("0.00");


    // Constructor
    public Account() {
        // main variables
        this.balance = 0.0;
        // date & time
        this.dateFormatter = DateTimeFormatter.ofPattern("MM/dd/uuuu");
        this.timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    }

    // withdrawl method
    public double withdraw(double withdrawAmount) {
        if (this.balance >= withdrawAmount && withdrawAmount > 0) {
            this.balance -= withdrawAmount;
        }
        return this.balance;
    }

    // Method to establish a function for depositing money into the vending machine
    public double deposit(int depositAmount) {
        if (depositAmount > 0) {
            this.balance += depositAmount;
        }
        return this.balance;
    }

    // Method for creating the log of transactions
    public void logUserAction(String logPath, String userAction, double changeInBalance, double newBalance) {
       //creates the txt file
        File logFile = new File(logPath);

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        String userActionDateString = this.dateFormatter.format(localDate);
        String userActionTimeString = this.timeFormatter.format(localTime);

        String amOrPm = (Integer.parseInt(userActionTimeString.substring(0,2)) < 12 ? "AM" : "PM");

        try (PrintWriter writer = new PrintWriter(new FileWriter(logFile, true))){
            writer.println(userActionDateString + " " + userActionTimeString + " " + amOrPm + " " +
                           userAction + " $" + df.format(changeInBalance) + " $" + df.format(newBalance));
        } catch (IOException logFileNotFoundException) {
            System.out.println("Invalid log File Path!");
        }
    }

    // Getter to retrieve the balance in the vending machine
    public double getBalance() {
        return balance;
    }


}
