package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 15..
 */
public class CafeStatisticsTest {
    @Test
    public void decreaseCommentCount_should_set_commentCount_minimum_zero() {
        // given
        CafeStatistics cafeStatistics = new CafeStatistics();
        // when
        cafeStatistics.decreaseCommentCount();
        // then
        assertThat(cafeStatistics.getCafeCommentCount())
                .isEqualTo(-1);
    }
}
