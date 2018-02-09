package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.projection.CafeProjection;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

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

    private Category category1;
    private Cafe cafe1;
    private Cafe cafe2;
    private Cafe cafe3;

    @Before
    public void setUp() {
        category1 = new Category("category1", 1);
        entityManager.persist(category1);

        cafe1 = new Cafe("test1", "test1", "", CafeVisibility.PUBLIC, category1);
        cafe2 = new Cafe("test2", "test2", "", CafeVisibility.PUBLIC, category1);
        cafe3 = new Cafe("test3", "test3", "", CafeVisibility.PUBLIC, category1);
        entityManager.persist(cafe1);
        entityManager.persist(cafe2);
        entityManager.persist(cafe3);
    }

    @Test
    public void findByCategory_thenListCafeByCategory() {
        List<Cafe> cafes = cafeRepository.findByCategory(category1);

        assertThat(cafes)
                .containsOnly(cafe1, cafe2, cafe3);
    }

    @Test
    public void findByCategoryId_thenListCafeByCategory() {
        List<Cafe> cafes = cafeRepository.findByCategoryId(category1.getId());

        assertThat(cafes)
                .containsOnly(cafe1, cafe2, cafe3);
    }

    @Test
    public void findByCategory_withPaging_thenCafeListByCategoryPagingAndSorted() {
        cafe1.getStatistics().setCafeMemberCount(10L);
        cafe2.getStatistics().setCafeMemberCount(5L);
        cafe3.getStatistics().setCafeMemberCount(2L);

        List<CafeProjection> cafes = cafeRepository.findByCategory(category1,
                PageRequest.of(0, 3, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount")));

        assertThat(cafes)
                .extracting("cafeMemberCount")
                .containsExactly(10L, 5L, 2L);
    }

    @Test
    public void findByCategoryId_withPaging_thenCafeListByCategoryPagingAndSorted() {
        cafe1.getStatistics().setCafeMemberCount(10L);
        cafe2.getStatistics().setCafeMemberCount(5L);
        cafe3.getStatistics().setCafeMemberCount(2L);

        List<CafeProjection> cafes = cafeRepository.findByCategoryId(category1.getId(),
                PageRequest.of(0, 3, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount")));

        assertThat(cafes)
                .extracting("cafeMemberCount")
                .containsExactly(10L, 5L, 2L);
    }

//    @Test
//    public void boards_list_by_listOrder() {
//        // given
//        Board board1 = new Board(cafe1, "board1", 4);
//        entityManager.persist(board1);
//        Board board2 = new Board(cafe1, "board2", 3);
//        entityManager.persist(board2);
//        Board board3 = new Board(cafe1, "board3", 2);
//        entityManager.persist(board3);
//        Board board4 = new Board(cafe1, "board4", 1);
//        entityManager.persist(board4);
//        cafe1.getBoards().add(board1);
//        cafe1.getBoards().add(board2);
//        cafe1.getBoards().add(board3);
//        cafe1.getBoards().add(board4);
//        entityManager.persist(cafe1);
//        entityManager.refresh(cafe1);
//        // when
//        List<Board> boards = cafe1.getBoards();
//        // then
//        then(boards)
//                .hasSize(4)
//                .containsExactly(board4, board3, board2, board1);
//    }

    @Test
    public void findCafeByUrlTest() {
        Cafe cafe = cafeRepository.findByUrl("test1");

        then(cafe).isEqualTo(cafe1);
    }
}
