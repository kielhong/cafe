package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

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
    public void findAllByCafe_thenListBoardOrderByListOrder() {
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

        List<Board> boards = boardRepository.findAllByCafe(cafe, new Sort(Sort.Direction.ASC, "listOrder"));

        then(boards)
                .containsExactly(board4, board3, board2, board1);
    }
}