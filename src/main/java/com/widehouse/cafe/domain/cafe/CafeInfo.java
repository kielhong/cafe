package com.widehouse.cafe.domain.cafe;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Getter
@Slf4j
public class CafeInfo {
    private Long cafeMemberCount;
    private Long cafeArticleCount;
    private Long cafeCommentCount;
    private Long cafeVisitCount;
    private LocalDateTime createDateTime;

    public CafeInfo() {
        cafeMemberCount = 0L;
        cafeArticleCount = 0L;
        cafeCommentCount = 0L;
        cafeVisitCount = 0L;
        createDateTime = LocalDateTime.now();
    }

    public void increaseCafeMemberCount() {
        cafeMemberCount++;
        log.debug("after increase cafe Member Count : {}", cafeMemberCount);
    }

    public void decreaseCafeMemberCount() {
        cafeMemberCount--;
    }
}
