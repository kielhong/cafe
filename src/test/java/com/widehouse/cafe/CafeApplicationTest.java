package com.widehouse.cafe;

import static org.assertj.core.api.BDDAssertions.then;

import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CafeApplicationTest {
    @Test
    public void getDefaultTimeZone_thenUTC() {
        then(TimeZone.getDefault())
                .isEqualTo(TimeZone.getTimeZone("UTC"));
    }
}
