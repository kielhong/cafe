package com.widehouse.cafe.domain.cafe;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Getter
@Slf4j
public class CafeStatistics {
    private Long cafeMemberCount;
    private Long cafeArticleCount;
    private Long cafeCommentCount;
    private Long cafeVisitCount;


    public CafeStatistics() {
        cafeMemberCount = 0L;
        cafeArticleCount = 0L;
        cafeCommentCount = 0L;
        cafeVisitCount = 0L;
    }

    public void increaseCafeMemberCount() {
        cafeMemberCount++;
        log.debug("after increase cafe Member Count : {}", cafeMemberCount);
    }

    public void decreaseCafeMemberCount() {
        cafeMemberCount--;
    }
}
