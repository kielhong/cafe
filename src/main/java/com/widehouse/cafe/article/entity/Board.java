package com.widehouse.cafe.article.entity;

import static com.widehouse.cafe.article.entity.BoardType.LIST;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.widehouse.cafe.cafe.entity.Cafe;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Cafe cafe;

    @Size(min = 1, max = 50)
    private String name;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private BoardType type = LIST;

    // list order in cafe
    @Builder.Default
    private int listOrder = 1;

    public void update(String name, int listOrder) {
        this.name = name;
        this.listOrder = listOrder;
    }

    public boolean isSpecialType() {
        return type.specialType;
    }
}
