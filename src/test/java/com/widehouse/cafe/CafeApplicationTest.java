package com.widehouse.cafe;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.TimeZone;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CafeApplicationTest {
    @Test
    void getDefaultTimeZone_thenUTC() {
        then(TimeZone.getDefault())
                .isEqualTo(TimeZone.getTimeZone("UTC"));
    }
}
