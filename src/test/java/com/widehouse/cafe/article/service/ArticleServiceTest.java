package com.widehouse.cafe.article.service;

import static com.widehouse.cafe.cafe.entity.CafeVisibility.PRIVATE;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.ArticleRepository;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.article.entity.Tag;
import com.widehouse.cafe.article.entity.TagRepository;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.cafe.entity.Category;
import com.widehouse.cafe.common.exception.NoAuthorityException;
import com.widehouse.cafe.member.entity.Member;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;

/**
 * Created by kiel on 2017. 2. 19..
 */
@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {
    private ArticleService service;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private CafeMemberRepository cafeMemberRepository;
    @Mock
    private TagRepository tagRepository;
    @Mock
    private ApplicationEventPublisher eventPublisher;

    private Cafe cafe;
    private Board board1;
    private Board board2;
    private Member writer;
    private Member reader;

    private Article article1;
    private Article article2;
    private Article article3;

    @BeforeEach
    void setUp() {
        service = new ArticleService(articleRepository, cafeMemberRepository, tagRepository, eventPublisher);

        cafe = new Cafe("testurl", "testcafe");
        board1 = Board.builder().cafe(cafe).name("board1").build();
        board2 = Board.builder().cafe(cafe).name("board2").build();

        writer = new Member(1L, "writer", "password", "nickname", "writer@bar.com");
        reader = new Member(2L, "reader", "password","nickname",  "reader@bar.com");

        article1 = new Article(board1, writer, "test article1", "test1");
        article2 = new Article(board1, writer, "test article2", "test2");
        article3 = new Article(board2, writer, "test article3", "test3");
    }

    @Test
    void getArticlesByCafe_thenListArticleInCafe() {
        given(articleRepository.findByBoardCafe(any(Cafe.class), any(PageRequest.class)))
                .willReturn(Arrays.asList(article3, article2, article1));
        // when
        List<Article> articles = service.getArticles(cafe, 0, 3);
        // then
        then(articles)
                .containsExactly(article3, article2, article1);
    }

    @Test
    void getArticlesByBoard_thenListArticleInBoard() {
        given(articleRepository.findByBoard(any(Board.class), any(PageRequest.class)))
                .willReturn(Arrays.asList(article2, article1));
        // when
        List<Article> articles = service.getArticles(board1, 0, 3);
        // then
        then(articles)
                .containsExactly(article2, article1);
    }

    @Test
    void getArticle_withArticleId_thenReturnArticle() {
        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.of(article1));

        Article article = service.getArticle(1L, reader);

        then(article)
                .isNotNull()
                .hasFieldOrPropertyWithValue("title", "test article1");
    }

    @Test
    void getArticle_withPrivateCafeAndNonCafeMember_thenRaiseNoAuthorityException() {
        // given
        Cafe privateCafe = new Cafe("private", "private cafe", "", PRIVATE, new Category());
        Board board = Board.builder().cafe(privateCafe).name("board").build();
        Article article = new Article(board, writer, "private article", "content");
        given(articleRepository.findById(1L))
                .willReturn(Optional.of(article));
        given(cafeMemberRepository.existsByCafeMember(any(Cafe.class), any(Member.class)))
                .willReturn(false);
        // then
        thenThrownBy(() -> service.getArticle(1L, reader))
                .isInstanceOf(NoAuthorityException.class);
    }

    @Test
    void writeArticle_withCafeMember_thenCreateArticle() {
        given(cafeMemberRepository.existsByCafeMember(any(Cafe.class), any(Member.class)))
                .willReturn(true);
        given(articleRepository.save(any(Article.class)))
                .willReturn(new Article(board1, writer, "title", "content"));
        // when
        Article article = service.writeArticle(board1, writer, "title", "content");
        // then
        then(article)
                .hasFieldOrPropertyWithValue("board", board1)
                .hasFieldOrPropertyWithValue("board.cafe", board1.getCafe())
                .hasFieldOrPropertyWithValue("writer", writer)
                .hasFieldOrPropertyWithValue("title", "title")
                .hasFieldOrPropertyWithValue("content", "content");
        verify(articleRepository).save(any(Article.class));
    }

    @Test
    void writeArticle_withNotCafeMember_thenRaiseNoAuthorityException() {
        given(cafeMemberRepository.existsByCafeMember(any(Cafe.class), any(Member.class)))
                .willReturn(false);

        thenThrownBy(() -> service.writeArticle(board1, writer, "title", "content"))
                .isInstanceOf(NoAuthorityException.class);
        verify(articleRepository, never()).save(any(Article.class));
    }

    @Test
    void addTag_withTag_thenArticleAddRelationToTag() {
        Tag tag = new Tag("tag");
        // when
        service.addTag(article1, tag);
        // then
        then(article1.getTags())
                .contains(tag);
        verify(articleRepository).save(article1);
        verify(tagRepository).save(tag);
    }
}
