package com.widehouse.cafe.article.entity;

import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.cafe.entity.Cafe;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Sort;

/**
 * Created by kiel on 2017. 2. 16..
 */
@DataJpaTest
class BoardRepositoryTest {
    @Autowired
    TestEntityManager entityManager;

    @Autowired
    private BoardRepository boardRepository;

    @Test
    void findAllByCafe_thenListBoardOrderByListOrder() {
        Cafe cafe = new Cafe("testurl", "testname");
        entityManager.persist(cafe);
        Board board1 = Board.builder().cafe(cafe).name("board1").listOrder(4).build();
        entityManager.persist(board1);
        Board board2 = Board.builder().cafe(cafe).name("board2").listOrder(3).build();
        entityManager.persist(board2);
        Board board3 = Board.builder().cafe(cafe).name("board3").listOrder(2).build();
        entityManager.persist(board3);
        Board board4 = Board.builder().cafe(cafe).name("board4").listOrder(1).build();
        entityManager.persist(board4);

        List<Board> boards = boardRepository.findAllByCafe(cafe, Sort.by("listOrder"));

        then(boards)
                .containsExactly(board4, board3, board2, board1);
    }
}