package com.widehouse.cafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.domain.board.Article;
import com.widehouse.cafe.domain.board.Board;
import com.widehouse.cafe.domain.board.Comment;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 2. 12..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CommentServiceTest {
    @Autowired
    private CafeService cafeService;
    @Autowired
    private CommentService commentService;

    private Cafe cafe;
    private Board board;
    private Article article;
    private Member commenter;

    @Before
    public void setUp() {
        cafe = new Cafe("url", "test");
        board = new Board(cafe,"board");
        Member writer = new Member("writer");
        article = new Article(cafe, board, writer, "title", "content");

        commenter = new Member("commenter");
        cafeService.joinMember(cafe, commenter);
    }

    @Test
    public void writeComment_should_createComment_to_article() {
        // Given
        Long beforeCommentCount = cafe.getStatistics().getCafeCommentCount();
        // When
        Comment comment = commentService.writeComment(article, commenter, "comment");
        // Then
        assertThat(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("article", article)
                .hasFieldOrPropertyWithValue("commenter", commenter)
                .hasFieldOrPropertyWithValue("comment", "comment");
        assertThat(cafe.getStatistics().getCafeCommentCount())
                .isEqualTo(beforeCommentCount + 1);
    }

    @Test
    public void writeComment_by_not_cafemember_thorws_NoAuthorityException() {
        // Given
        Member notCafeMember = new Member("commenter");
        // Then
        assertThatThrownBy(() -> commentService.writeComment(article, notCafeMember, "comment"))
                .isInstanceOf(NoAuthorityException.class);
    }

    @Test
    public void modifyComment_should_update_comment() {
        // given
        Comment comment = commentService.writeComment(article, commenter, "comment");
        Long beforeCommentCount = cafe.getStatistics().getCafeCommentCount();
        // when
        commentService.modifyComment(comment, commenter, "another comment");
        // then
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
    public void modifyComment_by_not_commenter_throw_NoAuthorityException() {
        // given
        Comment comment = commentService.writeComment(article, commenter, "comment");
        Member another = new Member("another member");
        cafeService.joinMember(cafe, another);
        // then
        assertThatThrownBy(() -> commentService.modifyComment(comment, another, "new comment"))
                .isInstanceOf(NoAuthorityException.class);
    }
}
