package com.widehouse.cafe.user.entity;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.jupiter.api.Test;

/**
 * Created by kiel on 2017. 2. 10..
 */
class UserTest {
    @Test
    void constructorTest() {
        User result = new User(1L, "foo", "password");

        then(result)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("username", "foo")
                .hasFieldOrPropertyWithValue("password", "password");
    }

}
