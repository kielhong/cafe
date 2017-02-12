package com.widehouse.cafe.domain.board;

import com.widehouse.cafe.domain.member.Member;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Getter
public class Comment {
    private Article article;

    private Member commenter;

    private String comment;

    private LocalDateTime createDateTime;

    public Comment(Article article, Member commenter, String comment) {
        this.article = article;
        this.commenter = commenter;
        this.comment = comment;
        this.createDateTime = LocalDateTime.now();
    }
}
