package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class CafeRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CafeRepository cafeRepository;

    private CafeCategory category1;
    private CafeCategory category2;
    private Cafe cafe1;
    private Cafe cafe2;
    private Cafe cafe3;
    private Cafe cafe4;
    private Cafe cafe5;
    private Cafe cafe6;

    @Before
    public void setUp() {
        category1 = new CafeCategory("test1");
        category2 = new CafeCategory("test2");
        entityManager.persist(category1);
        entityManager.persist(category2);

        cafe1 = new Cafe("test1", "test1", "", CafeVisibility.PUBLIC, category1);
        cafe2 = new Cafe("test2", "test2", "", CafeVisibility.PUBLIC, category1);
        cafe3 = new Cafe("test3", "test3", "", CafeVisibility.PUBLIC, category1);
        cafe4 = new Cafe("test4", "test4", "", CafeVisibility.PUBLIC, category2);
        cafe5 = new Cafe("test5", "test5", "", CafeVisibility.PUBLIC, category2);
        cafe6 = new Cafe("test6", "test6", "", CafeVisibility.PUBLIC, category2);
        entityManager.persist(cafe1);
        entityManager.persist(cafe2);
        entityManager.persist(cafe3);
        entityManager.persist(cafe4);
        entityManager.persist(cafe5);
        entityManager.persist(cafe6);
    }

    @Test
    public void cafeList_by_category() {
        // when
        List<Cafe> cafes = cafeRepository.findByCategory(category1);
        // then
        assertThat(cafes)
                .hasSize(3)
                .containsOnly(cafe1, cafe2, cafe3)
                .doesNotContain(cafe4, cafe5, cafe6);
    }

    @Test
    public void cafeList_by_categoryId() {
        // when
        List<Cafe> cafes = cafeRepository.findByCategoryId(category1.getId());
        // then
        assertThat(cafes)
                .hasSize(3)
                .containsOnly(cafe1, cafe2, cafe3)
                .doesNotContain(cafe4, cafe5, cafe6);
    }

    @Test
    public void cafeList_by_category_with_paging_sorting() {
        // given
        cafe1.getStatistics().setCafeMemberCount(10L);
        cafe2.getStatistics().setCafeMemberCount(5L);
        cafe3.getStatistics().setCafeMemberCount(2L);
        // when
        Pageable pageable = new PageRequest(0, 3, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount"));
        List<Cafe> cafes = cafeRepository.findByCategory(category1, pageable);
        // then
        assertThat(cafes)
                .hasSize(3)
                .containsExactly(cafe1, cafe2, cafe3)
                .doesNotContain(cafe4, cafe5, cafe6);
    }

    @Test
    public void boards_list_by_listOrder() {
        // given
        Board board1 = new Board(cafe1, "board1", 4);
        entityManager.persist(board1);
        Board board2 = new Board(cafe1, "board2", 3);
        entityManager.persist(board2);
        Board board3 = new Board(cafe1, "board3", 2);
        entityManager.persist(board3);
        Board board4 = new Board(cafe1, "board4", 1);
        entityManager.persist(board4);
        cafe1.getBoards().add(board1);
        cafe1.getBoards().add(board2);
        cafe1.getBoards().add(board3);
        cafe1.getBoards().add(board4);
        entityManager.persist(cafe1);
        entityManager.refresh(cafe1);
        // when
        List<Board> boards = cafe1.getBoards();
        // then
        then(boards)
                .hasSize(4)
                .containsExactly(board4, board3, board2, board1);
    }
}
