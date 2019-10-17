package com.widehouse.cafe.comment.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.user.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


/**
 * Created by kiel on 2017. 2. 12..
 */
class CommentTest {
    private User user;
    private Article article;
    private Comment comment;

    @BeforeEach
    void setUp() {
        Cafe cafe = new Cafe("testcafe", "testcafe");
        Board board = Board.builder().cafe(cafe).name("testboard").build();
        user = new User(1L, "user", "password");
        article = new Article(board, user, "test title", "test content");
        comment = new Comment(article, user, "test comment");
    }

    @Test
    void createComment_thenCreateComment_AttachToArticle() {
        Comment comment = new Comment(article, user, "test comment");
        // Then
        assertThat(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("member.id", user.getId())
                .hasFieldOrPropertyWithValue("text", "test comment");
        assertThat(comment.getCreateDateTime())
                .isNotNull();
    }

    @Test
    void modifyComment_thenUpdateComment() {
        comment.modify("another comment");
        // Then
        assertThat(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("cafeId", article.getCafe().getId())
                .hasFieldOrPropertyWithValue("articleId", article.getId())
                .hasFieldOrPropertyWithValue("member.id", user.getId())
                .hasFieldOrPropertyWithValue("text", "another comment");
        assertThat(comment.getUpdateDateTime())
                .isNotNull()
                .isAfterOrEqualTo(comment.getCreateDateTime());
    }
}