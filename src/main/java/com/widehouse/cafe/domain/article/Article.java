package com.widehouse.cafe.domain.article;

import com.widehouse.cafe.domain.cafe.Board;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Entity

@NoArgsConstructor
@Getter
public class Article {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    private Cafe cafe;

    @ManyToOne
    private Board board;

    @ManyToOne
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
