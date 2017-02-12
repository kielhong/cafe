package com.widehouse.cafe.domain.board;

import static org.assertj.core.api.Assertions.assertThat;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
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

    @Before
    public void setUp() {
        cafe = new Cafe("testcafe", "testcafe");
        board = new Board(cafe, "testboard");
        member = new Member();

        article = new Article(cafe, board, member, "test title", "test content");
    }

    @Test
    public void createComment_should_create_comment_and_attachto_article() {
        // Given
        Member commenter = new Member("commenter");
        // When
        Comment comment = new Comment(article, commenter, "test comment");
        // Then
        assertThat(comment)
                .isNotNull()
                .hasFieldOrPropertyWithValue("article", article)
                .hasFieldOrPropertyWithValue("commenter", commenter)
                .hasFieldOrPropertyWithValue("comment", "test comment");
        assertThat(comment.getCreateDateTime())
                .isNotNull();


    }
}
