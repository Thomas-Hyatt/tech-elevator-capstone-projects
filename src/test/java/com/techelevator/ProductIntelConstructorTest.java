package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductIntelConstructorTest {

    private ProductIntelConstructor sut;
    private List<String> productMenu;
    private Map<String, String> productNameMap = new HashMap<>();
    private Map<String, Double> productPriceMap = new HashMap<>();
    private Map<String, String> productTypeMap = new HashMap<>();

    @Before
    public void new_Object() {
        // ARRANGE
        sut = new ProductIntelConstructor("vendingmachine.csv");

        // ACT
        productMenu = sut.getProductMenu();
        productNameMap = sut.getProductNameMap();
        productPriceMap = sut.getProductPriceMap();
        productTypeMap = sut.getProductTypeMap();

    }
        // ASSERT
        @Test
        public void gproduct_menu_should_return_16_lines() {

            Assert.assertEquals(16, productMenu.size());
        }

        @Test
        public void product_menu_first_line_should_be_potato_crisps() {

            Assert.assertEquals("A1|Potato Crisps|3.05|Chip", productMenu.get(0));
        }

        @Test
        public void product_menu_last_line_should_be_triple_mint() {

            Assert.assertEquals("D4|Triplemint|0.75|Gum", productMenu.get(15));
        }

        @Test
        public void product_map_should_have_16_items() {

            Assert.assertEquals(16, productNameMap.size());
        }

        @Test
        public void product_map_A1_should_be_potato_crisps() {

            Assert.assertEquals("Potato Crisps", productNameMap.get("A1"));
        }

        @Test
        public void product_map_C2_should_be_Dr_Salt() {

            Assert.assertEquals("Dr. Salt", productNameMap.get("C2"));
        }

        @Test
        public void product_price_map_A3_should_be_2_75() {
            Double testPrice = 2.75;
            Assert.assertEquals(testPrice, productPriceMap.get("A3"));
        }

        @Test
        public void product_type_map_c2_should_be_drink() {
            Assert.assertEquals("Drink", productTypeMap.get("C2"));
        }

}
