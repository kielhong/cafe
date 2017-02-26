package com.widehouse.cafe.projection;

import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

/**
 * Created by kiel on 2017. 2. 25..
 */
public interface ArticleProjection {
    Long getId();

    @Value("#{target.cafe.id}")
    Long getCafeId();

    @Value("#{target.board.id}")
    Long getBoardId();

    @Value("#{target.writer.id}")
    Long getWriterId();

    @Value("#{target.writer.nickname}")
    String getWriterName();

    String getTitle();

    int getCommentCount();

    LocalDateTime getCreateDateTime();

    LocalDateTime getUpdateDateTime();
}
