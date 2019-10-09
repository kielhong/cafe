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
        cafeData.setCafeCommentCount(10L);
        Long count = cafeData.getCafeCommentCount();

        cafeData.decreaseCommentCount();

        then(cafeData.getCafeCommentCount()).isEqualTo(count - 1);
    }

    @Test
    void decreaseCommentCount_withZero_thenCommentCountZero() {
        cafeData.setCafeCommentCount(0L);

        cafeData.decreaseCommentCount();

        then(cafeData.getCafeCommentCount()).isEqualTo(0);
    }

    @Test
    void decreaseCafeMemberCount_thenDecreaseCafeMemberCountBy1() {
        cafeData.setCafeMemberCount(10L);
        Long count = cafeData.getCafeMemberCount();

        cafeData.decreaseCafeMemberCount();

        then(cafeData.getCafeMemberCount()).isEqualTo(count - 1);
    }

    @Test
    void decreaseCafeMemberCount_withZero_thenCafeMemberCountZero() {
        cafeData.setCafeMemberCount(0L);

        cafeData.decreaseCafeMemberCount();

        then(cafeData.getCafeMemberCount()).isEqualTo(0);
    }
}
