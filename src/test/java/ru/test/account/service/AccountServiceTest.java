package ru.test.account.service;


import org.junit.Test;
import ru.test.account.api.dto.StatusResponse;
import ru.test.account.exception.BusinessException;
import ru.test.account.model.AccountEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AccountServiceTest {
    private static AccountService accountService = new AccountService();

    @Test
    public void getAccountInfoNegative() {
        try {
            accountService.getAccountInfo("111");
            fail();
        } catch (BusinessException e) {
            assertEquals(StatusResponse.ACCOUNT_INVALID, e.getCode());
        }
        try {
            accountService.getAccountInfo("1111s");
            fail();
        } catch (BusinessException e) {
            assertEquals(StatusResponse.ACCOUNT_INVALID, e.getCode());
        }
    }

    @Test
    public void createAccountNegative() {
        try {
            accountService.createAccount(new AccountEntity("1111", 1));
            fail();
        } catch (BusinessException e) {
            assertEquals(StatusResponse.ACCOUNT_INVALID, e.getCode());
        }
        try {
            accountService.createAccount(new AccountEntity("1111s", 1));
            fail();
        } catch (BusinessException e) {
            assertEquals(StatusResponse.ACCOUNT_INVALID, e.getCode());
        }
        try {
            accountService.createAccount(new AccountEntity("11111", -1));
            fail();
        } catch (BusinessException e) {
            assertEquals(StatusResponse.SUM_INVALID, e.getCode());
        }
    }

    @Test
    public void createAccountPositive() {
        try {
            accountService.createAccount(new AccountEntity("11111", 1));
        } catch (BusinessException e) {
            fail();
        }
    }

}