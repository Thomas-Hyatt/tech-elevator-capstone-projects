package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;


import java.math.BigDecimal;
import java.util.Map;

@Component
public class JdbcTransferDAO implements  TransferDAO{

    private final JdbcTemplate jdbcTemplate;
    JdbcUserDao jdbcUserDao;
    JdbcTypeDao jdbcTypeDao;
    JdbcStatusDao jdbcStatusDao;
    private static Map<Integer, String> typeMap;
    private static Map<Integer, String> statusMap;

    public JdbcTransferDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcUserDao = new JdbcUserDao(jdbcTemplate);
        this.jdbcTypeDao = new JdbcTypeDao(jdbcTemplate);
        this.jdbcStatusDao = new JdbcStatusDao(jdbcTemplate);

        typeMap = jdbcTypeDao.getTypesMap();
        statusMap = jdbcStatusDao.getStatusMap();
    }

    @Override
    public Transfer getTransferById(int transferId) {
        if (transferId < 1) throw new IllegalArgumentException("Transfer ID is invalid.");

        Transfer transfer;

        String sql = "" +
                "SELECT transfer_id, type_id, status_id, user_from, user_to, amount " +
                "FROM transfers WHERE transfer_id = ? ;";
        try{
            SqlRowSet transferGot = jdbcTemplate.queryForRowSet(sql, transferId);
            if (transferGot.next()) {
                transfer = mapRowToTransfer(transferGot);
                return transfer;
            }
        }catch (EmptyResultDataAccessException | NullPointerException e){
            e.getMessage();
        }
        return null;
    }

    @Override
    public Transfer createTransfer(int userFrom, int userTo, int typeId, int statusId, BigDecimal amount) {
        if (userFrom < 1 || userTo < 1 || !typeMap.containsKey(typeId) || !statusMap.containsKey(statusId)) {
            throw new IllegalArgumentException("invalid id!");
        } else if (userFrom == userTo) {
            throw new IllegalArgumentException("userFrom and userTo cannot be the same!");
        } else if (amount.compareTo(BigDecimal.valueOf(0)) <= 0 ) {
            throw new IllegalArgumentException("the amount is invalid!");
        }

        String sql = ""+
                "INSERT INTO transfers (type_id, status_id, user_from, user_to, amount) "+
                "VALUES (?, ?, ?, ?, ?)" +
                "RETURNING transfer_id;";

        Integer transferId = jdbcTemplate.queryForObject(sql, Integer.class, typeId, statusId, userFrom, userTo, amount);
        if(transferId!=null) {
            return getTransferById(transferId);
        }
            return null;
    }

    @Override
    public Transfer updateTransferById(int transferId, int statusId) {
        if ((transferId < 1) || !statusMap.containsKey(statusId)) {
            throw new IllegalArgumentException("Invalid transfer type.");
        }
        Transfer transfer;
        String sql = ""+
                "UPDATE transfers SET status_id = ? WHERE transfer_id = ? ;";

            jdbcTemplate.update(sql, statusId, transferId);
            transfer = getTransferById(transferId);
            return transfer;

    }

    public Transfer mapRowToTransfer(SqlRowSet rowset) {
        Transfer transfer = new Transfer();

        transfer.setTransferId(rowset.getInt("transfer_id"));

        for (Map.Entry<Integer, String> entry: typeMap.entrySet()) {
            if (entry.getKey().equals(rowset.getInt("type_id"))) {
                transfer.setTransferType(entry.getValue());
            }
        }

        for (Map.Entry<Integer, String> entry: statusMap.entrySet()) {
            if (entry.getKey().equals(rowset.getInt("status_id"))) {
                transfer.setTransferStatus(entry.getValue());
            }
        }

        transfer.setUserFrom(jdbcUserDao.getUserById(rowset.getInt("user_from")));
        transfer.setUserTo(jdbcUserDao.getUserById(rowset.getInt("user_to")));
        transfer.setAmount(rowset.getBigDecimal("amount"));

        return transfer;
    }
}
