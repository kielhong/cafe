package com.widehouse.cafe.common.event;

import com.widehouse.cafe.article.entity.Article;

import org.springframework.context.ApplicationEvent;

public class ArticleReadEvent extends ApplicationEvent {
    public ArticleReadEvent(Article article) {
        super(article);
    }

    public Article getArticle() {
        return (Article)getSource();
    }
}
