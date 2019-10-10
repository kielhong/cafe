package com.widehouse.cafe.cafe.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMember;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.common.exception.CafeMemberExistsException;
import com.widehouse.cafe.member.entity.Member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Created by kiel on 2017. 2. 24..
 */
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = CafeMemberService.class)
class CafeMemberServiceTest {
    @Autowired
    private CafeMemberService cafeMemberService;
    @MockBean
    private CafeMemberRepository cafeMemberRepository;
    @MockBean
    private CafeRepository cafeRepository;

    private Cafe cafe;
    private Member member;

    @BeforeEach
    void setUp() {
        cafe = new Cafe("testurl", "testcafe");
        member = new Member(1L, "member", "password", "nickname", "foo@bar.com");
    }

    @Test
    void joinMember_thenCreateCafeMember() {
        CafeMember cafeMember = cafeMemberService.joinMember(cafe, member);

        then(cafeMember)
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("member", member);
        verify(cafeMemberRepository).save(cafeMember);
    }

    @Test
    void joinMember_thenIncreaseCafeStatisticsCafeMemberCountBy1() {
        Long beforeCount = cafe.getData().getCafeMemberCount();

        cafeMemberService.joinMember(cafe, member);

        then(cafe.getData().getCafeMemberCount())
                .isEqualTo(beforeCount + 1);
        verify(cafeRepository).save(cafe);
    }

    @Test
    void joinMember_withExistsCafeMember_thenRaiseCafeMemberExistsException() {
        given(cafeMemberRepository.existsByCafeMember(cafe, member))
                .willReturn(true);
        Long beforeSize = cafe.getData().getCafeMemberCount();

        thenThrownBy(() -> cafeMemberService.joinMember(cafe, member))
                .isInstanceOf(CafeMemberExistsException.class);
        then(cafe.getData().getCafeMemberCount())
                .isEqualTo(beforeSize);
    }
}
