package com.widehouse.cafe.domain.member;

import static org.assertj.core.api.BDDAssertions.then;

import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 10..
 */
public class MemberTest {
    @Test
    public void constructor_withIdAndUsername() {
        Member result = new Member(1L, "foo");

        then(result)
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("username", "foo")
                .hasFieldOrPropertyWithValue("password", "");
    }

    @Test
    public void constructor_withUsername() {
        Member result = new Member("foo");

        then(result)
                .hasFieldOrPropertyWithValue("id", null)
                .hasFieldOrPropertyWithValue("username", "foo")
                .hasFieldOrPropertyWithValue("password", "");
    }
}
