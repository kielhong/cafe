package com.widehouse.cafe.domain.cafe;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Embeddable
@Getter
@Setter
public class CafeStatistics {
    private Long cafeMemberCount;
    private Long cafeArticleCount;
    private Long cafeCommentCount;
    private Long cafeVisitCount;

    /**
     * Constructor.
     */
    public CafeStatistics() {
        this.cafeMemberCount = 0L;
        this.cafeArticleCount = 0L;
        this.cafeCommentCount = 0L;
        this.cafeVisitCount = 0L;
    }

    public void increaseCafeMemberCount() {
        cafeMemberCount++;
    }

    public void decreaseCafeMemberCount() {
        if (cafeMemberCount > 0) {
            cafeMemberCount--;
        }
    }

    public void increaseCommentCount() {
        cafeCommentCount++;
    }

    public void decreaseCommentCount() {
        if (cafeCommentCount > 0) {
            cafeCommentCount--;
        }
    }

    public void increaseArticleCount() {
        cafeArticleCount++;
    }
}