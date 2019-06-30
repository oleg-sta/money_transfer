package ru.test.account.validation;

import org.junit.Test;

import static org.junit.Assert.*;

public class AccountValidationTest {
    @Test
    public void checkCorrectAccounts() throws Exception {
        assertTrue(AccountValidation.checkAccount("11111"));
        assertTrue(AccountValidation.checkAccount("11113"));
        assertTrue(AccountValidation.checkAccount("11413"));
        assertTrue(AccountValidation.checkAccount("91413"));
    }

    @Test
    public void checkIncorrectAccountsByLength() throws Exception {
        assertFalse(AccountValidation.checkAccount("111111"));
        assertFalse(AccountValidation.checkAccount("1113"));
        assertFalse(AccountValidation.checkAccount("114"));
        assertFalse(AccountValidation.checkAccount(""));
        assertFalse(AccountValidation.checkAccount("123123123"));
    }

    @Test
    public void checkIncorrectAccountsBySymbols() throws Exception {
        assertFalse(AccountValidation.checkAccount("11a11"));
        assertFalse(AccountValidation.checkAccount("111b3"));
        assertFalse(AccountValidation.checkAccount("c1413"));
        assertFalse(AccountValidation.checkAccount("9141d"));
        assertFalse(AccountValidation.checkAccount("9141#"));
        assertFalse(AccountValidation.checkAccount("9141-"));
    }

    @Test
    public void checkIncorrectAccountsCombinations() throws Exception {
        assertFalse(AccountValidation.checkAccount("111a11"));
        assertFalse(AccountValidation.checkAccount("111db3"));
        assertFalse(AccountValidation.checkAccount("c1axsd413"));
        assertFalse(AccountValidation.checkAccount("914sd1d"));
        assertFalse(AccountValidation.checkAccount("!!914c1#"));
        assertFalse(AccountValidation.checkAccount("@q 9141-"));
    }

}