package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 15..
 */
public class CafeStatisticsTest {
    @Test
    public void decreaseCommentCount_Should_DescreaseCommentCount_ByOne() {
        // given
        CafeStatistics cafeStatistics = new CafeStatistics();
        Long count = cafeStatistics.getCafeCommentCount();
        // when
        cafeStatistics.decreaseCommentCount();
        // then
        then(cafeStatistics.getCafeCommentCount())
                .isEqualTo(count - 1);
    }

    @Test
    public void decreaseCafeMemberCount_Shoud_DescreaseCafeMemberCount_ByOne() {
        // given
        CafeStatistics cafeStatistics = new CafeStatistics();
        Long count = cafeStatistics.getCafeMemberCount();
        // when
        cafeStatistics.decreaseCafeMemberCount();
        // then
        then(cafeStatistics.getCafeMemberCount())
                .isEqualTo(count - 1);
    }
}
