package com.widehouse.cafe.domain.board;

import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import lombok.Getter;
import org.springframework.cglib.core.Local;

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

    private LocalDateTime updateDateTime;

    public Comment(Article article, Member commenter, String comment) {
        this.article = article;
        this.commenter = commenter;
        this.comment = comment;
        this.createDateTime = this.updateDateTime = LocalDateTime.now();
    }

    public void modify(Member commenter, String comment) {
        if (this.commenter.equals(commenter)) {
            this.comment = comment;
            this.updateDateTime = LocalDateTime.now();
        } else {
            throw new NoAuthorityException();
        }
    }
}
