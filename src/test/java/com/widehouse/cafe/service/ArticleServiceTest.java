package com.widehouse.cafe.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
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
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import com.widehouse.cafe.projection.ArticleProjection;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 19..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceTest {
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private CafeMemberRepository cafeMemberRepository;
    @MockBean
    private CafeRepository cafeRepository;
    @MockBean
    private TagRepository tagRepository;
    @Autowired
    private ArticleService articleService;


    private Cafe cafe;
    private Board board1;
    private Board board2;
    private Member writer;

    private Article article1;
    private Article article2;
    private Article article3;
    private Article article4;

    @Mock
    private ArticleProjection articleMock1;
    @Mock
    private ArticleProjection articleMock2;
    @Mock
    private ArticleProjection articleMock3;

    @Mock
    private Article articleMock;

    @Before
    public void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        board1 = new Board(cafe, "board1");
        board2 = new Board(cafe, "board2");
        writer = new Member("writer");

        article1 = new Article(board1, writer, "test article1", "test1");
        article2 = new Article(board1, writer, "test article2", "test2");
        article3 = new Article(board2, writer, "test article3", "test3");
        article4 = new Article(board2, writer, "test article3", "test3");
    }

    @Test
    public void getArticlesByCafe_Should_Return_ListArticleInCafeWithIdOrderDesc() {
        // given
        given(articleRepository.findByBoardCafe(cafe, new PageRequest(0, 3, new Sort(DESC, "id"))))
                .willReturn(Arrays.asList(articleMock3, articleMock2, articleMock1));
        // when
        List<ArticleProjection> articles = articleService.getArticlesByCafe(cafe, 0, 3);
        // then
        then(articles)
                .containsExactly(articleMock3, articleMock2, articleMock1);
    }

    @Test
    public void getArticlesByBoard_Should_Return_ListArticleInBoardWithIdOrderDesc() {
        // given
        given(articleRepository.findByBoard(board1, new PageRequest(0, 3, new Sort(DESC, "id"))))
                .willReturn(Arrays.asList(article2, article1));
        // when
        List<Article> articles = articleService.getArticlesByBoard(board1, 0, 3);
        // then
        then(articles)
                .containsExactly(article2, article1);
    }

    @Test
    public void getArticle_WithArticleId_Should_Return_Article() {
        // given
        Member reader = new Member("reader");
        given(articleRepository.findOne(1L))
                .willReturn(articleMock);
        given(articleMock.getCafe())
                .willReturn(cafe);
        given(articleMock.getTitle())
                .willReturn("article title");
        // when
        Article article = articleService.getArticle(1L, reader);
        // then
        then(article)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", "article title");
    }

    @Test
    public void getArticle_WithPrivateCafe_WithNonCafeMember_Should_Throw_NoAuthroityException() {
        // given
        Cafe cafe1 = new Cafe("private", "private cafe", "", CafeVisibility.PRIVATE, new Category());
        Member reader = new Member("reader");
        given(articleRepository.findOne(1L))
                .willReturn(articleMock);
        given(articleMock.getCafe())
                .willReturn(cafe1);
        given(cafeMemberRepository.existsByCafeMember(cafe, reader))
                .willReturn(false);
        // then
        assertThatThrownBy(() -> articleService.getArticle(1L, reader))
                .isInstanceOf(NoAuthorityException.class);
    }

    @Test
    public void writeArticle_WithCafeMember_Should_Success() {
        // given
        given(cafeMemberRepository.existsByCafeMember(board1.getCafe(), writer))
                .willReturn(true);
        given(articleRepository.save(any(Article.class)))
                .willReturn(new Article(board1, writer, "title", "content"));
        // when
        Article article = articleService.writeArticle(board1, writer, "title", "content");
        // then
        then(article)
                .isNotNull()
                .hasFieldOrPropertyWithValue("board.id", board1.getId())
                .hasFieldOrPropertyWithValue("title", "title")
                .hasFieldOrPropertyWithValue("content", "content");
        verify(cafeMemberRepository).existsByCafeMember(any(Cafe.class), any(Member.class));
        verify(articleRepository).save(any(Article.class));
        verify(cafeRepository).save(any(Cafe.class));
    }

    @Test
    public void writeArticle_WithNoCafeMember_Should_ThrowNoAuthorityException() {
        // given
        given(cafeMemberRepository.existsByCafeMember(board1.getCafe(), writer))
                .willReturn(false);
        given(articleRepository.save(any(Article.class)))
                .willReturn(new Article(board1, writer, "title", "content"));
        // when
        assertThatThrownBy(() -> articleService.writeArticle(board1, writer, "title", "content"))
                .isInstanceOf(NoAuthorityException.class);
        // then
        verify(cafeMemberRepository).existsByCafeMember(any(Cafe.class), any(Member.class));
        verify(articleRepository, never()).save(any(Article.class));
        verify(cafeRepository, never()).save(any(Cafe.class));
    }

    @Test
    public void addTag_WithTag_Should_AddRelationToTag() {
        // given
        Tag tag = new Tag("tag");
        // when
        articleService.addTag(article1, tag);
        // then
        then(article1.getTags())
                .contains(tag);
        verify(articleRepository).save(article1);
    }
}
