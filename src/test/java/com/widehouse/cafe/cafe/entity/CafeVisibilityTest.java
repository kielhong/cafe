package com.widehouse.cafe.cafe.entity;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

/**
 * Created by kiel on 2017. 2. 20..
 */
class CafeVisibilityTest {
    @Test
    void valueOf_withPublic_thenPublic() {
        CafeVisibility visibility = CafeVisibility.valueOf("PUBLIC");

        then(visibility)
                .isEqualTo(CafeVisibility.PUBLIC);
    }
}