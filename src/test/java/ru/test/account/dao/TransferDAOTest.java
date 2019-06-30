package ru.test.account.dao;

import org.junit.Before;
import org.junit.Test;
import ru.test.account.model.TransferEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransferDAOTest {

    TransferDAO transferDAO = new TransferDAO();
    EntityManagerFactory emf;
    EntityManager theManager;

    @Before
    public void before() {
        emf = mock(EntityManagerFactory.class);
        theManager = mock(EntityManager.class);
        when(emf.createEntityManager()).thenReturn(theManager);
        when(theManager.getTransaction()).thenReturn(mock(EntityTransaction.class));
        transferDAO.emf = emf;
    }

    @Test
    public void createTransfer() throws Exception {
        transferDAO.createTransfer(new TransferEntity("1", "2", 3));
        verify(theManager, times(1)).persist(any());
    }

    @Test
    public void updateStatus() throws Exception {
        transferDAO.updateStatus(new TransferEntity("1", "2", 3));
        verify(theManager, times(1)).merge(any());
    }

}