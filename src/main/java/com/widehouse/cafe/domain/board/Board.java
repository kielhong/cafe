package com.widehouse.cafe.domain.board;

import com.widehouse.cafe.domain.cafe.Cafe;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Entity

//@Getter
public class Board {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Cafe cafe;

    private String name;

    public Board(Cafe cafe, String name) {
        this.cafe = cafe;
        this.name = name;
    }
}
