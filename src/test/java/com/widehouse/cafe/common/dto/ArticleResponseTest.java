package com.widehouse.cafe.common.dto;

import static java.time.LocalDateTime.now;
import static java.util.Collections.emptyList;
import static org.assertj.core.api.BDDAssertions.then;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.user.entity.User;

import org.junit.jupiter.api.Test;

class ArticleResponseTest {
    @Test
    void of_GivenArticle_ThenReturnArticleResponse() {
        // given
        Cafe cafe = new Cafe("testcafe", "testcafe");
        Board board = Board.builder().cafe(cafe).name("testboard").build();
        User user = new User(1L, "user", "password");
        Article article = new Article(1L, board, user, "test title", "test content", emptyList(), 0, 0, now(), now());
        // when
        ArticleResponse result = ArticleResponse.of(article);
        // then
        then(result)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("title", "test title")
                .hasFieldOrPropertyWithValue("content", "test content")
                .hasFieldOrPropertyWithValue("board", board)
                .hasFieldOrPropertyWithValue("writer", user)
                .hasFieldOrPropertyWithValue("tags", emptyList())
                .hasFieldOrPropertyWithValue("readCount", 0L)
                .hasFieldOrPropertyWithValue("commentCount", 0L);
    }
}