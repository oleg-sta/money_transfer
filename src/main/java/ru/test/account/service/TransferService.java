package ru.test.account.service;

import ru.test.account.api.dto.StatusResponse;
import ru.test.account.dao.AccountDAO;
import ru.test.account.dao.TransferDAO;
import ru.test.account.exception.BusinessException;
import ru.test.account.model.AccountEntity;
import ru.test.account.model.TransferEntity;
import ru.test.account.validation.AccountValidation;

public class TransferService {

    AccountDAO accountDAO = new AccountDAO();
    TransferDAO transferDAO = new TransferDAO();

    public Integer transferMoney(TransferEntity transferEntity) throws BusinessException {
        if (!AccountValidation.checkAccount(transferEntity.getAccount1()) || !AccountValidation.checkAccount(transferEntity.getAccount2()) || transferEntity.getAccount1().equals(transferEntity.getAccount2())) {
            throw new BusinessException("Account invalid", StatusResponse.ACCOUNT_INVALID);
        }
        if (transferEntity.getAmount() < 1) {
            throw new BusinessException("Sum invalid", StatusResponse.SUM_INVALID);
        }
        transferDAO.createTransfer(transferEntity);
        AccountEntity account1 = accountDAO.getAccountValue(transferEntity.getAccount1());
        if (account1 == null) {
            throw new BusinessException("Account1 invalid", StatusResponse.NO_ACCOUNT1);
        }
        AccountEntity account2 = accountDAO.getAccountValue(transferEntity.getAccount2());
        if (account2 == null) {
            throw new BusinessException("Account2 invalid", StatusResponse.NO_ACCOUNT2);
        }
        int result = accountDAO.updateAccountsSum(account1, account2, transferEntity.getAmount());
        if (result != StatusResponse.OK) {
            throw new BusinessException("Error creating trasnfer", result);
        }
        transferEntity.setStatus(1);
        transferDAO.updateStatus(transferEntity);
        return StatusResponse.OK;
    }
}
