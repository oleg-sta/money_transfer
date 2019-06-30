package ru.test.account.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import ru.test.account.api.dto.AccountDTO;
import ru.test.account.api.dto.AccountResponse;
import ru.test.account.api.dto.StatusResponse;
import ru.test.account.exception.BusinessException;
import ru.test.account.service.AccountService;

import javax.ws.rs.core.Response;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AccountTest {
    ObjectMapper mapper = new ObjectMapper();
    @Test
    public void createAccountSuccess() throws Exception {
        Account account = new Account();
        AccountService accountService = mock(AccountService.class);
        when(accountService.createAccount(any())).thenReturn(StatusResponse.OK);
        account.accountService = accountService;
        Response response = account.createAccount(new AccountDTO("11111", 12));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        AccountResponse accountResponse = mapper.readValue(response.getEntity().toString(), AccountResponse.class);

        assertEquals(StatusResponse.OK, accountResponse.getStatusResponse());
        assertEquals("11111", accountResponse.getAccount());
        assertEquals(12, accountResponse.getSum());
    }

    @Test
    public void createAccountBusinessError() throws Exception {
        Account account = new Account();
        AccountService accountService = mock(AccountService.class);
        when(accountService.createAccount(any())).thenThrow(new BusinessException("some error", 4));
        account.accountService = accountService;
        Response response = account.createAccount(new AccountDTO("11111", 12));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        AccountResponse accountResponse = mapper.readValue(response.getEntity().toString(), AccountResponse.class);
        assertEquals(4, accountResponse.getStatusResponse());
    }

    @Test
    public void createAccountOtherError() throws Exception {
        Account account = new Account();
        AccountService accountService = mock(AccountService.class);
        when(accountService.createAccount(any())).thenThrow(new RuntimeException("some error"));
        account.accountService = accountService;
        Response response = account.createAccount(new AccountDTO("11111", 12));
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

}