package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.BoardRepository;
import com.widehouse.cafe.domain.cafe.Cafe;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 16..
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class BoardRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void findAllByCafe_should_list_order_by_listOrder() {
        // given
        Cafe cafe = new Cafe("testurl", "testname");
        entityManager.persist(cafe);
        Board board1 = new Board(cafe, "board1", 4);
        entityManager.persist(board1);
        Board board2 = new Board(cafe, "board2", 3);
        entityManager.persist(board2);
        Board board3 = new Board(cafe, "board3", 2);
        entityManager.persist(board3);
        Board board4 = new Board(cafe, "board4", 1);
        entityManager.persist(board4);
        // when
        List<Board> boards = boardRepository.findAllByCafe(cafe, new Sort(Sort.Direction.ASC, "listOrder"));
        // then
        then(boards)
                .hasSize(4)
                .containsExactly(board4, board3, board2, board1);
    }
}
