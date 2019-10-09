package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.Assertions.assertThat;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.common.exception.NoAuthorityException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Created by kiel on 2017. 2. 12..
 */
class CommentTest {
    private Member member;
    private Article article;
    private Comment comment;

    @BeforeEach
    void setUp() {
        Cafe cafe = new Cafe("testcafe", "testcafe");
        Board board = new Board(cafe, "testboard");
        member = new Member(1L, "member", "password", "nickname", "foo@bar.com");
        article = new Article(board, member, "test title", "test content");
        comment = new Comment(article, member, "test comment");
    }

    @Test
    void createComment_thenCreateComment_AttachToArticle() {
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
    void modifyComment_thenUpdateComment() {
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
    void modifyComment_withNotCommentOwner_thenRaiseNoAuthorityException() {
        // Given
        Member anotherCommenter = new Member(2L, "another", "password", "nickname", "another@bar.com");
        // Then
        Assertions.assertThatThrownBy(() -> comment.modify(anotherCommenter, "new comment"))
                .isInstanceOf(NoAuthorityException.class);
    }
}