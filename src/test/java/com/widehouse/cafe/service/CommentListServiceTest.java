package com.widehouse.cafe.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.springframework.data.domain.Sort.Direction.ASC;

import com.widehouse.cafe.domain.article.Article;
import com.widehouse.cafe.domain.article.ArticleRepository;
import com.widehouse.cafe.domain.article.Comment;
import com.widehouse.cafe.domain.article.CommentRepository;
import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.member.Member;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 20..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentListServiceTest {
    @MockBean
    private CommentRepository commentRepository;
    @MockBean
    private ArticleRepository articleRepository;
    @MockBean
    private CafeMemberRepository cafeMemberRepository;

    @Autowired
    private CommentService commentService;

    private Cafe cafe;
    private Board board;
    private Article article;
    private Member commenter;
    private Comment comment1;
    private Comment comment2;
    private Comment comment3;
    private Comment comment4;
    private Comment comment5;

    @Before
    public void setUp() {
        cafe = new Cafe("testurl", "testname");
        board = new Board(cafe,"article");
        Member writer = new Member("writer");
        article = new Article(board, writer, "title", "content");

        commenter = new Member("commenter");

        comment1 = new Comment(article, commenter, "comment1");
        comment2 = new Comment(article, commenter, "comment2");
        comment3 = new Comment(article, commenter, "comment3");
        comment4 = new Comment(article, commenter, "comment4");
        comment5 = new Comment(article, commenter, "comment5");
    }


    @Test
    public void getComments_Should_ListComments() {
        // given
        given(cafeMemberRepository.existsByCafeMember(cafe, commenter))
                .willReturn(true);
        given(articleRepository.findOne(article.getId()))
                .willReturn(article);
        given(commentRepository.findByArticleId(article.getId(), new PageRequest(0, 5, new Sort(ASC, "id"))))
                .willReturn(Arrays.asList(comment1, comment2, comment3, comment4, comment5));
        // when
        List<Comment> comments = commentService.getComments(commenter, article.getId(), 0, 5);
        // then
        then(comments)
                .containsExactly(comment1, comment2, comment3, comment4, comment5);
    }

    @Test
    public void getComments_PrivateCafe_NotMember_Should_EmptyList() {
        // given
        Member nonCafeMember = new Member("noncafemember");
        cafe.updateInfo(cafe.getName(), "", CafeVisibility.PRIVATE, cafe.getCategory());
        given(cafeMemberRepository.existsByCafeMember(cafe, nonCafeMember))
                .willReturn(false);
        given(articleRepository.findOne(article.getId()))
                .willReturn(article);
        given(commentRepository.findByArticleId(article.getId(), new PageRequest(0, 5, new Sort(ASC, "id"))))
                .willReturn(Arrays.asList(comment1, comment2, comment3, comment4, comment5));
        // when
        List<Comment> comments = commentService.getComments(nonCafeMember, article.getId(), 0, 5);
        // then
        then(comments)
                .isEmpty();
    }
}
