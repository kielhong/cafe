package com.widehouse.cafe.domain.cafe;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PRIVATE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import groovy.util.logging.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Slf4j
public class CafeTest {
    private Cafe cafe;
    private Category category;

    @Before
    public void setUp() {
        category = new Category(1L, "category");
        cafe = new Cafe("testcafe", "testcafe");
    }

    @Test
    public void createCafe() {
        Cafe cafe = new Cafe("cafeurl", "cafename", "desc", CafeVisibility.PUBLIC, category);

        assertThat(cafe)
                .hasFieldOrPropertyWithValue("url", "cafeurl")
                .hasFieldOrPropertyWithValue("name", "cafename")
                .hasFieldOrPropertyWithValue("description", "desc")
                .hasFieldOrPropertyWithValue("visibility", CafeVisibility.PUBLIC)
                .hasFieldOrPropertyWithValue("category", category)
                .hasFieldOrProperty("statistics");
    }

    @Test
    public void updateCafeInfo_thenChangeCafeInfo() {
        cafe.updateInfo("new name", "new description", CafeVisibility.PRIVATE, category);

        then(cafe)
                .hasFieldOrPropertyWithValue("name", "new name")
                .hasFieldOrPropertyWithValue("description", "new description")
                .hasFieldOrPropertyWithValue("visibility", PRIVATE)
                .hasFieldOrPropertyWithValue("category", category);
    }

//    @Test
//    public void boards_list_by_listOrder() {
//        // given
//        Board board1 = new Board(cafe, "board1", 4);
//        Board board2 = new Board(cafe, "board2", 3);
//        Board board3 = new Board(cafe, "board3", 2);
//        Board board4 = new Board(cafe, "board4", 1);
//        cafe.getBoards().add(board1);
//        cafe.getBoards().add(board2);
//        cafe.getBoards().add(board3);
//        cafe.getBoards().add(board4);
//        // when
//        List<Board> boards = cafe.getBoards();
//        // then
//        then(boards)
//                .hasSize(4)
//                .contains(board4, board3, board2, board1);
//    }
}
