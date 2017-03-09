package com.widehouse.cafe.domain.cafe;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Entity

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Board {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Cafe cafe;

    @Size(min = 1, max = 50)
    private String name;

    @Min(1)
    private int listOrder;

    public Board(Cafe cafe, String name, int listOrder) {
        this.cafe = cafe;
        this.name = name;
        this.listOrder = listOrder;
    }

    public Board(Cafe cafe, String name) {
        this.cafe = cafe;
        this.name = name;
        this.listOrder = 1;
    }

    public void update(String name, int listOrder) {
        this.name = name;
        this.listOrder = listOrder;
    }
}
