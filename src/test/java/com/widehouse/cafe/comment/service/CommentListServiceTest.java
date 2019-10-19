package com.widehouse.cafe.comment.service;

import static java.time.LocalDateTime.now;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.ArticleRepository;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.cafe.entity.CafeVisibility;
import com.widehouse.cafe.comment.entity.Comment;
import com.widehouse.cafe.comment.entity.CommentRepository;
import com.widehouse.cafe.user.entity.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

/**
 * Created by kiel on 2017. 2. 20..
 */
@ExtendWith(MockitoExtension.class)
class CommentListServiceTest {
    private CommentListService service;

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private CafeMemberRepository cafeMemberRepository;

    private Cafe cafe;
    private Article article;
    private User commenter;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
    private Comment comment4;
    private Comment comment5;

    @BeforeEach
    void setUp() {
        service = new CommentListService(commentRepository, articleRepository, cafeMemberRepository);
        cafe = new Cafe("testurl", "testname");
        Board board = Board.builder().cafe(cafe).name("article").build();
        User writer = new User(1L, "writer", "password");
        article = new Article(1L, board, writer, "title", "content", Collections.emptyList(), 0, now(), now());

        commenter = new User(2L, "commenter", "password");

        comment1 = new Comment(article, commenter, "comment1");
        comment2 = new Comment(article, commenter, "comment2");
        comment3 = new Comment(article, commenter, "comment3");
        comment4 = new Comment(article, commenter, "comment4");
        comment5 = new Comment(article, commenter, "comment5");
    }

    @Test
    void listComments_Should_ListComments() {
        // given
        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.of(article));
        given(commentRepository.findByArticleId(anyLong(), any(PageRequest.class)))
                .willReturn(Arrays.asList(comment1, comment2, comment3, comment4, comment5));
        // when
        List<Comment> comments = service.listComments(commenter, article.getId(), 0, 5);
        // then
        then(comments)
                .containsExactly(comment1, comment2, comment3, comment4, comment5);
    }

    @Test
    void listComments_PrivateCafeAndCafeMember_Should_ReturnCommentList() {
        // given
        cafe.updateInfo(cafe.getName(), "", CafeVisibility.PRIVATE, cafe.getCategory());
        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.of(article));
        given(cafeMemberRepository.existsByCafeMember(any(Cafe.class), any(User.class)))
                .willReturn(true);
        given(commentRepository.findByArticleId(anyLong(), any(PageRequest.class)))
                .willReturn(Arrays.asList(comment1, comment2, comment3, comment4, comment5));
        // when
        User nonCafeMember = new User(3L, "noncafemember", "password");
        List<Comment> comments = service.listComments(nonCafeMember, article.getId(), 0, 5);
        // then
        then(comments)
                .containsExactly(comment1, comment2, comment3, comment4, comment5);
    }

    @Test
    void listComments_PrivateCafe_NotMember_Should_EmptyList() {
        // given
        cafe.updateInfo(cafe.getName(), "", CafeVisibility.PRIVATE, cafe.getCategory());

        given(articleRepository.findById(anyLong()))
                .willReturn(Optional.of(article));
        given(cafeMemberRepository.existsByCafeMember(any(Cafe.class), any(User.class)))
                .willReturn(false);
        // when
        User nonCafeMember = new User(3L, "noncafemember", "password");
        List<Comment> comments = service.listComments(nonCafeMember, article.getId(), 0, 5);
        // then
        then(comments)
                .isEmpty();
    }
}
