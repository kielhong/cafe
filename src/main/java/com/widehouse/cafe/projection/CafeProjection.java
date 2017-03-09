package com.widehouse.cafe.projection;

import com.widehouse.cafe.domain.cafe.CafeVisibility;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;

/**
 * Created by kiel on 2017. 2. 24..
 */
public interface CafeProjection {
    Long getId();

    String getUrl();

    String getName();

    @Value("#{target.visibility}")
    CafeVisibility getViisibility();

    @Value("#{target.category.id}")
    Long getCategoryId();

    String getDescription();

    LocalDateTime getCreateDateTime();

    @Value("#{target.statistics.cafeArticleCount}")
    Long getArticleCount();

    @Value("#{target.statistics.cafeMemberCount}")
    Long getCafeMemberCount();

    @Value("#{target.statistics.cafeVisitCount}")
    Long getVisitCount();
}
