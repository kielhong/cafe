package com.widehouse.cafe.domain.member;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 10..
 */
public class MemberTest {
    @Test
    public void constructorTest() {
        Member result = new Member(1L, "foo", "password", "foo@bar.com");

        then(result)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("username", "foo")
                .hasFieldOrPropertyWithValue("password", "password")
                .hasFieldOrPropertyWithValue("email", "foo@bar.com");
    }

}
