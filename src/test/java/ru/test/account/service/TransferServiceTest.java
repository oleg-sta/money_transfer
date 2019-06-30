package ru.test.account.service;

import org.junit.Test;
import ru.test.account.api.dto.StatusResponse;
import ru.test.account.exception.BusinessException;
import ru.test.account.model.AccountEntity;
import ru.test.account.model.TransferEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TransferServiceTest {
    private static AccountService accountService = new AccountService();
    private static TransferService transferService = new TransferService();

    @Test
    public void createTransferIncorrectAccount() {
        try {
            transferService.transferMoney(new TransferEntity("1", "1", 1));
            fail();
        } catch (BusinessException e) {

        }
        try {
            transferService.transferMoney(new TransferEntity("1", "1", 1));
            fail();
        } catch (BusinessException e) {
        }
    }

    @Test
    public void createTransferIncorrectSum() {
        try {
            transferService.transferMoney(new TransferEntity("11111", "11112", -1));
            fail();
        } catch (BusinessException e) {
            assertEquals(StatusResponse.SUM_INVALID, e.getCode());
        }
    }

    @Test
    public void createTransferPositive() {
        try {
            accountService.createAccount(new AccountEntity("11131", 100));
            accountService.createAccount(new AccountEntity("11132", 100));
            transferService.transferMoney(new TransferEntity("11131", "11132", 2));
        } catch (BusinessException e) {
            fail();
        }
    }

    @Test
    public void createTransferIsufficientSum() {
        try {
            accountService.createAccount(new AccountEntity("11121", 100));
            accountService.createAccount(new AccountEntity("11122", 100));
            transferService.transferMoney(new TransferEntity("11121", "11122", 200));
            fail();
        } catch (BusinessException e) {
            assertEquals(StatusResponse.ACCOUNT1_NO_MONEY, e.getCode());
        }
    }
}
