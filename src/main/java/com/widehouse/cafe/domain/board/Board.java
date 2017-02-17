package com.widehouse.cafe.domain.board;

import com.widehouse.cafe.domain.cafe.Cafe;
import lombok.Getter;
import lombok.ToString;

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

@Getter
@ToString(exclude = "cafe")
public class Board {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Cafe cafe;

    @Size(min = 1, max = 30)
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
}
