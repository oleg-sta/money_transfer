package ru.test.account.api;

import org.junit.BeforeClass;
import org.junit.Test;
import ru.test.account.api.dto.StatusResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;

/**
 * Manual complex test. The application should be just started up. Each test works only one time, the second time couldn't be work
 */
public class ComplexIT {
    private static WebTarget target;

    @BeforeClass
    public static void init() {
        Client client = ClientBuilder.newClient();
        target = client.target(System.getProperty("url", "http://localhost:8080/"));
    }

    @Test
    public void testSimpleCase() {
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount("11131", 101, target));
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount("11132", 99, target));
        assertEquals(StatusResponse.OK, ManualClientHelper.createTransfer("11131", "11132", 4, target));
        assertEquals(StatusResponse.ACCOUNT_INVALID, ManualClientHelper.createTransfer("11131", "11131", 4, target));
    }

    @Test
    public void testSameAccount() {
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount("21131", 101, target));
        assertEquals(StatusResponse.ACCOUNT_INVALID, ManualClientHelper.createTransfer("21131", "21131", 4, target));
    }

    @Test
    public void testOneCase() {
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount("11111", 101, target));
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount("11112", 99, target));
        assertEquals(101, ManualClientHelper.checkAccount("11111", target));
        assertEquals(99, ManualClientHelper.checkAccount("11112", target));
        assertEquals(StatusResponse.OK, ManualClientHelper.createTransfer("11111", "11112", 4, target));
        assertEquals(97, ManualClientHelper.checkAccount("11111", target));
        assertEquals(103, ManualClientHelper.checkAccount("11112", target));
    }

    @Test
    public void testCreateAlreadyExistsAccount() {
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount("11161", 101, target));
        assertEquals(StatusResponse.ACCOUNT_EXISTS, ManualClientHelper.createAccount("11161", 101, target));
    }

    @Test
    public void testNegativeCreateAccount() {
        assertEquals(StatusResponse.ACCOUNT_INVALID, ManualClientHelper.createAccount("1111", 101, target));
        assertEquals(StatusResponse.ACCOUNT_INVALID, ManualClientHelper.createAccount("111s1", 101, target));
        assertEquals(StatusResponse.SUM_INVALID, ManualClientHelper.createAccount("11111", -101, target));
    }

    @Test
    public void testNegativeAmountTransfer() {
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount("11113", 101, target));
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount("11114", 99, target));
        assertEquals(StatusResponse.SUM_INVALID, ManualClientHelper.createTransfer("11113", "11114", 0, target));
        assertEquals(StatusResponse.SUM_INVALID, ManualClientHelper.createTransfer("11113", "11114", -1, target));
    }

    @Test
    public void testNegativeNoMoneyTransfer() {
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount("11151", 101, target));
        assertEquals(StatusResponse.OK, ManualClientHelper.createAccount("11152", 99, target));
        assertEquals(StatusResponse.ACCOUNT1_NO_MONEY, ManualClientHelper.createTransfer("11151", "11152", 102, target));
    }

    @Test
    public void testNegativeAccountDoesntExists() {
        assertEquals(StatusResponse.NO_ACCOUNT1, ManualClientHelper.createTransfer("11120", "11121", 1, target));
    }
}
