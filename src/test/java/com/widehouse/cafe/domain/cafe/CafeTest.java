package com.widehouse.cafe.domain.cafe;

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

    @Before
    public void setUp() {
        cafe = new Cafe("testcafe", "testcafe");
    }

    @Test
    public void createCafe() {
        // Given
        Category category = new Category();
        // When
        Cafe cafe = new Cafe("cafeurl", "cafename", "desc", CafeVisibility.PUBLIC, category);
        // Then
        assertThat(cafe)
                .hasFieldOrPropertyWithValue("url", "cafeurl")
                .hasFieldOrPropertyWithValue("name", "cafename")
                .hasFieldOrPropertyWithValue("description", "desc")
                .hasFieldOrPropertyWithValue("visibility", CafeVisibility.PUBLIC)
                .hasFieldOrPropertyWithValue("category", category)
                .hasFieldOrProperty("statistics");
        System.out.println("cafe" + cafe);
    }

    @Test
    public void updateCafeInfo_should_change_cafeName_description_visibility() {
        // When
        Category category = new Category("category");
        cafe.updateInfo("new name", "new description", CafeVisibility.PRIVATE, category);
        // Then
        assertThat(cafe.getName())
                .isEqualTo("new name");
        assertThat(cafe.getDescription())
                .isEqualTo("new description");
        assertThat(cafe.getVisibility())
                .isEqualTo(CafeVisibility.PRIVATE);
        assertThat(cafe.getCategory())
                .isEqualTo(category);
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
