package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.AccountDao;
import com.techelevator.tebucks.dao.TransferDAO;
import com.techelevator.tebucks.dao.UserDao;
import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.LoginResponseDto;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.User;
import com.techelevator.tebucks.security.jwt.TokenProvider;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/account")
@PreAuthorize("isAuthenticated()")
public class AccountController {

    private final AccountDao accountDao;
    private final TransferDAO transferDAO;
    private final UserDao userDao;

    public AccountController(AccountDao accountDao, TransferDAO transferDAO, UserDao userDao) {
        this.accountDao = accountDao;
        this.transferDAO = transferDAO;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(Principal loggedInUser) {
        return accountDao.getAccountBalance(accountDao.getAccountByUserId(userDao.findIdByUsername(loggedInUser.getName())).getAccountId());
    }

    @RequestMapping(path = "/transfers", method = RequestMethod.GET)
    public List<Transfer> getAccountTransfers(Principal loggedInUser) {

        int userId = userDao.findIdByUsername(loggedInUser.getName());
        Account userAccount = accountDao.getAccountByUserId(userId);
        int accountId = userAccount.getAccountId();

        return accountDao.getAccountTransfers(accountId);
    }

}
