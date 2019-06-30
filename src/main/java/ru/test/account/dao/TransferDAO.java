package ru.test.account.dao;

import ru.test.account.model.TransferEntity;

import java.util.concurrent.atomic.AtomicInteger;

public class TransferDAO {

    private AtomicInteger counter = new AtomicInteger();


    public void createTransfer(TransferEntity transferEntity) {
        transferEntity.setId(counter.incrementAndGet());
    }

    public void updateStatus(TransferEntity transferEntity) {
    }
}
