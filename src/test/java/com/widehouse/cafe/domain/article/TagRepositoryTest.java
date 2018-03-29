package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 3. 10..
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class TagRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TagRepository tagRepository;

    private Cafe cafe;
    private Board board;
    private Member member;

    @Before
    public void init() {
        cafe = new Cafe("testurl", "testname");
        entityManager.persist(cafe);
        board = new Board(cafe, "board");
        entityManager.persist(board);
        member = new Member("member", "password", "nickname", "foo@bar.com");
        entityManager.persist(member);
    }

    @Test
    public void findTagByNameTest() {
        // given
        entityManager.persist(new Tag("tagname"));
        // when
        Tag result = tagRepository.findByName("tagname");
        // then
        then(result)
                .hasFieldOrPropertyWithValue("name", "tagname");
    }

    @Test
    public void findAllByCafe_Should_ListDistinctTagsByCafe() {
        Tag tag1 = new Tag("tag1");
        entityManager.persist(tag1);
        Tag tag2 = new Tag("tag2");
        entityManager.persist(tag2);
        Tag tag3 = new Tag("tag3");
        entityManager.persist(tag3);

        Article article1 = new Article(board, member, "title1", "content1");
        article1.getTags().addAll(Arrays.asList(tag1, tag2));
        entityManager.persist(article1);
        Article article2 = new Article(board, member, "title2", "content2");
        article1.getTags().addAll(Arrays.asList(tag2, tag3));
        entityManager.persist(article2);
        // when
        List<Tag> tags = tagRepository.findAllByCafe(cafe);
        // then
        then(tags)
                .containsOnlyOnce(tag1, tag2, tag3);
    }

    @Test
    public void findArticlesByCafeAndTag_Should_ListArticlesByCafeAndTag() {
        Tag tag1 = new Tag("tag1");
        entityManager.persist(tag1);

        Article article1 = new Article(board, member, "title1", "content1");
        article1.getTags().addAll(Arrays.asList(tag1));
        entityManager.persist(article1);
        Article article2 = new Article(board, member, "title2", "content2");
        article2.getTags().addAll(Arrays.asList(tag1));
        entityManager.persist(article2);
        // when
        List<Article> articles = tagRepository.findArticlesByCafeAndTag(cafe, tag1);
        // then
        then(articles)
                .containsOnlyOnce(article1, article2);
    }
}