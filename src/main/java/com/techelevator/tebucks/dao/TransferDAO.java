package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Transfer;

import java.math.BigDecimal;

public interface TransferDAO {

    Transfer getTransferById(int user_id);

    Transfer createTransfer(int userFrom, int userTo, int typeId, int statusId, BigDecimal amount);

    Transfer updateTransferById(int transferId, int statusId);
}
