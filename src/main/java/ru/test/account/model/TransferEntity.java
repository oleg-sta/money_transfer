package ru.test.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
public class TransferEntity {
    @Id
    @GeneratedValue
    private Integer id;

    private String account1;
    private String account2;
    private int amount;
    private int status = 0;
    private Date date = new Date();

    public TransferEntity(String account1, String account2, int amount) {
        this.account1 = account1;
        this.account2 = account2;
        this.amount = amount;
    }
}
