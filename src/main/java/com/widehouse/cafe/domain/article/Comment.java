package com.widehouse.cafe.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.NoAuthorityException;
import lombok.AllArgsConstructor;
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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Comment {
    @Id
    private String id;

    private Long articleId;

    private Long memberId;

    @Size(max = 2000)
    private String comment;

    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;

    public Comment(Long articleId, Long memberId, String comment) {
        this.articleId = articleId;
        this.memberId = memberId;
        this.comment = comment;
        this.createDateTime = this.updateDateTime = LocalDateTime.now();
    }

    public Comment(Article article, Member member, String comment) {
        this(article.getId(), member.getId(), comment);
    }

    public void modify(Member member, String comment) {
        if (this.memberId.equals(member.getId())) {
            this.comment = comment;
            this.updateDateTime = LocalDateTime.now();
        } else {
            throw new NoAuthorityException();
        }
    }
}
