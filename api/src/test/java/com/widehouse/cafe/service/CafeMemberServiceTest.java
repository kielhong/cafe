package com.widehouse.cafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.CafeMemberAlreadyExistsException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 2. 24..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CafeMemberServiceTest {
    @MockBean
    private CafeMemberRepository cafeMemberRepository;
    @MockBean
    private CafeRepository cafeRepository;
    @Autowired
    private CafeMemberService cafeMemberService;

    private Cafe cafe;
    private Member member;

    @Before
    public void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        member = new Member("member");
    }

    @Test
    public void joinMember_Should_CreateCafeMember() {
        // when
        CafeMember cafeMember = cafeMemberService.joinMember(cafe, member);
        // then
        then(cafeMember)
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("member", member);
        verify(cafeMemberRepository).save(cafeMember);
    }

    @Test
    public void joinMember_Should_IncreaseCafeStatisticsCafeMemberCountBy1() {
        // given
        Long beforeCount = cafe.getStatistics().getCafeMemberCount();
        // when
        CafeMember cafeMember = cafeMemberService.joinMember(cafe, member);
        // then
        then(cafe.getStatistics().getCafeMemberCount())
                .isEqualTo(beforeCount + 1);
        verify(cafeRepository).save(cafe);
    }

    @Test
    public void joinMemberAlreadyExistsCafeMember_Should_Throw_CafeMemberAlreadyExistsException() {
        // given
        given(cafeMemberRepository.existsByCafeMember(cafe, member))
                .willReturn(true);
        Long beforeSize = cafe.getStatistics().getCafeMemberCount();
        // then
        assertThatThrownBy(() -> cafeMemberService.joinMember(cafe, member))
                .isInstanceOf(CafeMemberAlreadyExistsException.class);
        assertThat(cafe.getStatistics().getCafeMemberCount())
                .isEqualTo(beforeSize);
    }
}
