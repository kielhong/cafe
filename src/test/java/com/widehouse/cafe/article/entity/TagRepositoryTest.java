package com.widehouse.cafe.article.entity;

import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.user.entity.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Created by kiel on 2017. 3. 10..
 */
@DataJpaTest
class TagRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TagRepository tagRepository;

    private Cafe cafe;
    private Board board;
    private User member;

    @BeforeEach
    void init() {
        cafe = new Cafe("testurl", "testname");
        entityManager.persist(cafe);
        board = Board.builder().cafe(cafe).name("board").build();
        entityManager.persist(board);
        member = new User("user", "password");
        entityManager.persist(member);
    }

    @Test
    void findTagByNameTest() {
        // given
        Tag tag = entityManager.persist(new Tag("tagname"));
        // when
        Optional<Tag> result = tagRepository.findByName("tagname");
        // then
        then(result)
                .isPresent()
                .hasValue(tag);
    }

    @Test
    void findAllByCafe_Should_ListDistinctTagsByCafe() {
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
    void findArticlesByCafeAndTag_Should_ListArticlesByCafeAndTag() {
        Tag tag1 = new Tag("tag1");
        entityManager.persist(tag1);

        Article article1 = new Article(board, member, "title1", "content1");
        article1.getTags().addAll(Collections.singletonList(tag1));
        entityManager.persist(article1);
        Article article2 = new Article(board, member, "title2", "content2");
        article2.getTags().addAll(Collections.singletonList(tag1));
        entityManager.persist(article2);
        // when
        List<Article> articles = tagRepository.findArticlesByCafeAndTag(cafe, tag1);
        // then
        then(articles)
                .containsOnlyOnce(article1, article2);
    }
}