package com.widehouse.cafe.comment.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.ArticleRepository;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.cafe.entity.CafeVisibility;
import com.widehouse.cafe.comment.entity.Comment;
import com.widehouse.cafe.comment.entity.CommentRepository;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.member.entity.Member;
import com.widehouse.cafe.cafe.service.CafeMemberService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created by kiel on 2017. 2. 20..
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CommentService.class)
class CommentListServiceTest {
    @Autowired
    private CommentService commentService;

    @MockBean
    private CafeMemberService cafeMemberService;
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private CafeRepository cafeRepository;
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private CafeMemberRepository cafeMemberRepository;

    private Cafe cafe;
    private Board board;
    private Article article;
    private Member commenter;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
    private Comment comment4;
    private Comment comment5;

    @BeforeEach
    void setUp() {
        cafe = new Cafe("testurl", "testname");
        Board board = Board.builder().cafe(cafe).name("article").build();
        Member writer = new Member(1L, "writer", "password", "nickname", "foo@bar.com");
        article = new Article(board, writer, "title", "content");

        commenter = new Member(2L, "commenter", "password", "nickname", "commeter@bar.com");

        comment1 = new Comment(article, commenter, "comment1");
        comment2 = new Comment(article, commenter, "comment2");
        comment3 = new Comment(article, commenter, "comment3");
        comment4 = new Comment(article, commenter, "comment4");
        comment5 = new Comment(article, commenter, "comment5");
    }


    @Test
    void getComments_Should_ListComments() {
        // given
        given(cafeMemberService.isCafeMember(cafe, commenter))
                .willReturn(true);
        given(articleRepository.findById(article.getId()))
                .willReturn(Optional.of(article));
        given(commentRepository.findByArticleId(article.getId(), PageRequest.of(0, 5, new Sort(ASC, "id"))))
                .willReturn(Arrays.asList(comment1, comment2, comment3, comment4, comment5));
        // when
        List<Comment> comments = commentService.getComments(commenter, article.getId(), 0, 5);
        // then
        then(comments)
                .containsExactly(comment1, comment2, comment3, comment4, comment5);
    }

    @Test
    void getComments_PrivateCafe_NotMember_Should_EmptyList() {
        // given
        Member nonCafeMember = new Member(3L, "noncafemember", "password", "nickname", "nonmember@bar.com");
        cafe.updateInfo(cafe.getName(), "", CafeVisibility.PRIVATE, cafe.getCategory());
        given(cafeMemberService.isCafeMember(cafe, nonCafeMember))
                .willReturn(false);
        given(articleRepository.findById(article.getId()))
                .willReturn(Optional.of(article));
        given(commentRepository.findByArticleId(article.getId(), PageRequest.of(0, 5, new Sort(ASC, "id"))))
                .willReturn(Arrays.asList(comment1, comment2, comment3, comment4, comment5));
        // when
        List<Comment> comments = commentService.getComments(nonCafeMember, article.getId(), 0, 5);
        // then
        then(comments)
                .isEmpty();
    }
}
