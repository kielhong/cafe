package com.widehouse.cafe.comment.entity;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.user.entity.SimpleUser;
import com.widehouse.cafe.user.entity.User;

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

    private String text;

    private List<Comment> replies = new ArrayList<>();

    private LocalDateTime createDateTime;

    private LocalDateTime updateDateTime;

    /**
     * constructor.
     */
    public Comment(Long cafeId, Long articleId, User member, String text) {
        this.cafeId = cafeId;
        this.articleId = articleId;
        this.member = new SimpleUser(member);
        this.text = text;
        this.createDateTime = this.updateDateTime = LocalDateTime.now();
    }

    public Comment(Article article, User user, String text) {
        this(article.getCafe().getId(), article.getId(), user, text);
    }

    public void modify(String text) {
        this.text = text;
        this.updateDateTime = LocalDateTime.now();
    }
}
