package com.widehouse.cafe.event;

import com.widehouse.cafe.cafe.entity.Cafe;

import org.springframework.context.ApplicationEvent;

public class ArticleCreateEvent extends ApplicationEvent {
    public ArticleCreateEvent(Cafe cafe) {
        super(cafe);
    }

    public Cafe getCafe() {
        return (Cafe)getSource();
    }
}
