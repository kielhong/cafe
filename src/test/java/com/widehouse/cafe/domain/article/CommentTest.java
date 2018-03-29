package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.Assertions.assertThat;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;


/**
 * Created by kiel on 2017. 2. 12..
 */
public class CommentTest {
    private Cafe cafe;
    private Board board;
    private Member member;
    private Article article;
    private Comment comment;

    @Before
    public void init() {
        member = new Member(1L, "member", "password", "nickname", "foo@bar.com");
        cafe = new Cafe("testcafe", "testcafe");
        board = new Board(cafe, "testboard");
        article = new Article(board, member, "test title", "test content");
        comment = new Comment(article, member, "test comment");
    }

    @Test
    public void createComment_thenCreateComment_AttachToArticle() {
        Comment comment = new Comment(article, member, "test comment");
        // Then
        assertThat(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("member.id", member.getId())
                .hasFieldOrPropertyWithValue("comment", "test comment");
        assertThat(comment.getCreateDateTime())
                .isNotNull();
    }

    @Test
    public void modifyComment_thenUpdateComment() {
        comment.modify(member, "another comment");
        // Then
        assertThat(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("member.id", member.getId())
                .hasFieldOrPropertyWithValue("comment", "another comment");
        assertThat(comment.getUpdateDateTime())
                .isNotNull()
                .isAfterOrEqualTo(comment.getCreateDateTime());
    }

    @Test
    public void modifyComment_withNotCommentOwner_thenRaiseNoAuthorityException() {
        // Given
        Member anotherCommenter = new Member(2L, "another", "password", "nickname", "another@bar.com");
        // Then
        Assertions.assertThatThrownBy(() -> comment.modify(anotherCommenter, "new comment"))
                .isInstanceOf(NoAuthorityException.class);
    }
}