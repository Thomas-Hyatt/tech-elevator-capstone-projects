package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate = new JdbcTemplate();
    JdbcTransferDAO jdbcTransferDAO;
    private static final BigDecimal STARTING_BALANCE = BigDecimal.valueOf(1000);

    public JdbcAccountDao (JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTransferDAO = new JdbcTransferDAO(jdbcTemplate);
    }

    @Override
    public Account getAccountById(int accountId) {
        if (accountId < 1) throw new IllegalArgumentException("ID is invalid.");

        Account account;
        String sql = "" +
                "SELECT account_id, user_id, balance FROM accounts WHERE account_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);
            if (rowSet.next()) {
                account = mapRowToAccount(rowSet);
                return account;
            }

        } catch (EmptyResultDataAccessException | NullPointerException e) {

        }

        return null;
    }

    @Override
    public Account getAccountByUserId(int userId) {
        if (userId < 1) throw new IllegalArgumentException("ID is invalid.");

        Account account;
        String sql = "" +
                "SELECT account_id, user_id, balance FROM accounts WHERE user_id = ?;";

        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, userId);
            if (rowSet.next()) {
                account = mapRowToAccount(rowSet);
                return account;
            }

        } catch (EmptyResultDataAccessException | NullPointerException e) {

        }

        return null;
    }

    @Override
    public BigDecimal getAccountBalance(int accountId) {
        if (accountId < 1) throw new IllegalArgumentException("ID is invalid.");

            String sql = "" +
                    "SELECT balance FROM accounts WHERE account_id = ?;";

            try {

                BigDecimal balance = jdbcTemplate.queryForObject(sql, BigDecimal.class, accountId);
                    return balance;
            } catch (EmptyResultDataAccessException | NullPointerException e) {

            }

        return null;
    }

    @Override
    public List<Transfer> getAccountTransfers(int accountId) {
        if (accountId < 1) {
            throw new IllegalArgumentException("Id is invalid");
        }

        List<Transfer> transfers = new ArrayList<>();

        String sql = "" +
                "SELECT transfer_id, type_id, status_id, user_from, user_to, amount " +
                "FROM transfers " +
                "JOIN users ON users.user_id = transfers.user_from " +
                "JOIN accounts USING (user_id) " +
                "WHERE account_id = ? " +
                "ORDER BY transfer_id;";

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, accountId);

        while (rowSet.next()) {
            Transfer transfer = jdbcTransferDAO.mapRowToTransfer(rowSet);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public Account createAccount(int userId) {
        if (userId < 1) {
            throw new IllegalArgumentException("This id stinks!");
        }

        String sql = "" +
                "INSERT INTO accounts (user_id, balance) " +
                "VALUES (?, ?) " +
                "RETURNING account_id;";

        Integer accountId = jdbcTemplate.queryForObject(sql, Integer.class, userId, STARTING_BALANCE);

        if (accountId != null) {
            return getAccountById(accountId);
        } else {
            return null;
        }

    }

    public BigDecimal updateBalance(BigDecimal amount, int accountId, boolean receiver){
        if (amount.compareTo(BigDecimal.valueOf(0)) <= 0 ) {
            throw new IllegalArgumentException("the amount is invalid");
        } else if (accountId < 1) {
            throw new IllegalArgumentException("the user id is invalid");
        } else if (getAccountById(accountId) == null) {
            return null;
        }

        if(receiver) {

            String sql = "" +
                    "UPDATE accounts " +
                    "SET balance = (balance + ?) " +
                    "WHERE account_id = ?;";

            try {
                jdbcTemplate.update(sql, amount, accountId);
            } catch (DataIntegrityViolationException e) {
                throw new DataIntegrityViolationException("ID does not exist");
            }

        } else {

            if ((getAccountById(accountId).getBalance().subtract(amount)).compareTo(BigDecimal.valueOf(0)) >= 0) {
                String sql2 = "" +
                        "UPDATE accounts " +
                        "SET balance = balance - ? " +
                        "WHERE account_id = ? ;";

                try {
                    jdbcTemplate.update(sql2, amount, accountId);
                } catch (DataIntegrityViolationException e) {
                    throw new DataIntegrityViolationException("ID does not exist");
                }

            } else if ((getAccountById(accountId).getBalance().subtract(amount)).compareTo(BigDecimal.valueOf(0)) == -1) {
                String sql3 = "" +
                        "SELECT balance FROM accounts WHERE account_id = ?;";

                try {

                    BigDecimal balance = jdbcTemplate.queryForObject(sql3, BigDecimal.class, accountId);
                    return balance;
                } catch (EmptyResultDataAccessException | NullPointerException e) {

                }
            }
        }
        return getAccountById(accountId).getBalance();
    }

    private Account mapRowToAccount(SqlRowSet rowSet) {
        Account account = new Account();

        account.setAccountId(rowSet.getInt("account_id"));
        account.setUserId(rowSet.getInt("user_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));

        return account;
    }

}
