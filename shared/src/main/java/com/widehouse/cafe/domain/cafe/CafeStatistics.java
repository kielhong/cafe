package com.widehouse.cafe.domain.cafe;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Embeddable;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Embeddable

@Setter
@Getter
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
    }

    public void decreaseCafeMemberCount() {
        cafeMemberCount--;
    }

    public void increaseCommentCount() {
        cafeCommentCount++;
    }

    public void decreaseCommentCount() {
        cafeCommentCount = cafeCommentCount > 0 ? cafeCommentCount - 1 : 0;
    }
}
