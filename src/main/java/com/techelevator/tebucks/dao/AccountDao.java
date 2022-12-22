package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

    Account getAccountById(int accountId);

    Account getAccountByUserId(int userId);

    BigDecimal getAccountBalance(int accountId);

    List<Transfer> getAccountTransfers(int accountId);

    Account createAccount (int userId);

    BigDecimal updateBalance(BigDecimal amount, int user_id, boolean receiver);
}
