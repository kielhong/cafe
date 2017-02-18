package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 17..
 */
public class BoardTest {

    @Test
    public void modifyBoard_should_change_board_info() {
        // given
        Cafe cafe = new Cafe("test_url", "test cafe");
        Board board = new Board(cafe, "test board", 1);
        // when
        board.update("new board name", 2);
        // then
        then(board)
                .hasFieldOrPropertyWithValue("name", "new board name")
                .hasFieldOrPropertyWithValue("listOrder", 2);
    }
}
