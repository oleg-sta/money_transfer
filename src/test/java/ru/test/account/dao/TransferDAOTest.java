package ru.test.account.dao;

import org.junit.Test;
import ru.test.account.model.TransferEntity;

import static org.junit.Assert.assertTrue;

public class TransferDAOTest {

    @Test
    public void createTransfer() throws Exception {
        TransferDAO transferDAO = new TransferDAO();
        TransferEntity transferEntity = new TransferEntity("1", "2", 3);
        transferDAO.createTransfer(transferEntity);
        assertTrue(transferEntity.getId() != null);
    }
}