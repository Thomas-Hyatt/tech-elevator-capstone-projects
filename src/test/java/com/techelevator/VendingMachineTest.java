package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class VendingMachineTest {

    VendingMachine vendoMatic800 = new VendingMachine();
    // Construct Product Intel
    String productIntelFileString = "vendingmachine.csv";
    ProductIntelConstructor productIntelConstructor = new ProductIntelConstructor(productIntelFileString);
    @Before public void setup_vending_machine() {
    // ARRANGE
        // Construct Vending Machine
        vendoMatic800.turnOn(productIntelConstructor.getProductMenu(),                                          productIntelConstructor.getProductNameMap(),                                       productIntelConstructor.getProductPriceMap(),                                      productIntelConstructor.getProductTypeMap());
    }

    @Test
    public void dispense_twice_mountain_melter_should_return_3_stock() {

        // ACT
        String mountainMelterKey = "C3";
        vendoMatic800.dispenseItem(mountainMelterKey);
        vendoMatic800.dispenseItem(mountainMelterKey);
        // ASSERT
        Assert.assertEquals(3, vendoMatic800.getProduct(mountainMelterKey).getstock());

    }

    @Test
    public void key_B2_should_return_cowtales() {

        // ACT
        String key = "B2";
        // ASSERT
        Assert.assertEquals("Cowtales", vendoMatic800.getProduct(key).getName());
    }

    @Test
    public void product_array_size_returned_should_be_the_same_as_product_menu() {

        // ASSERT
        Assert.assertEquals(productIntelConstructor.getProductMenu().size(), vendoMatic800.getProductArray().length);
    }

}
