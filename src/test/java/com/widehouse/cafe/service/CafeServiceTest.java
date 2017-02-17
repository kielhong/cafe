package com.widehouse.cafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeCategory;
import com.widehouse.cafe.domain.cafe.CafeMember;
import com.widehouse.cafe.domain.cafe.CafeMemberRole;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.CafeMemberAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 11..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CafeServiceTest {
    @MockBean
    private CafeRepository cafeRepository;
    @Autowired
    private CafeService cafeService;

    private CafeCategory category;
    private Cafe cafe;
    private Cafe cafe1;
    private Cafe cafe2;
    private Cafe cafe3;
    private Cafe cafe4;

    @Before
    public void setUp() {
        Member member = new Member("user");
        category = new CafeCategory(1L, "category");
        cafe = cafeService.createCafe(member, "url", "name", "desc", CafeVisibility.PUBLIC, category);
        cafe1 = cafeService.createCafe(member, "url1", "name1", "desc", CafeVisibility.PUBLIC, category);
        cafe2 = cafeService.createCafe(member, "url2", "name2", "desc", CafeVisibility.PUBLIC, category);
        cafe3 = cafeService.createCafe(member, "url3", "name3", "desc", CafeVisibility.PUBLIC, category);
        cafe4 = cafeService.createCafe(member, "url4", "name4", "desc", CafeVisibility.PUBLIC, category);
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

    @Test
    public void getCafesByCategory_should_return_cafes_by_category() {
        // given
        given(cafeRepository.findByCategoryId(category.getId(),
                new PageRequest(0, 4, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount"))))
                .willReturn(Arrays.asList(cafe4, cafe3, cafe2, cafe1));
        // when
        List<Cafe> cafes = cafeService.getCafeByCategory(category.getId(), new PageRequest(0, 4, new Sort(Sort.Direction.DESC, "statistics.cafeMemberCount")));
        // then
        assertThat(cafes)
                .contains(cafe4, cafe3, cafe2, cafe1);
    }

}
