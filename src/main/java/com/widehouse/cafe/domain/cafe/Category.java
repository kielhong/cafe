package com.widehouse.cafe.domain.cafe;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private LocalDateTime createDateTime;

    private int listOrder;

    public Category(String name, int listOrder) {
        this.name = name;
        this.listOrder = listOrder;
        this.createDateTime = LocalDateTime.now();
    }

    public Category(Long id, String name) {
        this.id = id;
        this.name = name;
        this.listOrder = 1;

    }
}
