package com.widehouse.cafe.cafe.entity;

import javax.persistence.Embeddable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Embeddable
@Getter
@Setter
public class CafeData {
    private Long memberCount;
    private Long articleCount;
    private Long commentCount;
    private Long visitCount;

    /**
     * Constructor.
     */
    public CafeData() {
        this.memberCount = 0L;
        this.articleCount = 0L;
        this.commentCount = 0L;
        this.visitCount = 0L;
    }

    public void increaseCafeMemberCount() {
        memberCount++;
    }

    public void decreaseCafeMemberCount() {
        if (memberCount > 0) {
            memberCount--;
        }
    }

    public void increaseCommentCount() {
        commentCount++;
    }

    public void decreaseCommentCount() {
        if (commentCount > 0) {
            commentCount--;
        }
    }

    public void increaseArticleCount() {
        articleCount++;
    }
}