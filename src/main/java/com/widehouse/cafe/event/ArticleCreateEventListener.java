package com.widehouse.cafe.event;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Slf4j
public class ArticleCreateEventListener {
    @Autowired
    private CafeRepository cafeRepository;

    /**
     * listener for article create event.
     * @param event {@link ArticleCreateEvent}
     */
    @EventListener
    @Async
    @Transactional
    public void handleArticleCreatedEvent(ArticleCreateEvent event) {
        Cafe cafe = event.getCafe();
        cafe.getStatistics().increaseArticleCount();
        cafeRepository.save(cafe);

        log.info("ASYNC EVENT : increase cafe article count ");
    }
}
