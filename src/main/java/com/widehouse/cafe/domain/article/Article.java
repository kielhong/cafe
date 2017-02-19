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
import javax.validation.constraints.Size;

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

    @Size(max = 500)
    private String title;

    @Size(max = 5000)
    private String content;

    private Long commentCount;

    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;

    public Article(Cafe cafe, Board board, Member writer, String title, String content) {
        this.cafe = cafe;
        this.board = board;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.commentCount = 0L;
        this.createDateTime = this.updateDateTime = LocalDateTime.now();
    }

    public void modify(String title, String content) {
        this.title = title;
        this.content = content;
        this.updateDateTime = LocalDateTime.now();
    }

    public void moveBoard(Board board) {
        this.board = board;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }

    public void decreaseCommentCount() {
        this.commentCount--;
    }

}
