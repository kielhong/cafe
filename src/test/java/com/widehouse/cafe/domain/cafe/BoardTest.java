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
    public void modifyBoard_thenChangeBoardInfo() {
        Board board = new Board(cafe, "test board", 1);

        board.update("new board name", 2);

        then(board)
                .hasFieldOrPropertyWithValue("name", "new board name")
                .hasFieldOrPropertyWithValue("listOrder", 2);
    }

    @Test
    public void isSpecialType_withTAG_thenTrue() {
        Board board = new Board(cafe, "test board", TAG, 1);

        boolean isSpecialType = board.isSpecialType();

        then(isSpecialType).isTrue();
    }

    @Test
    public void isSpecialType_withBOOK_thenTrue() {
        Board board = new Board(cafe, "test board", BOOK, 1);

        boolean isSpecialType = board.isSpecialType();

        then(isSpecialType).isTrue();
    }

    @Test
    public void isSpecialType_withBEST_thenTrue() {
        Board board = new Board(cafe, "test board", BEST, 1);

        boolean isSpecialType = board.isSpecialType();

        then(isSpecialType).isTrue();
    }

    @Test
    public void isSpecialType_withCALENDAR_thenTrue() {
        Board board = new Board(cafe, "test board", CALENDAR, 1);

        boolean isSpecialType = board.isSpecialType();

        then(isSpecialType).isTrue();
    }

    @Test
    public void isSpecialType_withLIST_thenFalse() {
        Board board = new Board(cafe, "test board", LIST, 1);

        then(board.getType().isSpecialType()).isFalse();
    }

}
