package com.widehouse.cafe.domain.cafe;

import static com.widehouse.cafe.domain.cafe.CafeVisibility.PRIVATE;
import static java.time.LocalDateTime.now;
import static org.assertj.core.api.BDDAssertions.then;

import groovy.util.logging.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Created by kiel on 2017. 2. 10..
 */
@Slf4j
class CafeTest {
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category(1, "category", 1, now());
    }

    @Test
    void createCafe() {
        Cafe cafe = new Cafe("cafeurl", "cafename", "desc", CafeVisibility.PUBLIC, category);

        then(cafe)
                .hasFieldOrPropertyWithValue("url", "cafeurl")
                .hasFieldOrPropertyWithValue("name", "cafename")
                .hasFieldOrPropertyWithValue("description", "desc")
                .hasFieldOrPropertyWithValue("visibility", CafeVisibility.PUBLIC)
                .hasFieldOrPropertyWithValue("category", category)
                .hasFieldOrProperty("data");
    }

    @Test
    void updateCafeInfo_thenChangeCafeInfo() {
        // given
        Cafe cafe = new Cafe("cafeurl", "cafename", "desc", CafeVisibility.PUBLIC, category);
        // when
        cafe.updateInfo("new name", "new description", CafeVisibility.PRIVATE, category);
        // then
        then(cafe)
                .hasFieldOrPropertyWithValue("name", "new name")
                .hasFieldOrPropertyWithValue("description", "new description")
                .hasFieldOrPropertyWithValue("visibility", PRIVATE)
                .hasFieldOrPropertyWithValue("category", category);
    }
}
