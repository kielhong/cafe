package com.widehouse.cafe.domain.cafe;

import static com.widehouse.cafe.domain.cafe.BoardType.BEST;
import static com.widehouse.cafe.domain.cafe.BoardType.BOOK;
import static com.widehouse.cafe.domain.cafe.BoardType.CALENDAR;
import static com.widehouse.cafe.domain.cafe.BoardType.LIST;
import static com.widehouse.cafe.domain.cafe.BoardType.TAG;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 17..
 */
public class BoardTest {
    private Cafe cafe;

    @Before
    public void setup() {
        cafe = new Cafe("testurl", "testcafe");
    }

    @Test
    public void modifyBoard_should_change_board_info() {
        // given
        Board board = new Board(cafe, "test board", 1);
        // when
        board.update("new board name", 2);
        // then
        then(board)
                .hasFieldOrPropertyWithValue("name", "new board name")
                .hasFieldOrPropertyWithValue("listOrder", 2);
    }

    @Test
    public void isSpecialType_WhenTAGorBOOKorCALENDARorBEST_Should_True() {
        // given
        Board board = new Board(cafe, "test board", TAG, 1);
        // when
        boolean isSpecialType = board.isSpecialType();
        // then
        then(isSpecialType)
                .isTrue();
        // given
        board = new Board(cafe, "test board", BOOK, 1);
        // when
        isSpecialType = board.isSpecialType();
        // then
        then(isSpecialType)
                .isTrue();
        // given
        board = new Board(cafe, "test board", CALENDAR, 1);
        // when
        isSpecialType = board.isSpecialType();
        // then
        then(isSpecialType)
                .isTrue();
        // given
        board = new Board(cafe, "test board", BEST, 1);
        // when
        isSpecialType = board.isSpecialType();
        // then
        then(isSpecialType)
                .isTrue();
    }

    @Test
    public void isSpecialType_WhenLIST_Should_False() {
        // given
        Board board = new Board(cafe, "test board", LIST, 1);
        // when
        boolean isSpecialType = board.isSpecialType();
        // then
        then(isSpecialType)
                .isFalse();
    }

}
