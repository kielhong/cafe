package com.widehouse.cafe.domain.board;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeMember;
import com.widehouse.cafe.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Getter
public class Article {
    private Long id;

    // relations
    private Cafe cafe;
    private Board board;
    private List<Comment> comments;

    private Member writer;

    private String title;

    private String content;

    private LocalDateTime createDateTime;

    private LocalDateTime modifyDateTime;

    public Article(Cafe cafe, Board board, Member writer, String title, String content) {
        this.cafe = cafe;
        this.board = board;
        this.writer = writer;
        this.title = title;
        this.content = content;

        this.comments = new ArrayList<>();
        this.createDateTime = this.modifyDateTime = LocalDateTime.now();
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
        this.modifyDateTime = LocalDateTime.now();
    }

    public void moveBoard(Board board) {
        this.board = board;
    }
}
