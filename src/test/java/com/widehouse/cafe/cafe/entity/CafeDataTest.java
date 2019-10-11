package com.widehouse.cafe.cafe.entity;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created by kiel on 2017. 2. 15..
 */
class CafeDataTest {
    private CafeData cafeData;

    @BeforeEach
    void setUp() {
        cafeData = new CafeData();
    }

    @Test
    void decreaseCommentCount_thenDecreaseCommentCountBy1() {
        cafeData.setCommentCount(10L);
        Long count = cafeData.getCommentCount();

        cafeData.decreaseCommentCount();

        then(cafeData.getCommentCount()).isEqualTo(count - 1);
    }

    @Test
    void decreaseCommentCount_withZero_thenCommentCountZero() {
        cafeData.setCommentCount(0L);

        cafeData.decreaseCommentCount();

        then(cafeData.getCommentCount()).isEqualTo(0);
    }

    @Test
    void decreaseCafeMemberCount_thenDecreaseCafeMemberCountBy1() {
        cafeData.setMemberCount(10L);
        Long count = cafeData.getMemberCount();

        cafeData.decreaseCafeMemberCount();

        then(cafeData.getMemberCount()).isEqualTo(count - 1);
    }

    @Test
    void decreaseCafeMemberCount_withZero_thenCafeMemberCountZero() {
        cafeData.setMemberCount(0L);

        cafeData.decreaseCafeMemberCount();

        then(cafeData.getMemberCount()).isEqualTo(0);
    }
}
