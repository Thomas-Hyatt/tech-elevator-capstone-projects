package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcTransferDaoTests extends BaseDaoTests{

    private static final BigDecimal BALANCE_1 = BigDecimal.valueOf(1000);

    private static final BigDecimal STARTING_BALANCE = BigDecimal.valueOf(1000);

    public static final String TRANSFER_TYPE_REQUEST = "Request";
    public static final String TRANSFER_TYPE_SEND = "Send";
    public static final String TRANSFER_STATUS_PENDING = "Pending";
    public static final String TRANSFER_STATUS_APPROVED = "Approved";
    public static final String TRANSFER_STATUS_REJECTED = "Rejected";

    protected static final User USER_1 = new User(1, "user1", "user1", "USER");
    protected static final User USER_2 = new User(2, "user2", "user2", "USER");
    private static final User USER_3 = new User(3, "user3", "user3", "USER");

    private static final Account ACCOUNT_1 = new Account(1, 1, BALANCE_1);
    private static final Account ACCOUNT_2 = new Account(2, 2, BigDecimal.valueOf(500));
    private static final Account ACCOUNT_3 = new Account(3, 3, BigDecimal.valueOf(100));

    private static final Transfer TRANSFER_1 = new Transfer(1, TRANSFER_TYPE_REQUEST, TRANSFER_STATUS_APPROVED, USER_1, USER_2, BigDecimal.valueOf(200));
    private static final Transfer TRANSFER_2 = new Transfer(2, TRANSFER_TYPE_SEND, TRANSFER_STATUS_APPROVED, USER_1, USER_3, BigDecimal.valueOf(25));
    private static final Transfer TRANSFER_3 = new Transfer(3, TRANSFER_TYPE_REQUEST, TRANSFER_STATUS_PENDING, USER_2, USER_3, BigDecimal.valueOf(2000));
    private static final Transfer TRANSFER_4 = new Transfer(4, TRANSFER_TYPE_REQUEST, TRANSFER_STATUS_PENDING, USER_1, USER_2, BigDecimal.valueOf(1000000));

    private JdbcTransferDAO sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDAO(jdbcTemplate);
    }

    // IJ: Get
    @Test(expected = IllegalArgumentException.class)
    public void getTransferById_throws_exception_when_given_invalid_id(){
         sut.getTransferById(-1);
    }

    @Test
    public void getTransferById_returns_null_when_given_nonexistent_id(){
        Assert.assertNull(sut.getTransferById(5));
    }

    @Test
    public void getTransferById_returns_correct_transfer(){
        Transfer actual = sut.getTransferById(TRANSFER_1.getTransferId());
        Assert.assertEquals(TRANSFER_1.getTransferId(),actual.getTransferId());
        Assert.assertEquals(TRANSFER_1.getAmount(),actual.getAmount());
        Assert.assertEquals(TRANSFER_1.getTransferStatus(),actual.getTransferStatus());
        Assert.assertEquals(TRANSFER_1.getTransferType(),actual.getTransferType());
        Assert.assertEquals(TRANSFER_1.getUserFrom(),actual.getUserFrom());
        Assert.assertEquals(TRANSFER_1.getUserTo(),actual.getUserTo());
    }

    // Thomas: Create
    @Test(expected = IllegalArgumentException.class)
    public void createTransfer_returns_exception_with_invalid_id() {

        sut.createTransfer(0,1,1,2, BigDecimal.valueOf(500));

    }

    @Test(expected = IllegalArgumentException.class)
    public void createTransfer_returns_exception_with_invalid_amount() {

        sut.createTransfer(1,1,1,2, BigDecimal.valueOf(-500));

    }

    @Test(expected = IllegalArgumentException.class)
    public void createTransfer_returns_exception_with_same_user_from_and_to() {

        sut.createTransfer(1,1,1,2, BigDecimal.valueOf(500));

    }

    @Test
    public void createTransfer_returns_correct_transfer_with_valid_inputs() {

        Transfer transferCreated = sut.createTransfer(1,2,1,2, BigDecimal.valueOf(500));

        Assert.assertEquals(1, transferCreated.getUserFrom().getId());
        Assert.assertEquals(2, transferCreated.getUserTo().getId());
        Assert.assertEquals(TRANSFER_TYPE_SEND, transferCreated.getTransferType());
        Assert.assertEquals(TRANSFER_STATUS_APPROVED, transferCreated.getTransferStatus());
        Assert.assertEquals(0, BigDecimal.valueOf(500).compareTo(transferCreated.getAmount()));

    }

    // Zack: update
    @Test(expected = IllegalArgumentException.class)
    public void updateTransferById_throws_exception_given_invalid_transferId() {
        Transfer actual = sut.updateTransferById(-1, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTransferById_throws_exception_given_invalid_statusId() {
        Transfer actual = sut.updateTransferById(1, 4);
    }

    @Test
    public void updateTransferById_returns_null_transfer_given_nonexistent_transferId() {
        Transfer actual = sut.updateTransferById(7, 1);

        Assert.assertNull(actual);
    }

    @Test
    public void updateTransferById_returns_updated_transfer_given_valid_transferId() {
        Transfer actual = sut.updateTransferById(3, 3);
        Transfer expected = new Transfer(3, TRANSFER_TYPE_REQUEST, TRANSFER_STATUS_REJECTED, USER_2, USER_3, BigDecimal.valueOf(2000));

        Assert.assertEquals(expected.getTransferStatus(), actual.getTransferStatus());
        Assert.assertEquals(expected.getTransferType(), actual.getTransferType());
        Assert.assertEquals(expected.getUserFrom(), actual.getUserFrom());
        Assert.assertEquals(expected.getUserTo(), actual.getUserTo());
    }

}
