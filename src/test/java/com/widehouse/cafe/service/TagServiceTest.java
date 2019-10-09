package com.widehouse.cafe.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.article.TagRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.domain.member.Member;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created by kiel on 2017. 3. 10..
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TagService.class)
public class TagServiceTest {
    @Autowired
    private TagService tagService;
    @MockBean
    private TagRepository tagRepository;
    @MockBean
    private ArticleRepository articleRepository;

    private Member member;
    private Cafe cafe;
    private Board board;
    private Article article;
    private Tag tag;

    @BeforeEach
    public void setup() {
        member = new Member(1L, "member", "password", "nickname", "foo@bar.com");
        cafe = new Cafe("testurl", "testname");
        board = new Board(cafe, "board");
        article = new Article(board, member, "test", "test");
        tag = new Tag("tag");

        given(tagRepository.findByName("tag"))
                .willReturn(tag);
    }

    @Test
    public void getTagsByCafe_thenListTagsOfCafe() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        given(tagRepository.findAllByCafe(cafe))
                .willReturn(Arrays.asList(tag1, tag2));

        List<Tag> tags = tagService.getTagsByCafe(cafe);

        then(tags)
                .contains(tag1, tag2);
    }

    @Test
    public void getTagsByName_thenTagWithName() {
        Tag result = tagService.getTagByName("tag");

        then(result)
                .isEqualTo(tag);
    }

    @Test
    public void getTagsByName_whenTagNameHasSpace_thenReturnTagWithTrimmedName() {
        Tag result = tagService.getTagByName(" tag  ");

        then(result)
                .isEqualTo(tag);
    }

    @Test
    public void getArticlesByTag_whenCafeAndTag_thenReturnArticleHasTagInCafe() {
        Article article1 = new Article(board, member, "title1", "content1");
        Article article2 = new Article(board, member, "title2", "content2");
        given(tagRepository.findArticlesByCafeAndTag(cafe, tag))
                .willReturn(Arrays.asList(article1, article2));

        List<Article> articles = tagService.getArticlesByTag(cafe, tag);

        then(articles)
                .containsOnlyOnce(article1, article2);
    }

    @Test
    public void updateTagsOfArticle_thenUpdateTags() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        article.getTags().addAll(Arrays.asList(tag1));
        given(tagRepository.findByName("tag2"))
            .willReturn(tag2);

        tagService.updateTagsOfArticle(article, Arrays.asList(tag2));

        then(article.getTags())
                .contains(tag2);
        verify(articleRepository).save(article);
    }

    @Test
    public void updateTagsOfArticle_whenNotSavedTag_thenSaveTagAndUpdateTags() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        article.getTags().addAll(Arrays.asList(tag1));
        given(tagRepository.findByName("tag2"))
                .willReturn(null);
        given(tagRepository.save(tag2))
                .willReturn(tag2);

        tagService.updateTagsOfArticle(article, Arrays.asList(tag2));

        then(article.getTags())
                .contains(tag2);
        verify(tagRepository).save(tag2);
        verify(articleRepository).save(article);
    }
}