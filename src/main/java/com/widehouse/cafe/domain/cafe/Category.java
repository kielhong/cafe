package com.widehouse.cafe.domain.cafe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Category {
    @Id @GeneratedValue
    private Long id;

    private String name;

    public Category(String name) {
        this.name = name;
    }
}
