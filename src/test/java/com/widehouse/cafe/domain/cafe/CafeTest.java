package com.widehouse.cafe.domain.cafe;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.CafeMemberAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by kiel on 2017. 2. 10..
 */
public class CafeTest {
    private Cafe cafe;

    @Before
    public void setUp() {
        cafe = new Cafe("testcafe");
    }

    @Test
    public void modifyCafeName_should_change_cafeName() {
        // When
        cafe.modifyName("new name");
        // Then
        assertThat(cafe.getName()).isEqualTo("new name");
    }

    @Test
    public void addMember_increase_cafeMember() {
        // Given
        Member member = new Member();
        int beforeSize = cafe.getCafeMembers().size();
        // When
        CafeMember cafeMember = cafe.addMember(member);
        // Then
        assertThat(cafe.getCafeMembers())
                .hasSize(beforeSize + 1)
                .contains(cafeMember);
    }

    @Test
    public void addMember_existsCafeMember_should_throw_CafeMemberAlreadyExistsException() {
        // Given
        Member member = new Member();
        cafe.addMember(member);
        // Then
        assertThatThrownBy(() -> cafe.addMember(member))
                .isInstanceOf(CafeMemberAlreadyExistsException.class);
    }

    @Test
    public void removeCafeMember_decrease_cafeMember() {
        // Given
        Member member1 = new Member();
        Member member2 = new Member();
        CafeMember cafeMember1 = cafe.addMember(member1);
        CafeMember cafeMember2 = cafe.addMember(member2);
        int beforeSize = cafe.getCafeMembers().size();
        // When
        cafe.removeCafeMember(cafeMember1);
        // Then
        assertThat(cafe.getCafeMembers())
                .hasSize(beforeSize - 1)
                .doesNotContain(cafeMember1);
    }
}
