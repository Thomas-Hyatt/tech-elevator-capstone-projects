package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProductIntelConstructor {


    private File filePathToProducts;


    private ArrayList<String> productMenu;

        // Created maps to house our item names, item prices and item types
        // used maps because we want to store those item properties along with the item key
    private Map<String, String> productNameMap = new HashMap<>();
    private Map<String, Double> productPriceMap = new HashMap<>();
    private Map<String, String> productTypeMap = new HashMap<>();

        //constructor to initialize object
    public ProductIntelConstructor(String filePathFromUser) {
       //initalize the file object
        File filePathToProducts = new File(filePathFromUser);

        //created a try loop to ensure that the code could be tested for errors and continue running
        //Created a scanner to read through the list of snack items
        try(Scanner fileScanner = new Scanner(filePathToProducts)) {
           this.productMenu = new ArrayList<String>();
           this.productNameMap = new HashMap<String, String>();
           this.productPriceMap = new HashMap<String, Double>();
           this.productTypeMap = new HashMap<String, String>();
            while(fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                productMenu.add(line);

            //Created a string array and split the item information using the pipe key
                String[] productProperties = line.split("\\|");
            //doing the .put command to put specific properties into each map
                productNameMap.put(productProperties[0], productProperties[1]);
                productPriceMap.put(productProperties[0], Double.parseDouble(productProperties[2]));
                productTypeMap.put(productProperties[0],productProperties[3]);
            }

        } catch (FileNotFoundException e) {
            System.out.println("Sorry, this File was not found.");
        }
    }
        //getters to retrieve the information from the array list and maps
    public ArrayList<String> getProductMenu() {
        return productMenu;
    }

    public Map<String, String> getProductNameMap() {
        return productNameMap;
    }

    public Map<String, Double> getProductPriceMap() {
        return productPriceMap;
    }

    public Map<String, String> getProductTypeMap() {
        return productTypeMap;
    }

}
