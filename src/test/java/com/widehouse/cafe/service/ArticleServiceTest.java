package com.widehouse.cafe.service;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PRIVATE;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.data.domain.Sort.Direction.DESC;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Tag;
import com.widehouse.cafe.domain.article.TagRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 2. 19..
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ArticleService.class)
public class ArticleServiceTest {
    @Autowired
    private ArticleService articleService;
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private CafeMemberRepository cafeMemberRepository;
    @MockBean
    private CafeRepository cafeRepository;
    @MockBean
    private TagRepository tagRepository;

    private Cafe cafe;
    private Board board1;
    private Board board2;
    private Member writer;
    private Member reader;

    private Article article1;
    private Article article2;
    private Article article3;

    @Before
    public void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        board1 = new Board(cafe, "board1");
        board2 = new Board(cafe, "board2");

        writer = new Member("writer");
        reader = new Member("reader");

        article1 = new Article(board1, writer, "test article1", "test1");
        article2 = new Article(board1, writer, "test article2", "test2");
        article3 = new Article(board2, writer, "test article3", "test3");
    }

    @Test
    public void getArticlesByCafe_thenListArticleInCafeWithIdOrderDesc() {
        given(articleRepository.findByBoardCafe(cafe, PageRequest.of(0, 3, new Sort(DESC, "id"))))
                .willReturn(Arrays.asList(article3, article2, article1));

        List<Article> articles = articleService.getArticlesByCafe(cafe, 0, 3);

        then(articles)
                .containsExactly(article3, article2, article1);
    }

    @Test
    public void getArticlesByBoard_thenListArticleInBoardWithIdOrderDesc() {
        given(articleRepository.findByBoard(board1, PageRequest.of(0, 3, new Sort(DESC, "id"))))
                .willReturn(Arrays.asList(article2, article1));

        List<Article> articles = articleService.getArticlesByBoard(board1, 0, 3);

        then(articles)
                .containsExactly(article2, article1);
    }

    @Test
    public void getArticle_withArticleId_thenReturnArticle() {
        given(articleRepository.findById(1L))
                .willReturn(Optional.of(article1));

        Article article = articleService.getArticle(1L, reader);

        then(article)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", "test article1");
    }

    @Test
    public void getArticle_withPrivateCafeAndNonCafeMember_thenRaiseNoAuthorityException() {
        Cafe privateCafe = new Cafe("private", "private cafe", "", PRIVATE, new Category());
        Board board = new Board(privateCafe, "board");
        Article article = new Article(board, writer, "private article", "content");
        given(articleRepository.findById(1L))
                .willReturn(Optional.of(article));
        given(cafeMemberRepository.existsByCafeMember(privateCafe, reader))
                .willReturn(false);

        thenThrownBy(() -> articleService.getArticle(1L, reader))
                .isInstanceOf(NoAuthorityException.class);
    }

    @Test
    public void writeArticle_withCafeMember_thenCreateArticle() {
        given(cafeMemberRepository.existsByCafeMember(board1.getCafe(), writer))
                .willReturn(true);
        given(articleRepository.save(any(Article.class)))
                .willReturn(new Article(board1, writer, "title", "content"));

        Article article = articleService.writeArticle(board1, writer, "title", "content");

        then(article)
                .hasFieldOrPropertyWithValue("board", board1)
                .hasFieldOrPropertyWithValue("board.cafe", board1.getCafe())
                .hasFieldOrPropertyWithValue("writer", writer)
                .hasFieldOrPropertyWithValue("title", "title")
                .hasFieldOrPropertyWithValue("content", "content");
        verify(articleRepository).save(any(Article.class));
        verify(cafeRepository).save(any(Cafe.class));
    }

    @Test
    public void writeArticle_withNotCafeMember_thenRaiseNoAuthorityException() {
        given(cafeMemberRepository.existsByCafeMember(board1.getCafe(), writer))
                .willReturn(false);
        given(articleRepository.save(any(Article.class)))
                .willReturn(new Article(board1, writer, "title", "content"));

        thenThrownBy(() -> articleService.writeArticle(board1, writer, "title", "content"))
                .isInstanceOf(NoAuthorityException.class);
        verify(articleRepository, never()).save(any(Article.class));
        verify(cafeRepository, never()).save(any(Cafe.class));
    }

    @Test
    public void addTag_withTag_thenArticleAddRelationToTag() {
        Tag tag = new Tag("tag");

        articleService.addTag(article1, tag);

        then(article1.getTags())
                .contains(tag);
        verify(articleRepository).save(article1);
        verify(tagRepository).save(tag);
    }
}
