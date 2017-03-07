package com.widehouse.cafe.service;

import static com.widehouse.cafe.domain.cafemember.CafeMemberRole.MANAGER;
import static com.widehouse.cafe.domain.cafemember.CafeMemberRole.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Comment;
import com.widehouse.cafe.domain.article.CommentRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafe.CategoryRepository;
import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 12..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private CafeRepository cafeRepository;
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private CafeMemberRepository cafeMemberRepository;
    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private CafeService cafeService;
    @Autowired
    private CommentService commentService;

    private Cafe cafe;
    private Board board;
    private Article article;
    private Member manager;
    private Member commenter;

    @Before
    public void setUp() {
        manager = new Member("manager");
        cafe = new Cafe("testurl", "testname", "", CafeVisibility.PUBLIC, new Category(1L, "test"));
        board = new Board(cafe,"article");
        Member writer = new Member("writer");
        article = new Article(board, writer, "title", "content");

        commenter = new Member("commenter");
    }

    @Test
    public void writeComment_Should_CreateComment_IncreaseCafeCommentCount_IncreaseArticleCommentCount() {
        // given
        String commentText = "comment";
        Long cafeCommentCount = cafe.getStatistics().getCafeCommentCount();
        int articleCommentCount = article.getCommentCount();
        given(cafeMemberRepository.existsByCafeMember(cafe, commenter))
                .willReturn(true);
        // when
        Comment comment = commentService.writeComment(article, commenter, commentText);
        // then
        verify(commentRepository).save(comment);
        verify(cafeRepository).save(cafe);
        verify(articleRepository).save(article);
        assertThat(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("article", article)
                .hasFieldOrPropertyWithValue("commenter", commenter)
                .hasFieldOrPropertyWithValue("comment", commentText);
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(cafeCommentCount + 1);
        assertThat(article.getCommentCount())
                .isEqualTo(articleCommentCount + 1);
    }

    @Test
    public void writeComment_By_NotCafeMember_Thorws_NoAuthorityException() {
        // Given
        Member notCafeMember = new Member("commenter");
        given(cafeMemberRepository.existsByCafeMember(cafe, notCafeMember))
                .willReturn(false);
        // Then
        assertThatThrownBy(() -> commentService.writeComment(article, notCafeMember, "comment"))
                .isInstanceOf(NoAuthorityException.class);
    }

    @Test
    public void modifyComment_Should_UpdateComment() {
        // given
        Comment comment = new Comment(article, commenter, "comment");
        Long beforeCommentCount = cafe.getStatistics().getCafeCommentCount();
        // when
        commentService.modifyComment(comment, commenter, "another comment");
        // then
        verify(commentRepository).save(comment);
        assertThat(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("article", article)
                .hasFieldOrPropertyWithValue("commenter", commenter)
                .hasFieldOrPropertyWithValue("comment", "another comment");
        assertThat(comment.getUpdateDateTime())
                .isAfterOrEqualTo(comment.getCreateDateTime());
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(beforeCommentCount);
    }

    @Test
    public void modifyComment_ByNotCommenter_Throw_NoAuthorityException() {
        // given
        Comment comment = new Comment(article, commenter, "comment");
        Member another = new Member("another member");
        // then
        assertThatThrownBy(() -> commentService.modifyComment(comment, another, "new comment"))
                .isInstanceOf(NoAuthorityException.class);
    }

    @Test
    public void deleteComment_ByCommenter_Should_Success_DecreaseCafeCommentCount_DecreaseArticleCommentCount() {
        // given
        given(cafeMemberRepository.existsByCafeMember(cafe, commenter))
                .willReturn(true);
        Comment comment = commentService.writeComment(article, commenter, "comment");
        Mockito.reset(commentRepository);
        Mockito.reset(cafeRepository);
        Mockito.reset(articleRepository);
        Long cafeCommentCount = cafe.getStatistics().getCafeCommentCount();
        int articleCommentCount = article.getCommentCount();
        given(commentRepository.findOne(comment.getId()))
                .willReturn(comment);
        // when
        commentService.deleteComment(comment.getId(), commenter);
        // then
        verify(commentRepository).delete(comment);
        verify(cafeRepository).save(cafe);
        verify(articleRepository).save(article);
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(cafeCommentCount - 1);
        assertThat(article.getCommentCount())
                .isEqualTo(articleCommentCount - 1);
    }

    @Test
    public void deleteComment_ByCafeManager_Should_Success_DecreaseCommentCount() {
        // given
        given(cafeMemberRepository.existsByCafeMember(cafe, commenter))
                .willReturn(true);
        Comment comment = commentService.writeComment(article, commenter, "comment");
        Mockito.reset(commentRepository);
        Mockito.reset(cafeRepository);
        Mockito.reset(articleRepository);

        Long beforeCafeStatisticsCommentCount = cafe.getStatistics().getCafeCommentCount();
        given(commentRepository.findOne(comment.getId()))
                .willReturn(comment);
        given(cafeMemberRepository.findByCafeAndMember(cafe, manager))
                .willReturn(new CafeMember(cafe, manager, MANAGER));
        // when
        commentService.deleteComment(comment.getId(), manager);
        // then
        verify(commentRepository).delete(comment);
        verify(cafeRepository).save(cafe);
        verify(articleRepository).save(article);
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(beforeCafeStatisticsCommentCount - 1);
    }

    @Test
    public void deleteComment_ByNotCafeManagerNorNotCommenter_Throw_NoAuthorityException() {
        // given
        given(cafeMemberRepository.existsByCafeMember(cafe, commenter))
                .willReturn(true);
        Comment comment = commentService.writeComment(article, commenter, "comment");
        Long beforeCafeStatisticsCommentCount = cafe.getStatistics().getCafeCommentCount();
        given(commentRepository.findOne(comment.getId()))
                .willReturn(comment);
        Member member1 = new Member("another writer");
        given(cafeMemberRepository.findByCafeAndMember(cafe, member1))
                .willReturn(new CafeMember(cafe, member1, MEMBER));
        // then
        assertThatThrownBy(() -> commentService.deleteComment(comment.getId(), member1))
                .isInstanceOf(NoAuthorityException.class);
        verify(commentRepository, never()).delete(comment);
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(beforeCafeStatisticsCommentCount);
    }
}