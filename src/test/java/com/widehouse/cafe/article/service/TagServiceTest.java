package com.widehouse.cafe.article.service;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.ArticleRepository;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.article.entity.Tag;
import com.widehouse.cafe.article.entity.TagRepository;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.user.entity.User;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Created by kiel on 2017. 3. 10..
 */
@ExtendWith(MockitoExtension.class)
class TagServiceTest {
    private TagService service;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private ArticleRepository articleRepository;

    private User user;
    private Cafe cafe;
    private Board board;
    private Article article;
    private Tag tag;

    @BeforeEach
    void setup() {
        service = new TagService(tagRepository, articleRepository);

        user = new User(1L, "user", "password");
        cafe = new Cafe("testurl", "testname");
        board = Board.builder().cafe(cafe).name("board").build();
        article = new Article(board, user, "test", "test");
        tag = new Tag("tag");
    }

    @Test
    void getTagsByCafe_thenListTagsOfCafe() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        given(tagRepository.findAllByCafe(cafe))
                .willReturn(Arrays.asList(tag1, tag2));

        List<Tag> tags = service.getTagsByCafe(cafe);

        then(tags)
                .contains(tag1, tag2);
    }

    @Test
    void getTagsByName_thenTagWithName() {
        // given
        given(tagRepository.findByName(anyString()))
                .willReturn(Optional.of(tag));
        // when
        Optional<Tag> result = service.getTagByName("tag");
        // then
        then(result)
                .isPresent()
                .hasValue(tag);
    }

    @Test
    void getTagsByName_whenUnmatchedTagName_thenReturnNull() {
        // given
        given(tagRepository.findByName(anyString()))
                .willReturn(Optional.empty());
        // when
        Optional<Tag> result = service.getTagByName(" tag  ");
        // then
        then(result).isNotPresent();
    }

    @Test
    void getArticlesByTag_whenCafeAndTag_thenReturnArticleHasTagInCafe() {
        Article article1 = new Article(board, user, "title1", "content1");
        Article article2 = new Article(board, user, "title2", "content2");
        given(tagRepository.findArticlesByCafeAndTag(cafe, tag))
                .willReturn(Arrays.asList(article1, article2));

        List<Article> articles = service.getArticlesByTag(cafe, tag);

        then(articles)
                .containsOnlyOnce(article1, article2);
    }

    @Test
    void updateTagsOfArticle_thenUpdateTags() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        article.getTags().addAll(singletonList(tag1));
        given(tagRepository.findByName("tag2"))
            .willReturn(Optional.of(tag2));

        service.updateTagsOfArticle(article, singletonList(tag2));

        then(article.getTags())
                .contains(tag2);
        verify(articleRepository).save(article);
    }

    @Test
    void updateTagsOfArticle_whenNotSavedTag_thenSaveTagAndUpdateTags() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        article.getTags().addAll(singletonList(tag1));
        given(tagRepository.findByName("tag2"))
                .willReturn(Optional.empty());
        given(tagRepository.save(tag2))
                .willReturn(tag2);
        // when
        service.updateTagsOfArticle(article, singletonList(tag2));
        // then
        then(article.getTags())
                .contains(tag2);
        verify(tagRepository).save(tag2);
        verify(articleRepository).save(article);
    }
}