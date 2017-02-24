package com.widehouse.cafe.projection;

import com.widehouse.cafe.domain.cafe.CafeStatistics;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.cafe.Category;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDateTime;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * Created by kiel on 2017. 2. 23..
 */
public interface Cafe {
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
