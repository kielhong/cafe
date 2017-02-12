package com.widehouse.cafe.domain.board;

import com.widehouse.cafe.domain.cafe.Cafe;

/**
 * Created by kiel on 2017. 2. 11..
 */
public class Board {
    private Long id;

    private Cafe cafe;

    private String name;

    public Board(Cafe cafe, String name) {
        this.cafe = cafe;
        this.name = name;
    }
}
