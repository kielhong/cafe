package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

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
    private Article article;
    @Before
    public void setUp () {
        cafe = new Cafe("testcafe", "testcafe");
        board = new Board(cafe, "testboard");
        member = new Member();
        article = new Article(board, member, "test title", "test content");
    }

    @Test
    public void writeArticle_should_create_article() {
        // When
        Article newArticle = new Article(board, member, "test title", "test content");
        // Then
        assertThat(newArticle)
                .isNotNull()
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("board", board)
                .hasFieldOrPropertyWithValue("writer", member)
                .hasFieldOrPropertyWithValue("title", "test title")
                .hasFieldOrPropertyWithValue("content", "test content")
                .hasFieldOrPropertyWithValue("commentCount", 0);
        assertThat(newArticle.getCreateDateTime())
                .isNotNull()
                .isEqualTo(newArticle.getUpdateDateTime());
    }

    @Test
    public void modifyArticle_should_update_article() {
        // Given
        int commentCount = article.getCommentCount();
        // When
        article.modify("modify title", "modify content");
        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("board", board)
                .hasFieldOrPropertyWithValue("writer", member)
                .hasFieldOrPropertyWithValue("title", "modify title")
                .hasFieldOrPropertyWithValue("content", "modify content")
                .hasFieldOrPropertyWithValue("commentCount", commentCount);
        assertThat(article.getUpdateDateTime())
                .isAfterOrEqualTo(article.getCreateDateTime());
    }

    @Test
    public void moveBoard_should_update_board() {
        // given
        Board board2 = new Board(cafe, "another article");
        // When
        article.moveBoard(board2);
        // Then
        assertThat(article)
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("board", board2);
    }

    // TODO : cafe 가 다른 board로 이동할 경우 오류 처리

    @Test
    public void increaseCommentCount_Should_IncreaseCommentCountBy1() {
        // given
        int commentCount = article.getCommentCount();
        // when
        article.increaseCommentCount();
        // then
        then(article.getCommentCount())
                .isEqualTo(commentCount + 1);
    }

    @Test
    public void descreaseCommentCount_Should_DescreaseCommentCountBy1() {
        // given
        int commentCount = article.getCommentCount();
        // when
        article.decreaseCommentCount();
        // then
        then(article.getCommentCount())
                .isEqualTo(commentCount - 1);
    }
}
