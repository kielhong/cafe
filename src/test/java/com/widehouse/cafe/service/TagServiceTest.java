package com.widehouse.cafe.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.ArticleTag;
import com.widehouse.cafe.domain.article.ArticleTagRepository;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.article.TagRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 3. 10..
 */
@RunWith(SpringRunner.class)
@Import(TagService.class)
public class TagServiceTest {
    @Autowired
    private TagService tagService;
    @MockBean
    private ArticleTagRepository articleTagRepository;
    @MockBean
    private TagRepository tagRepository;
    @MockBean
    private ArticleRepository articleRepository;

    @Test
    public void getTagsByCafe_Should_ListTagsOfCafe() {
        // Given
        Cafe cafe = new Cafe("testurl", "testname");
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        given(articleTagRepository.findTagsByCafe(cafe))
                .willReturn(Arrays.asList(tag1, tag2));
        // when
        List<Tag> tags = tagService.getTagsByCafe(cafe);
        // then
        then(tags)
                .contains(tag1, tag2);
    }

    @Test
    public void getTagsByName_Should_ReturnTagHasName() {
        String tagName = "tag1";
        Tag tag = new Tag(tagName);
        given(tagRepository.findByName(tagName))
                .willReturn(tag);
        // when
        Tag result = tagService.getTagByName(tagName);
        // then
        then(result)
                .hasFieldOrPropertyWithValue("name", tagName);
    }

    @Test
    public void getTagsByName_WhenSpaceIncluded_Should_ReturnTagWithTrimedName() {
        String tagName = "tagname";
        Tag tag = new Tag(tagName);
        given(tagRepository.findByName(tagName))
                .willReturn(tag);
        // when
        Tag result = tagService.getTagByName(" " + tagName + "  ");
        // then
        then(result)
                .hasFieldOrPropertyWithValue("name", tagName);
    }

    @Test
    public void getArticlesByTag_WhenCafeAndTag_Should_ReturnArticleHasTagInCafe() {
        Cafe cafe = new Cafe("testurl", "testname");
        Board board = new Board(cafe, "board");
        Member member = new Member("member");
        Tag tag = new Tag("tagname");
        given(articleTagRepository.findArticlesByCafeAndTag(cafe, tag))
                .willReturn(Arrays.asList(
                        new Article(board, member, "title1", "content1"),
                        new Article(board, member, "title2", "content2")));
        // when
        List<Article> articles = tagService.getArticlesByTag(cafe, tag);
        // then
        then(articles)
                .hasSize(2);
    }

    @Test
    public void updateTagsOfArticle_Should_UpdateTags() {
        // given
        Cafe cafe = new Cafe("testurl", "testname");
        Board board = new Board(cafe, "board");
        Member member = new Member("member");
        Article article = new Article(board, member, "test", "test");
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        ArticleTag at1 = new ArticleTag(article, tag1);
        tag1.getArticleTags().add(at1);
        given(tagRepository.findByName("tag2"))
            .willReturn(tag2);
        // when
        tagService.updateTagsOfArticle(article, Arrays.asList(tag2));
        // then
        then(article.getTags())
                .contains(tag2);
        verify(articleRepository).save(article);
    }

    @Test
    public void updateTagsOfArticle_WhenNotSavedTag_Should_SaveTagAndUpdateTags() {
        // given
        Cafe cafe = new Cafe("testurl", "testname");
        Board board = new Board(cafe, "board");
        Member member = new Member("member");
        Article article = new Article(board, member, "test", "test");
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");
        ArticleTag at1 = new ArticleTag(article, tag1);
        tag1.getArticleTags().add(at1);
        given(tagRepository.findByName("tag2"))
                .willReturn(null);
        // when
        tagService.updateTagsOfArticle(article, Arrays.asList(tag2));
        // then
        then(article.getTags())
                .contains(tag2);
        verify(tagRepository).save(tag2);
        verify(articleRepository).save(article);
    }
}
