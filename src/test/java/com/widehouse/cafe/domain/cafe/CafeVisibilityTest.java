package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 20..
 */
public class CafeVisibilityTest {
    @Test
    public void PUBLIC_Should_ValueOfPublic() {
        // when
        CafeVisibility visibility = CafeVisibility.valueOf("PUBLIC");
        // then
        then(visibility)
                .isEqualTo(CafeVisibility.PUBLIC);
    }
}
