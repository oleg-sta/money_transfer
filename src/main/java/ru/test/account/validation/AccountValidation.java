package ru.test.account.validation;

import java.util.regex.Pattern;

public class AccountValidation {
    private static final Pattern accountPattern = Pattern.compile("^\\d{5}$");

    public static boolean checkAccount(String account) {
        if (account != null) {
            return accountPattern.matcher(account).matches();
        }
        return false;
    }
}
