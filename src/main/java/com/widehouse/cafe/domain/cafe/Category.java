package com.widehouse.cafe.domain.cafe;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Category {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
        this(name);
        this.id = id;
    }

    public Category(String name) {
        this(name, 1);
    }
}
