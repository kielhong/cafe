package com.widehouse.cafe.domain.cafe;

import static com.widehouse.cafe.domain.cafe.BoardType.BEST;
import static com.widehouse.cafe.domain.cafe.BoardType.BOOK;
import static com.widehouse.cafe.domain.cafe.BoardType.CALENDAR;
import static com.widehouse.cafe.domain.cafe.BoardType.LIST;
import static com.widehouse.cafe.domain.cafe.BoardType.TAG;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonBackReference
    private Cafe cafe;

    @Size(min = 1, max = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    private BoardType type;

    private int listOrder;

    public Board(Cafe cafe ,String name, BoardType type, int listOrder) {
        this.cafe = cafe;
        this.name = name;
        this.type = type;
        this.listOrder = listOrder;
    }
    public Board(Cafe cafe, String name, int listOrder) {
        this(cafe, name, LIST, listOrder);
    }

    public Board(Cafe cafe, String name) {
        this(cafe, name, 1);
    }

    public void update(String name, int listOrder) {
        this.name = name;
        this.listOrder = listOrder;
    }

    public boolean isSpecialType() {
        return type.specialType;
    }
}
