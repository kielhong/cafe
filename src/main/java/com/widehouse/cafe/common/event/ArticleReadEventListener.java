package com.widehouse.cafe.common.event;

import com.widehouse.cafe.article.entity.Article;
import com.widehouse.cafe.article.entity.ArticleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ArticleReadEventListener {
    @Autowired
    private ArticleRepository articleRepository;

    /**
     * listener for article create event.
     * @param event {@link ArticleCreateEvent}
     */
    @EventListener
    @Async
    @Transactional
    public void handleArticleReadEvent(ArticleReadEvent event) {
        Article article = event.getArticle();
        article.increaseReadCount();
        articleRepository.save(article);
    }
}
