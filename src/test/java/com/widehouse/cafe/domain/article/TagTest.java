package com.widehouse.cafe.domain.article;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

public class TagTest {
    @Test
    public void tagCreationTest() {
        Tag tag = new Tag("test");

        then(tag).hasFieldOrPropertyWithValue("name", "test");

    }
}
