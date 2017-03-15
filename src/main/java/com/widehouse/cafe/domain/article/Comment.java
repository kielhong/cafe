package com.widehouse.cafe.domain.article;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.SimpleMember;
import com.widehouse.cafe.exception.NoAuthorityException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    private SimpleMember member;

    private String comment;

    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;

    public Comment(Long articleId, Member member, String comment) {
        this.articleId = articleId;
        this.member = new SimpleMember(member);
        this.comment = comment;
        this.createDateTime = this.updateDateTime = LocalDateTime.now();
    }

    public Comment(Article article, Member member, String comment) {
        this(article.getId(), member, comment);
    }

    public void modify(Member member, String comment) {
        if (this.member.getId().equals(member.getId())) {
            this.comment = comment;
            this.updateDateTime = LocalDateTime.now();
        } else {
            throw new NoAuthorityException();
        }
    }
}
