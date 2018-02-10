package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 20..
 */
public class CafeVisibilityTest {
    @Test
    public void valueOf_withPublic_thenPublic() {
        CafeVisibility visibility = CafeVisibility.valueOf("PUBLIC");

        then(visibility)
                .isEqualTo(CafeVisibility.PUBLIC);
    }
}