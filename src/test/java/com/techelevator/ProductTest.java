package com.techelevator;

import com.techelevator.sales_items.Candy;
import com.techelevator.sales_items.Chips;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class ProductTest {

    @Test
    public void candy_initial_stock_should_be_set_to_5() {
        // ARRANGE
        Candy wonkaBar;

        // ACT
        Double inputPrice = 1.50;
        wonkaBar = new Candy("B3", "Wonka Bar", inputPrice);

        // ASSERT
        Assert.assertEquals(5, wonkaBar.getstock());

    }

    @Test
    public void chip_stock_set_to_3() {
        // ARRANGE
        Chips cloudPopcorn;

        // ACT
        Double inputPrice = 3.65;
        cloudPopcorn = new Chips("A4", "Cloud Popcorn", inputPrice);
        cloudPopcorn.setStock(3);

        // ASSERT
        Assert.assertEquals(3, cloudPopcorn.getstock());
    }

    @Test
    public void chip_key_set_to_shrek() {
        // ARRANGE
        Chips cloudPopcorn;

        // ACT
        Double inputPrice = 3.65;
        cloudPopcorn = new Chips("Shrek", "Cloud Popcorn", inputPrice);
        cloudPopcorn.setStock(3);

        // ASSERT
        Assert.assertEquals("Shrek", cloudPopcorn.getKey());
    }

    @Test
    public void chip_price_set_to_9_99() {
        // ARRANGE
        Chips cloudPopcorn;

        // ACT
        double inputPrice = 9.99;
        cloudPopcorn = new Chips("Shrek", "Cloud Popcorn", inputPrice);
        cloudPopcorn.setStock(3);

        // ASSERT
        Assert.assertEquals(new BigDecimal(inputPrice), new BigDecimal(cloudPopcorn.getItemPrice()));
    }
}
