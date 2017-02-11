package com.widehouse.cafe.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeMember;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 10..
 */
public class MemberTest {
    private Member member;

    @Before
    public void setUp() {
        member = new Member();
    }

    @Test
    public void joinCafe_add_cafe_to_cafes() {
        // Given
        Cafe cafe = new Cafe("testcafe", "testcafe");
        // When
        cafe.addMember(member);
        // Then
        assertThat(member.getCafes())
                .contains(cafe);
    }

    @Test
    public void withdrawCafe_remove_cafe_from_cafes() {
        // Given
        Cafe cafe = new Cafe("testcafe", "testcafe");
        CafeMember cafeMember = cafe.addMember(member);
        // When
        cafe.removeCafeMember(cafeMember);
        // Then
        assertThat(member.getCafes())
                .doesNotContain(cafe);

    }

}
