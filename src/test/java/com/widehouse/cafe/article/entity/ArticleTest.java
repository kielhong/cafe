package com.widehouse.cafe.article.entity;

import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.user.entity.User;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created by kiel on 2017. 2. 11..
 */
class ArticleTest {
    private Cafe cafe;
    private Board board;
    private User user;
    private Article article;

    @BeforeEach
    void init() {
        cafe = new Cafe("testcafe", "testcafe");
        board = Board.builder().cafe(cafe).name("testboard").build();
        user = new User();
        article = new Article(board, user, "test title", "test content");
    }

    @Test
    void writeArticle_should_create_article() {
        Article newArticle = new Article(board, user, "test title", "test content");

        then(newArticle)
                .isNotNull()
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("board", board)
                .hasFieldOrPropertyWithValue("writer", user)
                .hasFieldOrPropertyWithValue("title", "test title")
                .hasFieldOrPropertyWithValue("commentCount", 0L)
                .hasFieldOrProperty("createdAt")
                .hasFieldOrProperty("updatedAt");
    }

    @Test
    void modifyArticle_should_update_article() {
        long commentCount = article.getCommentCount();

        article.modify("modify title", "modify content");

        then(article)
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("board", board)
                .hasFieldOrPropertyWithValue("writer", user)
                .hasFieldOrPropertyWithValue("title", "modify title")
                .hasFieldOrPropertyWithValue("commentCount", commentCount);
        then(article.getUpdatedAt())
                .isAfterOrEqualTo(article.getCreatedAt());
    }

    @Test
    void moveBoard_should_update_board() {
        Board board2 = board = Board.builder().cafe(cafe).name("another artilce").build();

        article.moveBoard(board2);

        then(article)
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("board", board2);
    }

    // TODO : cafe 가 다른 board로 이동할 경우 오류 처리

    @Test
    void increaseCommentCount_thenCommentCountIncreasedBy1() {
        long commentCount = article.getCommentCount();

        article.increaseCommentCount();

        then(article.getCommentCount())
                .isEqualTo(commentCount + 1);
    }

    @Test
    void decreaseCommentCount_thenCommentCountDescreasedBy1() {
        long commentCount = article.getCommentCount();

        article.decreaseCommentCount();

        then(article.getCommentCount())
                .isEqualTo(commentCount - 1);
    }

    @Test
    void getTags_Should_ListTags() {
        Tag tag1 = new Tag("tag1");
        Tag tag2 = new Tag("tag2");

        article.getTags()
                .addAll(Arrays.asList(tag1, tag2));

        then(article.getTags())
                .contains(tag1, tag2);
    }

    @Test
    void increaseReadCount_ThenIncreaseReadCountBy1() {
        // given
        Article article = new Article(board, user, "title", "content");
        long readCount = article.getReadCount();
        // when
        article.increaseReadCount();
        // then
        then(article.getReadCount())
                .isEqualTo(readCount + 1);
    }
}