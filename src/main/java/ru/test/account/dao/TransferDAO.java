package ru.test.account.dao;

import ru.test.account.db.PersistenceManager;
import ru.test.account.model.TransferEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public class TransferDAO {

    EntityManagerFactory emf = PersistenceManager.getInstance().getEntityManagerFactory();


    public void createTransfer(TransferEntity transferEntity) {
        EntityManager theManager = emf.createEntityManager();
        theManager.getTransaction().begin();
        theManager.persist(transferEntity);
        theManager.getTransaction().commit();
        theManager.close();
    }

    public void updateStatus(TransferEntity transferEntity) {
        EntityManager theManager = emf.createEntityManager();
        theManager.getTransaction().begin();
        theManager.merge(transferEntity);
        theManager.getTransaction().commit();
        theManager.close();
    }
}
