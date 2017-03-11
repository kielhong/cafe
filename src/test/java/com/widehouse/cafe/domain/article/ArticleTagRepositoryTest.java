package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by kiel on 2017. 3. 10..
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class ArticleTagRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ArticleTagRepository articleTagRepository;

    private Cafe cafe;
    private Board board;
    private Member member;
    private Article article;
    private Tag tag1;
    private Tag tag2;

    @Before
    public void setup() {
        cafe = new Cafe("testurl", "testname");
        entityManager.persist(cafe);
        board = new Board(cafe, "testboard");
        entityManager.persist(board);
        member = new Member("member");
        entityManager.persist(member);
        article = new Article(board, member, "title", "content");
        entityManager.persist(article);

        tag1 = new Tag("tag1");
        tag2 = new Tag("tag2");
        ArticleTag at1 = new ArticleTag(article, tag1);
        ArticleTag at2 = new ArticleTag(article, tag2);

        article.getArticleTags().add(at1);
        article.getArticleTags().add(new ArticleTag(article, tag2));
        entityManager.persist(article);

        tag1.getArticleTags().add(at1);
        tag2.getArticleTags().add(at2);
        entityManager.persist(tag1);
        entityManager.persist(tag2);
    }

    @Test
    public void findAllByCafe_Should_ListAllTagsByCafe() {
        // when
        List<Tag> tags = articleTagRepository.findTagsByCafe(cafe);
        // then
        then(tags)
                .contains(tag1, tag2);
    }

    @Test
    public void findArticlesByCafeAndTag_Should_ListArticlesByCafeandTag() {
        // given
        Article article3 = new Article(board, member, "title", "content");
        entityManager.persist(article3);
        ArticleTag at3 = new ArticleTag(article3, tag1);
        tag1.getArticleTags().add(at3);
        entityManager.persist(at3);
        entityManager.persist(tag1);

        // when
        List<Article> articles = articleTagRepository.findArticlesByCafeAndTag(cafe, tag1);
        // then
        then(articles)
                .contains(article, article3);
    }
}
