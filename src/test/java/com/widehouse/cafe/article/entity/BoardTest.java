package com.widehouse.cafe.article.entity;

import static com.widehouse.cafe.article.entity.BoardType.BEST;
import static com.widehouse.cafe.article.entity.BoardType.BOOK;
import static com.widehouse.cafe.article.entity.BoardType.CALENDAR;
import static com.widehouse.cafe.article.entity.BoardType.LIST;
import static com.widehouse.cafe.article.entity.BoardType.TAG;
import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.cafe.entity.Cafe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created by kiel on 2017. 2. 17..
 */
class BoardTest {
    private Cafe cafe;

    @BeforeEach
    void setup() {
        cafe = new Cafe("testurl", "testcafe");
    }

    @Test
    void modifyBoard_thenChangeBoardInfo() {
        Board board = new Board(cafe, "test board", 1);

        board.update("new board name", 2);

        then(board)
                .hasFieldOrPropertyWithValue("name", "new board name")
                .hasFieldOrPropertyWithValue("listOrder", 2);
    }

    @Test
    void isSpecialType_withTagType_thenTrue() {
        Board board = new Board(cafe, "test board", TAG, 1);

        boolean isSpecialType = board.isSpecialType();

        then(isSpecialType).isTrue();
    }

    @Test
    void isSpecialType_withBookType_thenTrue() {
        Board board = new Board(cafe, "test board", BOOK, 1);

        boolean isSpecialType = board.isSpecialType();

        then(isSpecialType).isTrue();
    }

    @Test
    void isSpecialType_withBestType_thenTrue() {
        Board board = new Board(cafe, "test board", BEST, 1);

        boolean isSpecialType = board.isSpecialType();

        then(isSpecialType).isTrue();
    }

    @Test
    void isSpecialType_withCalendarType_thenTrue() {
        Board board = new Board(cafe, "test board", CALENDAR, 1);

        boolean isSpecialType = board.isSpecialType();

        then(isSpecialType).isTrue();
    }

    @Test
    void isSpecialType_withListType_thenFalse() {
        Board board = new Board(cafe, "test board", LIST, 1);

        then(board.getType().isSpecialType()).isFalse();
    }

}
