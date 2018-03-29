package com.widehouse.cafe.service;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PUBLIC;
import static com.widehouse.cafe.domain.cafemember.CafeMemberRole.MANAGER;
import static com.widehouse.cafe.domain.cafemember.CafeMemberRole.MEMBER;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
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
import com.widehouse.cafe.domain.cafe.Category;
import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.SimpleMember;
import com.widehouse.cafe.exception.NoAuthorityException;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 2. 12..
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CommentService.class)
public class CommentServiceTest {
    @Autowired
    private CommentService commentService;

    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private CafeRepository cafeRepository;
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private CafeMemberRepository cafeMemberRepository;
    @MockBean
    private CafeMemberService cafeMemberService;

    private Member manager;
    private Member commenter;
    private Cafe cafe;
    private Article article;
    private Comment comment;

    @Before
    public void setUp() {
        manager = new Member(1L, "manager", "password", "nickname", "manager@bar.com");
        commenter = new Member(2L, "commenter", "password", "nickname", "commeter@bar.com");
        Member writer = new Member(3L, "writer", "password", "nickname", "writer@bar.com");

        cafe = new Cafe("testurl", "testname", "", PUBLIC, new Category(1, "test", 1, now()));
        Board board = new Board(cafe,"article");
        article = new Article(1L, board, writer, "title", "content",
                new ArrayList<>(), 0, now(), now());
        comment = new Comment("commentId", 1L, new SimpleMember(commenter), "comment",
                new ArrayList<>(), now(), now());

        given(articleRepository.findById(1L))
                .willReturn(Optional.of(article));
        given(commentRepository.findById("commentId"))
                .willReturn(Optional.of(comment));
        given(cafeMemberService.isCafeMember(cafe, commenter))
                .willReturn(true);
    }

    @Test
    public void writeComment_thenCreateComment_IncreaseCafeCommentCount_IncreaseArticleCommentCount() {
        Long cafeCommentCount = cafe.getStatistics().getCafeCommentCount();
        int articleCommentCount = article.getCommentCount();

        Comment result = commentService.writeComment(article, commenter, "new comment");

        then(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("member.id", commenter.getId())
                .hasFieldOrPropertyWithValue("comment", "new comment");
        then(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(cafeCommentCount + 1);
        then(article.getCommentCount())
                .isEqualTo(articleCommentCount + 1);
        verify(commentRepository).save(any(Comment.class));
        verify(cafeRepository).save(cafe);
        verify(articleRepository).save(article);
    }

    @Test
    public void writeComment_withNotCafeMember_thenRaiseNoAuthorityException() {
        Member notCafeMember = new Member(4L, "commenter", "password", "nickname", "commeter@bar.com");
        given(cafeMemberService.isCafeMember(cafe, notCafeMember))
                .willReturn(false);

        thenThrownBy(() -> commentService.writeComment(article, notCafeMember, "comment"))
                .isInstanceOf(NoAuthorityException.class);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    public void writeReplyComment_thenSuccess() {
        Comment writeResult = new Comment(1L, commenter, "comment");
        writeResult.getComments().add(new Comment(1L, commenter, "reply comment"));
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
    public void writeReplyComment_withNotCafeMember_thenRaiseNoAuthorityException() {
        Member another = new Member(4L, "another_member", "password", "nickname", "amember@bar.com");
        given(cafeMemberService.isCafeMember(cafe, another))
                .willReturn(false);
        // when
        thenThrownBy(() -> commentService.writeReplyComment(comment, another, "reply comment"))
                .isInstanceOf(NoAuthorityException.class);
        // then
        then(comment.getComments())
                .hasSize(0);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    public void modifyComment_withCommenter_thenUpdateComment() {
        // given
        Long beforeCommentCount = cafe.getStatistics().getCafeCommentCount();
        // when
        commentService.modifyComment(comment, commenter, "updated comment");
        // then
        then(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(beforeCommentCount);
        then(comment)
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("member.id", commenter.getId())
                .hasFieldOrPropertyWithValue("comment", "updated comment");
        then(comment.getUpdateDateTime())
                .isAfterOrEqualTo(comment.getCreateDateTime());
        verify(commentRepository).save(comment);
    }

    @Test
    public void modifyComment_withNotCommenter_thenRaiseNoAuthorityException() {
        Member another = new Member(4L, "another_member", "password", "nickname", "foo@bar.com");

        thenThrownBy(() -> commentService.modifyComment(comment, another, "new comment"))
                .isInstanceOf(NoAuthorityException.class);
    }

    @Test
    public void deleteComment_ByCommenter_thenDeleteComment_DecreaseCounts() {
        // given
        Long cafeCommentCount = cafe.getStatistics().getCafeCommentCount();
        int articleCommentCount = article.getCommentCount();
        // when
        commentService.deleteComment(comment.getId(), commenter);
        // then
        then(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(cafeCommentCount - 1 < 0 ? 0 : cafeCommentCount - 1);
        then(article.getCommentCount())
                .isEqualTo(articleCommentCount - 1);
        verify(commentRepository).delete(comment);
        verify(cafeRepository).save(cafe);
        verify(articleRepository).save(article);
    }

    @Test
    public void deleteComment_withCafeManager_thenDeleteComment_DecreaseCounts() {
        // given
        Long beforeCafeStatisticsCommentCount = cafe.getStatistics().getCafeCommentCount();
        int articleCommentCount = article.getCommentCount();
        given(cafeMemberRepository.findByCafeAndMember(cafe, manager))
                .willReturn(new CafeMember(cafe, manager, MANAGER));
        // when
        commentService.deleteComment(comment.getId(), manager);
        // then
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(beforeCafeStatisticsCommentCount - 1 < 0 ? 0 : beforeCafeStatisticsCommentCount - 1);
        then(article.getCommentCount())
                .isEqualTo(articleCommentCount - 1);
        verify(commentRepository).delete(comment);
        verify(cafeRepository).save(cafe);
        verify(articleRepository).save(article);
    }

    @Test
    public void deleteComment_withNotCommentNorNotCafeManager_thenRaiseNoAuthorityException() {
        // given
        Member anotherMember = new Member(4L, "another writer", "password", "nickname", "awriter@bar.com");
        Long beforeCafeStatisticsCommentCount = cafe.getStatistics().getCafeCommentCount();
        given(cafeMemberRepository.findByCafeAndMember(cafe, anotherMember))
                .willReturn(new CafeMember(cafe, anotherMember, MEMBER));
        // then
        thenThrownBy(() -> commentService.deleteComment(comment.getId(), anotherMember))
                .isInstanceOf(NoAuthorityException.class);
        then(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(beforeCafeStatisticsCommentCount);
        verify(commentRepository, never()).delete(comment);

    }
}