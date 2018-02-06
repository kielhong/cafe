package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.BDDAssertions.then;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.projection.ArticleProjection;

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
    private Board board1;
    private Board board2;
    private Member writer;

    private Article article1;
    private Article article2;
    private Article article3;

    @Before
    public void init() {
        cafe = new Cafe("testurl", "testcafe");
        entityManager.persist(cafe);
        board1 = new Board(cafe, "board1");
        entityManager.persist(board1);
        board2 = new Board(cafe, "board2");
        entityManager.persist(board2);
        writer = new Member("writer");
        entityManager.persist(writer);

        article1 = new Article(board1, writer, "test article1", "test1");
        entityManager.persist(article1);
        article2 = new Article(board1, writer, "test article2", "test2");
        entityManager.persist(article2);
        article3 = new Article(board2, writer, "test article3", "test3");
        entityManager.persist(article3);
    }

    @Test
    public void findByCafe_Should_Return_ListArticle() {
        // when
        List<ArticleProjection> articles = articleRepository.findByBoardCafe(cafe, PageRequest.of(0, 2, new Sort(DESC, "id")));
        // then
        then(articles)
                .hasSize(2)
                .extracting("id")
                .containsExactly(article3.getId(), article2.getId());
    }

    @Test
    public void findByBoard_Should_Return_ListArticle() {
        // when
        List<Article> articles = articleRepository.findByBoard(board1, PageRequest.of(0, 3, new Sort(DESC, "id")));
        // then
        then(articles)
                .containsExactly(article2, article1);
    }

    @Test
    public void saveArticle_WhenAddTag_Should_SaveTag() {
        // given
        Article article = new Article(board1, writer, "test article1", "test1");
        entityManager.persist(article);
        Tag tag = new Tag("tag");
        tag = entityManager.persist(tag);
        // when
        article.getTags().add(tag);
        articleRepository.save(article);
        tag.getArticles().add(article);
        entityManager.persist(tag);
        // then
        Tag result = entityManager.find(Tag.class, tag.getId());
        then(result.getArticles())
                .contains(article);
    }
}