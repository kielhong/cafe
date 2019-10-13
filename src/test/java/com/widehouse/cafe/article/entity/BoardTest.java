package com.widehouse.cafe.article.entity;

import static com.widehouse.cafe.article.entity.BoardType.BEST;
import static com.widehouse.cafe.article.entity.BoardType.BOOK;
import static com.widehouse.cafe.article.entity.BoardType.CALENDAR;
import static com.widehouse.cafe.article.entity.BoardType.LIST;
import static com.widehouse.cafe.article.entity.BoardType.TAG;
import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.cafe.entity.Cafe;

import java.util.stream.Stream;

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
    void builder() {
        Board board = Board.builder().cafe(cafe).name("board").type(LIST).listOrder(1).build();
        // then
        then(board)
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("name", "board")
                .hasFieldOrPropertyWithValue("type", LIST)
                .hasFieldOrPropertyWithValue("listOrder", 1);
    }

    @Test
    void builder_defaultValue() {
        Board board = Board.builder().cafe(cafe).name("board").build();
        // then
        then(board)
                .hasFieldOrPropertyWithValue("type", LIST)
                .hasFieldOrPropertyWithValue("listOrder", 1);
    }

    @Test
    void updateBoardInfo_ThenChangeBoardInfo() {
        // given
        Board board = Board.builder().cafe(cafe).name("test board").listOrder(1).build();
        // when
        board.update("new board name", 2);
        // then
        then(board)
                .hasFieldOrPropertyWithValue("name", "new board name")
                .hasFieldOrPropertyWithValue("listOrder", 2);
    }

    @Test
    void isSpecialType_withTagType_thenTrue() {
        Stream.of(TAG, BOOK, BEST)
                .forEach(boardType -> {
                    Board board = Board.builder().cafe(cafe).name("test board").type(boardType).listOrder(1).build();
                    boolean isSpecialType = board.isSpecialType();
                    then(isSpecialType).isTrue();
                });
    }

    @Test
    void isSpecialType_withBookType_thenTrue() {
        // given
        Board board = Board.builder().cafe(cafe).name("test board").type(BOOK).listOrder(1).build();
        // when
        boolean isSpecialType = board.isSpecialType();
        // then
        then(isSpecialType).isTrue();
    }

    @Test
    void isSpecialType_withBestType_thenTrue() {
        Board board = Board.builder().cafe(cafe).name("test board").type(BEST).listOrder(1).build();

        boolean isSpecialType = board.isSpecialType();

        then(isSpecialType).isTrue();
    }

    @Test
    void isSpecialType_withCalendarType_thenTrue() {
        Board board = Board.builder().cafe(cafe).name("test board").type(CALENDAR).listOrder(1).build();

        boolean isSpecialType = board.isSpecialType();

        then(isSpecialType).isTrue();
    }

    @Test
    void isSpecialType_withListType_thenFalse() {
        Board board = Board.builder().cafe(cafe).name("test board").type(LIST).listOrder(1).build();

        then(board.getType().isSpecialType()).isFalse();
    }

}
