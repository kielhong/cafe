package com.widehouse.cafe.service;

import static com.widehouse.cafe.domain.cafemember.CafeMemberRole.MANAGER;
import static com.widehouse.cafe.domain.cafemember.CafeMemberRole.MEMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

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
import com.widehouse.cafe.domain.member.SimpleMember;
import com.widehouse.cafe.exception.NoAuthorityException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 2. 12..
 */
@RunWith(SpringRunner.class)
@Import(CommentService.class)
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

        commenter = new Member(1L, "commenter");
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
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("member.id", commenter.getId())
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
    public void writeReplyComment_Should_Success() {
        // given
        Comment comment = new Comment(1L, commenter, "comment");
        Comment writeResult = new Comment(1L, commenter, "comment");
        writeResult.getComments().add(new Comment(1L, commenter, "reply comment"));
        given(commentRepository.findById(anyString()))
                .willReturn(Optional.of(comment));
        given(commentRepository.save(comment))
                .willReturn(writeResult);
        // when
        Comment result = commentService.writeReplyComment(comment, commenter, "reply comment");
        // then
        then(comment.getComments())
                .hasSize(1);
        then(result)
                .hasFieldOrPropertyWithValue("comment", "reply comment");
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    public void modifyComment_Should_UpdateComment() {
        // given
        Comment comment = new Comment(article, commenter, "comment");
        Long beforeCommentCount = cafe.getStatistics().getCafeCommentCount();
        // when
        commentService.modifyComment(comment, commenter, "another comment");
        // then
        then(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(beforeCommentCount);
        then(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("member.id", commenter.getId())
                .hasFieldOrPropertyWithValue("comment", "another comment");
        then(comment.getUpdateDateTime())
                .isAfterOrEqualTo(comment.getCreateDateTime());
        verify(commentRepository).save(comment);
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
        Comment comment = new Comment("testcomment", article.getId(), new SimpleMember(commenter), "comment",
                Arrays.asList(), LocalDateTime.now(), LocalDateTime.now());
        Long cafeCommentCount = cafe.getStatistics().getCafeCommentCount();
        int articleCommentCount = article.getCommentCount();
        given(cafeMemberRepository.existsByCafeMember(cafe, commenter))
                .willReturn(true);
        given(articleRepository.findById(comment.getArticleId()))
                .willReturn(Optional.of(article));
        given(commentRepository.findById(comment.getId()))
                .willReturn(Optional.of(comment));
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
        Comment comment = new Comment("testcomment", article.getId(), new SimpleMember(commenter), "comment",
                Arrays.asList(), LocalDateTime.now(), LocalDateTime.now());
        Long beforeCafeStatisticsCommentCount = cafe.getStatistics().getCafeCommentCount();
        given(cafeMemberRepository.existsByCafeMember(cafe, commenter))
                .willReturn(true);
        given(articleRepository.findById(comment.getArticleId()))
                .willReturn(Optional.of(article));
        given(commentRepository.findById(comment.getId()))
                .willReturn(Optional.of(comment));
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
        Comment comment = new Comment("testcomment", article.getId(), new SimpleMember(commenter), "comment",
                Arrays.asList(), LocalDateTime.now(), LocalDateTime.now());
        Member member1 = new Member(2L, "another writer");
        Long beforeCafeStatisticsCommentCount = cafe.getStatistics().getCafeCommentCount();
        given(cafeMemberRepository.existsByCafeMember(cafe, commenter))
                .willReturn(true);
        given(commentRepository.findById(comment.getId()))
                .willReturn(Optional.of(comment));
        given(articleRepository.findById(comment.getArticleId()))
                .willReturn(Optional.of(article));
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