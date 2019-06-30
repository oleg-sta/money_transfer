package ru.test.account.dao;

import ru.test.account.api.dto.StatusResponse;
import ru.test.account.model.AccountEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountDAO {

    private Map<String, AccountEntity> accounts = new ConcurrentHashMap<>();
    private Map<String, Lock> locksMonitor = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger();

    private static final AccountDAO accountDAO = new AccountDAO();
    private AccountDAO() {}
    public static AccountDAO getInstance() {
        return accountDAO;
    }

    public AccountEntity getAccountValue(String account) {
        AccountEntity accountEntity = accounts.get(account);
        return accountEntity;
    }

    public int updateAccountsSum(String accountEntity1, String accountEntity2, int sum) {
        String accountLockFirst;
        String accountLockSecond;
        if (accountEntity1.compareTo(accountEntity2) > 0) {
            accountLockFirst = accountEntity1;
            accountLockSecond = accountEntity2;
        } else {
            accountLockFirst = accountEntity2;
            accountLockSecond = accountEntity1;
        }
        Lock lockFirst = locksMonitor.computeIfAbsent(accountLockFirst, o -> new ReentrantLock());
        Lock lockSecond = locksMonitor.computeIfAbsent(accountLockSecond, o -> new ReentrantLock());
        lockFirst.lock();
        lockSecond.lock();
        try {
            AccountEntity newAccountEntity1 = accounts.get(accountEntity1);
            if (newAccountEntity1.getSum() < sum) {
                return StatusResponse.ACCOUNT1_NO_MONEY;
            }
            newAccountEntity1.setSum(newAccountEntity1.getSum() - sum);
            AccountEntity newAccountEntity2 = accounts.get(accountEntity2);
            newAccountEntity2.setSum(newAccountEntity2.getSum() + sum);
        } finally {
            lockSecond.unlock();
            lockFirst.unlock();
        }
        return StatusResponse.OK;
    }

    // we assume that this is a rare operation
    public synchronized int createAccount(AccountEntity account) {
        if (accounts.containsKey(account.getName())) {
            return StatusResponse.ACCOUNT_EXISTS;
        }
        account.setId(counter.incrementAndGet());
        accounts.put(account.getName(), account);
        return StatusResponse.OK;
    }
}
