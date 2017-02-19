package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 19..
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ArticleRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ArticleRepository articleRepository;

    private Cafe cafe;
    private Board board;
    private Member writer;

    @Before
    public void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        entityManager.persist(cafe);
        board = new Board(cafe, "board");
        entityManager.persist(board);
        writer = new Member("writer");
        entityManager.persist(writer);
    }

    @Test
    public void findByCafe_Should_Return_ListArticle() {
        // given
        Article article1 = new Article(cafe, board, writer, "test article1", "test1");
        entityManager.persist(article1);
        Article article2 = new Article(cafe, board, writer, "test article2", "test2");
        entityManager.persist(article2);
        Article article3 = new Article(cafe, board, writer, "test article3", "test3");
        entityManager.persist(article3);
        // when
        List<Article> articles = articleRepository.findByCafe(cafe, new PageRequest(0, 2, new Sort(DESC, "id")));
        // then
        then(articles)
                .containsExactly(article3, article2);
    }
}
