package com.widehouse.cafe.comment.entity;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.common.exception.NoAuthorityException;
import com.widehouse.cafe.member.entity.Member;
import com.widehouse.cafe.member.entity.SimpleMember;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
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
