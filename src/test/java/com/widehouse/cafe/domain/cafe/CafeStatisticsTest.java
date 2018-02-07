package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 15..
 */
public class CafeStatisticsTest {
    private CafeStatistics statistics;

    @Before
    public void init() {
        statistics = new CafeStatistics();
    }

    @Test
    public void decreaseCommentCount_thenDecreaseCommentCountBy1() {
        statistics.setCafeCommentCount(10L);
        Long count = statistics.getCafeCommentCount();

        statistics.decreaseCommentCount();

        then(statistics.getCafeCommentCount()).isEqualTo(count - 1);
    }

    @Test
    public void decreaseCommentCount_withZero_thenCommentCountZero() {
        statistics.setCafeCommentCount(0L);

        statistics.decreaseCommentCount();

        then(statistics.getCafeCommentCount()).isEqualTo(0);
    }

    @Test
    public void decreaseCafeMemberCount_thenDecreaseCafeMemberCountBy1() {
        statistics.setCafeMemberCount(10L);
        Long count = statistics.getCafeMemberCount();

        statistics.decreaseCafeMemberCount();

        then(statistics.getCafeMemberCount()).isEqualTo(count - 1);
    }

    @Test
    public void decreaseCafeMemberCount_withZero_thenCafeMemberCountZero() {
        statistics.setCafeMemberCount(0L);

        statistics.decreaseCafeMemberCount();

        then(statistics.getCafeMemberCount()).isEqualTo(0);
    }
}
