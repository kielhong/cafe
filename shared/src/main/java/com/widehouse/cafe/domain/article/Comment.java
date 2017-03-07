package com.widehouse.cafe.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Size;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Entity

@NoArgsConstructor
@Getter
@ToString
public class Comment {
    @Id @GeneratedValue
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Article article;

    @ManyToOne
    private Member commenter;

    @Size(max = 2000)
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