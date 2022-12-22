package com.techelevator.tebucks.controller;

import com.techelevator.tebucks.dao.*;
import com.techelevator.tebucks.model.*;
import com.techelevator.tebucks.service.IrsAuthenticationService;
import com.techelevator.tebucks.service.IrsLogService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/api/transfers")
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private final AccountDao accountDao;
    private final TransferDAO transferDAO;
    private final UserDao userDao;
    private final TypeDao typeDao;
    private final StatusDao statusDao;
    private final BigDecimal LOG_TO_IRS_MIN = BigDecimal.valueOf(1000);

    private final IrsAuthenticationService irsAuthenticationService = new IrsAuthenticationService();
    private IrsLogService irsLogService = new IrsLogService();


    public TransferController(AccountDao accountDao, TransferDAO transferDAO, UserDao userDao, TypeDao typeDao, StatusDao statusDao) {
        this.accountDao = accountDao;
        this.transferDAO = transferDAO;
        this.userDao = userDao;
        this.typeDao = typeDao;
        this.statusDao = statusDao;
        
        this.irsLogService.setAuthToken(irsAuthenticationService.login());
    }

    @RequestMapping(path = "/{transfer_id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int transfer_id) {
        return transferDAO.getTransferById(transfer_id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Transfer createTransfer(@RequestBody NewTransferDto transferDto) {

        int userFromId = transferDto.getUserFrom();
        int userFromAccountId = accountDao.getAccountByUserId(userFromId).getAccountId();
        int userToId = transferDto.getUserTo();
        int userToAccountId = accountDao.getAccountByUserId(userToId).getAccountId();
        int typeId = 0;
        int statusId = 0;

        //get type ID from typeDAO map and set typeID variable
        Map<Integer, String> typeMap = typeDao.getTypesMap();
        for (Map.Entry<Integer, String> entry: typeMap.entrySet()) {
            if (entry.getValue().equals(transferDto.getTransferType())) {
                typeId = entry.getKey();
            }
        }
        //if typeID matches the "Send" type ID...
        Map<Integer, String> statusMap = statusDao.getStatusMap();
        if (transferDto.getTransferType().equals(Transfer.TRANSFER_TYPE_SEND)) {

            BigDecimal oldUserFromBalance = accountDao.getAccountBalance(userFromAccountId);
            BigDecimal amountToSend = transferDto.getAmount();


            // if withdraw from userFrom would not result in overdraft
            if (amountToSend.compareTo(oldUserFromBalance) <= 0) {
                accountDao.updateBalance(transferDto.getAmount(), userToAccountId, true);
                accountDao.updateBalance(transferDto.getAmount(), userFromAccountId, false);
                // log transfer to IRS if amount transferred exceeds $1000
                if (transferDto.getAmount().compareTo(LOG_TO_IRS_MIN) >= 0) {

                    LogDto logDto = new LogDto();
                    logDto.setDescription("Transaction amount was at least $1000");
                    logDto.setAccount_from(userDao.getUserById(userFromId).getUsername());
                    logDto.setAccount_to(userDao.getUserById(userToId).getUsername());
                    logDto.setAmount(transferDto.getAmount().doubleValue());

                    irsLogService.logTransfer(logDto);
                }
                //set statusId variable to the Approved status ID
                statusId = getStatusID(Transfer.TRANSFER_STATUS_APPROVED);
            // if withdraw from userFrom was unsuccessful due to overdraft
            } else {
                //set status ID to Rejected ID
                statusId = getStatusID(Transfer.TRANSFER_STATUS_REJECTED);
                //log overdraft attempt to TEIRS
                LogDto logDto = new LogDto();
                logDto.setDescription("Transaction rejected because result would be an overdraft.");
                logDto.setAccount_from(userDao.getUserById(userFromId).getUsername());
                logDto.setAccount_to(userDao.getUserById(userToId).getUsername());
                logDto.setAmount(transferDto.getAmount().doubleValue());
                irsLogService.logTransfer(logDto);
            }

        //if type is a request, set status to pending
        } else {
           statusId = getStatusID(Transfer.TRANSFER_STATUS_PENDING);
        }

        return transferDAO.createTransfer(userFromId, userToId, typeId, statusId, transferDto.getAmount());
    }


    @RequestMapping(path = "/{transfer_id}/status", method = RequestMethod.PUT)
    public Transfer updateTransfer(@PathVariable int transfer_id, @RequestBody TransferStatusUpdateDto transferStatusUpdateDto) {

        int statusId = 0;
        String updateStatus = transferStatusUpdateDto.getTransferStatus();

        Transfer originalTransfer = transferDAO.getTransferById(transfer_id);
        int userFromAccountId = accountDao.getAccountByUserId(originalTransfer.getUserFrom().getId()).getAccountId();
        int userToAccountId = accountDao.getAccountByUserId(originalTransfer.getUserTo().getId()).getAccountId();


        // if userFrom clicked Approve
        if (updateStatus.equals(Transfer.TRANSFER_STATUS_APPROVED)) {
            BigDecimal oldUserFromBalance = accountDao.getAccountBalance(userFromAccountId);
            BigDecimal amountToSend = originalTransfer.getAmount();


            // if userFrom has enough balance for the amount
            if (amountToSend.compareTo(oldUserFromBalance) <= 0) {
                accountDao.updateBalance(originalTransfer.getAmount(), userFromAccountId, false);
                accountDao.updateBalance(originalTransfer.getAmount(), userToAccountId, true);

                // log transfer if amount is at least $1000
                if (originalTransfer.getAmount().compareTo(LOG_TO_IRS_MIN) >= 0) {

                    LogDto logDto = new LogDto();
                    logDto.setDescription("Transaction amount was at least $1000");
                    logDto.setAccount_from(originalTransfer.getUserFrom().getUsername());
                    logDto.setAccount_to(originalTransfer.getUserTo().getUsername());
                    logDto.setAmount(originalTransfer.getAmount().doubleValue());
                    irsLogService.logTransfer(logDto);
                }
                // set status id to approved ID
               statusId = getStatusID(Transfer.TRANSFER_STATUS_APPROVED);
            // if userFrom doesn't have enough balance for the amount
            } else if (amountToSend.compareTo(oldUserFromBalance) > 0) {

                //log overdraft attempt to TEIRS
                LogDto logDto = new LogDto();
                logDto.setDescription("Transaction rejected because result would be an overdraft.");
                logDto.setAccount_from(originalTransfer.getUserFrom().getUsername());
                logDto.setAccount_to(originalTransfer.getUserTo().getUsername());
                logDto.setAmount(originalTransfer.getAmount().doubleValue());
                irsLogService.logTransfer(logDto);
                // set status ID to rejected status ID
                statusId = getStatusID(Transfer.TRANSFER_STATUS_REJECTED);
            }
        // if userFrom clicked REJECT
        } else if (updateStatus.equals(Transfer.TRANSFER_STATUS_REJECTED)) {
           statusId = getStatusID(Transfer.TRANSFER_STATUS_REJECTED);
        }

        return transferDAO.updateTransferById(transfer_id, statusId);

    }

    //helper method to get status id in create/update transfer methods
    public int getStatusID(String statusType){
        Map<Integer, String> statusMap = statusDao.getStatusMap();
        int statusId = 0;
        for (Map.Entry<Integer, String> entry: statusMap.entrySet()) {
            if (entry.getValue().equals(statusType)) {
                statusId = entry.getKey();
            }
        }
        return  statusId;
    }

}
