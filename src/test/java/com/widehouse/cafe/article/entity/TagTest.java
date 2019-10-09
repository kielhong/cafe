package com.widehouse.cafe.article.entity;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

class TagTest {
    @Test
    void tagCreationTest() {
        Tag tag = new Tag("test");

        then(tag).hasFieldOrPropertyWithValue("name", "test");

    }
}
