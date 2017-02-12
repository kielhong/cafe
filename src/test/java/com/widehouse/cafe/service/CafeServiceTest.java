package com.widehouse.cafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeCategory;
import com.widehouse.cafe.domain.cafe.CafeMember;
import com.widehouse.cafe.domain.cafe.CafeMemberRole;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.CafeMemberAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 2. 11..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CafeServiceTest {
    @Autowired
    CafeService cafeService;

    private Cafe cafe;

    @Before
    public void setUp() {
        Member member = new Member("user");
        CafeCategory category = new CafeCategory();
        cafe = cafeService.createCafe(member, "url", "name", "description",
                CafeVisibility.PUBLIC, category);
    }

    @Test
    public void createCafe_should_create_cafe_and_assign_cafeManager() {
        // Given
        Member member = new Member("user");
        CafeCategory category = new CafeCategory();
        // When
        Cafe cafe = cafeService.createCafe(member, "cafeurl", "cafename", "cafedescription",
                CafeVisibility.PUBLIC, category);
        // Then
        assertThat(cafe)
                .isNotNull()
                .hasFieldOrPropertyWithValue("url", "cafeurl")
                .hasFieldOrPropertyWithValue("name", "cafename")
                .hasFieldOrPropertyWithValue("description", "cafedescription")
                .hasFieldOrPropertyWithValue("visibility", CafeVisibility.PUBLIC)
                .hasFieldOrPropertyWithValue("category", category);
        assertThat(cafe.getCafeMembers())
                .hasSize(1)
                .extracting("member").containsOnlyOnce(member);
        assertThat(cafe.getCafeMembers())
                .extracting("role").containsOnlyOnce(CafeMemberRole.MANAGER);
    }

    @Test
    public void joinMember_increase_cafeMember() {
        // Given
        Member member = new Member();
        int beforeSize = cafe.getCafeMembers().size();
        // When
        CafeMember cafeMember = cafeService.joinMember(cafe, member);
        // Then
        assertThat(cafe.getCafeMembers())
                .hasSize(beforeSize + 1)
                .contains(cafeMember);
        assertThat(cafe.getStatistics().getCafeMemberCount())
                .isEqualTo(beforeSize + 1);
    }

    @Test
    public void joinMember_existsCafeMember_should_throw_CafeMemberAlreadyExistsException() {
        // Given
        Member member = new Member();
        CafeMember cafeMember = cafeService.joinMember(cafe, member);
        Long beforeSize = cafe.getStatistics().getCafeMemberCount();
        // Then
        assertThatThrownBy(() -> cafeService.joinMember(cafe, member))
                .isInstanceOf(CafeMemberAlreadyExistsException.class);
        assertThat(cafe.getStatistics().getCafeMemberCount())
                .isEqualTo(beforeSize);
    }

}
