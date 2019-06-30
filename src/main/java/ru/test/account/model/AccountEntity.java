package ru.test.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.eclipse.persistence.annotations.Index;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Data
@NoArgsConstructor
@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "name", columnNames = {"name"}))
public class AccountEntity {

    @Id
    @GeneratedValue
    private Integer id;

    @Index(unique = true)
    private String name;

    private Integer sum;

    public AccountEntity(String name, Integer sum) {
        this.name = name;
        this.sum = sum;
    }
}
