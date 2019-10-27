package com.widehouse.cafe.common.dto;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.Board;
import com.widehouse.cafe.article.entity.Tag;
import com.widehouse.cafe.user.entity.User;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleResponse {
    private Long id;
    private Board board;
    private User writer;
    private String title;
    private String content;
    private List<Tag> tags;
    private long readCount;
    private long commentCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ArticleResponse of(Article article) {
        return new ArticleResponse(article.getId(), article.getBoard(), article.getWriter(), article.getTitle(),
                article.getContent(), article.getTags(), article.getReadCount(), article.getCommentCount(),
                article.getCreatedAt(), article.getUpdatedAt());
    }
}