package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.BDDAssertions.then;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Tag Test Case")
public class TagTest {
    @Test
    @DisplayName("Tag Creation Test")
    public void tagCreationTest() {
        Tag tag = new Tag("test");

        then(tag).hasFieldOrPropertyWithValue("name", "test");

    }
}
