package com.techelevator.tebucks.dao;

import com.techelevator.tebucks.model.Account;
import com.techelevator.tebucks.model.Transfer;
import com.techelevator.tebucks.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcAccountDaoTests extends BaseDaoTests{

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


//    INSERT INTO transfers (user_from, user_to, type_id, status_id, amount) VALUES (2, 3, 1, 2, '100');
//    INSERT INTO transfers (user_from, user_to, type_id, status_id, amount) VALUES (3, 1, 2, 1, '20');

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    //****getAccountBalance()
    @Test
    public void getAccountBalance_returns_null_when_given_nonexistent_id() {
        BigDecimal actual = sut.getAccountBalance(4);

        Assert.assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAccountBalance_returns_exception_when_given_invalid_id() {
        sut.getAccountBalance(-1);
    }

    @Test
    public void getAccountBalance_returns_balance_when_given_valid_id() {
        BigDecimal actual = sut.getAccountBalance(1);


        Assert.assertTrue(BigDecimal.valueOf(1000).compareTo(actual) == 0);
    }

    //***getAccountByUserID
    @Test
    public void getAccountByUserID_returns_null_when_given_nonexistent_user_id(){
        Assert.assertNull(sut.getAccountByUserId(4));
    }
    @Test(expected = IllegalArgumentException.class)
    public void getAccountByUserID_throws_exception_when_given_invalid_id(){
        sut.getAccountByUserId(-1);
    }
    @Test
    public void getAccountByUserID_gets_correct_account(){
        BigDecimal actual = ACCOUNT_2.getBalance();
        Account accountTest = sut.getAccountByUserId(USER_2.getId());
        BigDecimal expected = accountTest.getBalance();

        Assert.assertEquals(actual,expected);
    }



    //***getAccountTransfers()
    @Test
    public void getAccountTransfers_returns_empty_list_when_given_nonexistent_id() {
        List<Transfer> actual = sut.getAccountTransfers(5);

        Assert.assertEquals(0, actual.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getAccountTransfers_returns_exception_when_given_invalid_id() {
        sut.getAccountTransfers(-1);
    }

    @Test
    public void getAccountTransfers_returns_correct_transfers() {

        List<Transfer> actual = sut.getAccountTransfers(1);
        List<Transfer> expected = new ArrayList<>();
        expected.add(TRANSFER_1);
        expected.add(TRANSFER_2);
        expected.add(TRANSFER_4);

        Assert.assertEquals(expected.size(), actual.size());

    }

    //***createAccount()
    @Test(expected = IllegalArgumentException.class)
    public void createAccount_returns_exception_with_invalid_id() {

        sut.createAccount(-100000000);

    }

    @Test
    public void createAccount_returns_correct_account_with_valid_id() {

        Account accountCreated = sut.createAccount(3);
        Assert.assertEquals(3, accountCreated.getUserId());
        Assert.assertTrue(STARTING_BALANCE.compareTo(accountCreated.getBalance()) == 0);
    }


    //***updateBalance()
    @Test(expected = IllegalArgumentException.class)
    public void updatedBalance_throws_exception_if_amount_is_negative() {
        sut.updateBalance(BigDecimal.valueOf(-1), 1, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updatedBalance_throws_exception_if_userId_is_invalid() {
        sut.updateBalance(BigDecimal.valueOf(100), -1, true);
    }

    @Test
    public void updatedBalance_does_not_update_if_transfer_results_in_negative_balance() {
        BigDecimal actual = sut.updateBalance(BigDecimal.valueOf(200), 3, false);
        BigDecimal expected = sut.getAccountBalance(3);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void updatedBalance_returns_null_if_given_nonexistent_id() {
        BigDecimal actual1 = sut.updateBalance(BigDecimal.valueOf(100), 17, true);
        BigDecimal actual2 = sut.updateBalance(BigDecimal.valueOf(100), 17, false);

        Assert.assertNull(actual1);
        Assert.assertNull(actual2);
    }

    @Test
    public void updatedBalance_adds_correct_amount_if_valid_amount() {
        BigDecimal actual = sut.updateBalance(BigDecimal.valueOf(100), 3, true);
        BigDecimal expected = new BigDecimal(200);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void updatedBalance_subtracts_correct_amount_if_valid_amount() {
        BigDecimal actual = sut.updateBalance(BigDecimal.valueOf(25), 3, false);
        BigDecimal expected = new BigDecimal(75);

        Assert.assertEquals(expected, actual);
    }
}
