package com.widehouse.cafe.comment.entity;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.common.exception.NoAuthorityException;
import com.widehouse.cafe.user.entity.User;
import com.widehouse.cafe.user.entity.SimpleUser;

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

    private Long cafeId;

    private Long articleId;

    private SimpleUser member;

    private String comment;

    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;

    public Comment(Long cafeId, Long articleId, User member, String comment) {
        this.cafeId = cafeId;
        this.articleId = articleId;
        this.member = new SimpleUser(member);
        this.comment = comment;
        this.createDateTime = this.updateDateTime = LocalDateTime.now();
    }

    public Comment(Article article, User user, String comment) {
        this(article.getCafe().getId(), article.getId(), user, comment);
    }

    public void modify(String comment) {
        this.comment = comment;
        this.updateDateTime = LocalDateTime.now();
    }
}
