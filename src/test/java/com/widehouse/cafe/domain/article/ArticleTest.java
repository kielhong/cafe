package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.Assertions.assertThat;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Slf4j
public class ArticleTest {
    private Cafe cafe;
    private Board board;
    private Member member;

    @Before
    public void setUp () {
        cafe = new Cafe("testcafe", "testcafe");
        board = new Board(cafe, "testboard");
        member = new Member();
    }

    @Test
    public void writeArticle_should_create_article() {
        // When
        Article article = new Article(cafe, board, member, "test title", "test content");
        // Then
        assertThat(article)
                .isNotNull()
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("board", board)
                .hasFieldOrPropertyWithValue("writer", member)
                .hasFieldOrPropertyWithValue("title", "test title")
                .hasFieldOrPropertyWithValue("content", "test content");
        assertThat(article.getCreateDateTime())
                .isNotNull()
                .isEqualTo(article.getUpdateDateTime());
    }

    @Test
    public void modifyArticle_should_update_article() {
        // Given
        Article article = new Article(cafe, board, member, "test title", "test content");
        // When
        article.modify("modify title", "modify content");
        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("board", board)
                .hasFieldOrPropertyWithValue("writer", member)
                .hasFieldOrPropertyWithValue("title", "modify title")
                .hasFieldOrPropertyWithValue("content", "modify content");
        assertThat(article.getUpdateDateTime())
                .isAfterOrEqualTo(article.getCreateDateTime());
    }

    @Test
    public void moveBoard_should_update_board() {
        // Given
        Article article = new Article(cafe, board, member, "test title", "test content");
        Board board2 = new Board(cafe, "another article");
        // When
        article.moveBoard(board2);
        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("board", board2);
    }
}
