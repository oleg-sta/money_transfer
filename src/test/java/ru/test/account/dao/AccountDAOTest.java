package ru.test.account.dao;

import org.junit.Test;
import ru.test.account.model.AccountEntity;

import static org.junit.Assert.*;

public class AccountDAOTest {
    @Test
    public void complexTest() throws Exception {
        AccountDAO accountDAO = AccountDAO.getInstance();
        assertEquals(0, accountDAO.createAccount(new AccountEntity("11112", 22)));
        assertEquals(22, accountDAO.getAccountValue("11112").getSum().intValue());
        assertEquals(0, accountDAO.createAccount(new AccountEntity("11113", 24)));
        assertEquals(24, accountDAO.getAccountValue("11113").getSum().intValue());
        assertEquals(0, accountDAO.updateAccountsSum("11112", "11113", 3));
        assertEquals(19, accountDAO.getAccountValue("11112").getSum().intValue());
        assertEquals(27, accountDAO.getAccountValue("11113").getSum().intValue());
    }

}