package com.widehouse.cafe.comment.service;

import static com.widehouse.cafe.cafe.entity.CafeMemberRole.MANAGER;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMember;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.comment.entity.Comment;
import com.widehouse.cafe.comment.entity.CommentRepository;
import com.widehouse.cafe.common.exception.NoAuthorityException;
import com.widehouse.cafe.user.entity.SimpleUser;
import com.widehouse.cafe.user.entity.User;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Created by kiel on 2017. 2. 12..
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    private CommentService commentService;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private CafeMemberRepository cafeMemberRepository;

    private User commenter;
    private Cafe cafe;
    private Article article;
    private Comment comment;

    @BeforeEach
    void setUp() {
        commentService = new CommentService(commentRepository, cafeRepository, cafeMemberRepository);
        commenter = new User(2L, "commenter", "password");
        User writer = new User(3L, "writer", "password");

        cafe = new Cafe(1L, "testurl", "testname");
        Board board = Board.builder().cafe(cafe).name("article").build();
        article = new Article(1L, board, writer, "title", "content", new ArrayList<>(), 0, 0, now(), now());
        comment = new Comment("commentId", 1L, 1L, new SimpleUser(commenter), "comment",
                new ArrayList<>(), now(), now());
    }

    @Test
    void writeComment_thenCreateComment_IncreaseCafeCommentCount_IncreaseArticleCommentCount() {
        given(cafeMemberRepository.existsByCafeMember(any(Cafe.class), any(User.class)))
                .willReturn(true);
        // when
        Comment result = commentService.writeComment(article, commenter, "new comment");
        // then
        then(result)
                .isNotNull()
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("member.id", commenter.getId())
                .hasFieldOrPropertyWithValue("text", "new comment");
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void writeComment_withNotCafeMember_thenRaiseNoAuthorityException() {
        User notCafeMember = new User(4L, "commenter", "password");
        given(cafeMemberRepository.existsByCafeMember(any(Cafe.class), any(User.class)))
                .willReturn(false);

        thenThrownBy(() -> commentService.writeComment(article, notCafeMember, "comment"))
                .isInstanceOf(NoAuthorityException.class);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void writeReplyComment_thenSuccess() {
        given(cafeMemberRepository.existsByCafeMember(any(Cafe.class), any(User.class)))
                .willReturn(true);
        given(cafeRepository.findById(anyLong()))
                .willReturn(Optional.of(cafe));
        Comment writeResult = new Comment(1L, 1L, commenter, "comment");
        writeResult.getReplies().add(new Comment(1L, 1L, commenter, "reply comment"));
        given(commentRepository.save(comment))
                .willReturn(writeResult);
        // when
        Comment result = commentService.writeReplyComment(comment, commenter, "reply comment");
        // then
        then(comment.getReplies())
                .hasSize(1);
        then(result)
                .hasFieldOrPropertyWithValue("text", "reply comment");
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    void writeReplyComment_withNotCafeMember_thenRaiseNoAuthorityException() {
        given(cafeRepository.findById(anyLong()))
                .willReturn(Optional.of(cafe));
        given(cafeMemberRepository.existsByCafeMember(any(Cafe.class), any(User.class)))
                .willReturn(false);
        // when
        User another = new User(4L, "another_user", "password");
        thenThrownBy(() -> commentService.writeReplyComment(comment, another, "reply comment"))
                .isInstanceOf(NoAuthorityException.class);
        // then
        then(comment.getReplies())
                .hasSize(0);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    void modifyComment_withCommenter_thenUpdateComment() {
        // when
        commentService.modifyComment(comment, commenter, "updated comment");
        // then
        then(comment)
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("member.id", commenter.getId())
                .hasFieldOrPropertyWithValue("text", "updated comment");
        then(comment.getUpdateDateTime())
                .isAfterOrEqualTo(comment.getCreateDateTime());
        verify(commentRepository).save(comment);
    }

    @Test
    void modifyComment_withNotCommenter_thenRaiseNoAuthorityException() {
        User another = new User(4L, "another_member", "password");

        thenThrownBy(() -> commentService.modifyComment(comment, another, "new comment"))
                .isInstanceOf(NoAuthorityException.class);
    }

    @Test
    void deleteComment_ByCommenter_thenDeleteComment_DecreaseCounts() {
        // given
        given(cafeRepository.findById(anyLong()))
                .willReturn(Optional.of(cafe));
        // when
        commentService.deleteComment(comment, commenter);
        // then
        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteComment_withCafeManager_thenDeleteComment_DecreaseCounts() {
        // given
        User manager = new User(1L, "manager", "password");
        given(cafeRepository.findById(anyLong()))
                .willReturn(Optional.of(cafe));
        given(cafeMemberRepository.findByCafeAndMember(any(Cafe.class), any(User.class)))
                .willReturn(CafeMember.builder().cafe(cafe).member(manager).role(MANAGER).build());
        // when
        commentService.deleteComment(comment, manager);
        // then
        verify(commentRepository).delete(comment);
    }

    @Test
    void deleteComment_withNotCommentNorCafeManager_thenRaiseNoAuthorityException() {
        // given
        User anotherMember = new User(4L, "another writer", "password");
        given(cafeRepository.findById(anyLong()))
                .willReturn(Optional.of(cafe));
        given(cafeMemberRepository.findByCafeAndMember(any(Cafe.class), any(User.class)))
                .willReturn(CafeMember.builder().cafe(cafe).member(anotherMember).build());
        // then
        thenThrownBy(() -> commentService.deleteComment(comment, anotherMember))
                .isInstanceOf(NoAuthorityException.class);
        verify(commentRepository, never()).delete(comment);
    }

    @Test
    void getComment_ThenReturnComment() {
        given(commentRepository.findById(anyString()))
                .willReturn(Optional.of(comment));
        // when
        Comment result = commentService.getComment("commentId");
        // then
        then(result)
                .isEqualTo(comment);
    }
}