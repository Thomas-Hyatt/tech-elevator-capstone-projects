package com.techelevator;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class AccountTest {

    Account testAccount;

    @Before
    public void newObject() {
        // ARRANGE
        testAccount = new Account();

    }
        @Test
        public void new_account_should_have_zero_balance() {

            // ACT
            double newBalance = testAccount.getBalance();

            // ASSERT
            Assert.assertEquals(BigDecimal.valueOf(0.0), BigDecimal.valueOf(newBalance));
        }

        @Test
        public void withdrawing_some_money_with_no_balance() {

            // ACT
            double withdrawAmount = 5.00;
            double newBalance = testAccount.withdraw(withdrawAmount);

            // ASSERT
            Assert.assertEquals(BigDecimal.valueOf(0.0), BigDecimal.valueOf(newBalance));
        }

        @Test
        public void withdrawing_and_depositing() {

            // ACT
            int depositAmount = 69;
            int withdrawAmount = 5;
            double newBalance1 = testAccount.deposit(depositAmount);
            double newBalance2 = testAccount.withdraw(withdrawAmount);

            // ASSERT
            Assert.assertEquals(BigDecimal.valueOf(69.0), BigDecimal.valueOf(newBalance1));
            Assert.assertEquals(BigDecimal.valueOf(64.0), BigDecimal.valueOf(newBalance2));
        }

}
