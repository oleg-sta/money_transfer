package ru.test.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    private Integer id;

    private String name;

    private Integer sum;

    public AccountEntity(String name, Integer sum) {
        this.name = name;
        this.sum = sum;
    }
}
