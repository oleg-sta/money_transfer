package ru.test.account.service;

import ru.test.account.api.dto.StatusResponse;
import ru.test.account.dao.AccountDAO;
import ru.test.account.exception.BusinessException;
import ru.test.account.model.AccountEntity;
import ru.test.account.validation.AccountValidation;

public class AccountService {

    AccountDAO accountDAO = new AccountDAO();

    public AccountEntity getAccountInfo(String account) throws BusinessException {
        if (!AccountValidation.checkAccount(account)) {
            throw new BusinessException("Account invalid", StatusResponse.ACCOUNT_INVALID);
        }
        AccountEntity accountEntity = accountDAO.getAccountValue(account);
        if (accountEntity == null) {
            throw new BusinessException("No account", StatusResponse.NO_ACCOUNT1);
        }
        return accountDAO.getAccountValue(account);
    }

    public Integer createAccount(AccountEntity account) throws BusinessException {
        if (!AccountValidation.checkAccount(account.getName())) {
            throw new BusinessException("Account invalid", StatusResponse.ACCOUNT_INVALID);
        }
        if (account.getSum() < 0) {
            throw new BusinessException("Account sum invalid", StatusResponse.SUM_INVALID);
        }
        int status = accountDAO.createAccount(account);
        if (status == StatusResponse.OK) {
            return status;
        }
        throw new BusinessException("Error creating accont", status);
    }
}
