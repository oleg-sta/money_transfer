package ru.test.account.dao;

import ru.test.account.api.dto.StatusResponse;
import ru.test.account.model.AccountEntity;
import ru.test.account.db.PersistenceManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AccountDAO {

    private Map<String, Lock> locksMonitor = new ConcurrentHashMap<>();

    EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();

    public AccountEntity getAccountValue(String account) {
        EntityManager theManager = emf.createEntityManager();
        List<AccountEntity> entities = theManager.createQuery("SELECT t FROM AccountEntity t where t.name = :value1", AccountEntity.class)
                .setParameter("value1", account).getResultList();
        if (entities.size() == 1) {
            System.out.println("result " + entities.get(0).getSum());
            return entities.get(0);
        }
        theManager.close();
        return null;
    }

    public int updateAccountsSum(AccountEntity accountEntity1, AccountEntity accountEntity2, int sum) {
        EntityManager theManager = emf.createEntityManager();
        String accountLockFirst;
        String accountLockSecond;
        if (accountEntity1.getName().compareTo(accountEntity2.getName()) > 0) {
            accountLockFirst = accountEntity1.getName();
            accountLockSecond = accountEntity2.getName();
        } else {
            accountLockFirst = accountEntity2.getName();
            accountLockSecond = accountEntity1.getName();
        }
        Lock lockFirst = locksMonitor.computeIfAbsent(accountLockFirst, o -> new ReentrantLock());
        Lock lockSecond = locksMonitor.computeIfAbsent(accountLockSecond, o -> new ReentrantLock());
        lockFirst.lock();
        lockSecond.lock();
        try {
            theManager.getTransaction().begin();
            AccountEntity newAccountEntity1 = theManager.find(AccountEntity.class, accountEntity1.getId());
            if (newAccountEntity1.getSum() < sum) {
                theManager.getTransaction().commit();
                return StatusResponse.ACCOUNT1_NO_MONEY;
            }
            newAccountEntity1.setSum(newAccountEntity1.getSum() - sum);
            theManager.merge(newAccountEntity1);
            AccountEntity newAccountEntity2 = theManager.find(AccountEntity.class, accountEntity2.getId());
            newAccountEntity2.setSum(newAccountEntity2.getSum() + sum);
            theManager.merge(newAccountEntity2);
            theManager.getTransaction().commit();
            theManager.close();
        } finally {
            lockSecond.unlock();
            lockFirst.unlock();
        }
        return StatusResponse.OK;
    }

    public int createAccount(AccountEntity account) {
        EntityManager theManager = emf.createEntityManager();
        try {
            theManager.getTransaction().begin();
            theManager.persist(account);
            theManager.getTransaction().commit();
        } catch (PersistenceException e) {
            return StatusResponse.ACCOUNT_EXISTS;
        }
        theManager.close();

        return StatusResponse.OK;
    }
}
