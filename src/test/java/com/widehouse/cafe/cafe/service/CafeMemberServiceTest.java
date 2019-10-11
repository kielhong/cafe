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
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Created by kiel on 2017. 2. 24..
 */
@ExtendWith(MockitoExtension.class)
class CafeMemberServiceTest {
    private CafeMemberService service;

    @Mock
    private CafeRepository cafeRepository;
    @Mock
    private CafeMemberRepository cafeMemberRepository;

    private Cafe cafe;
    private Member member;

    @BeforeEach
    void setUp() {
        service = new CafeMemberService(cafeRepository, cafeMemberRepository);

        cafe = new Cafe("testurl", "testcafe");
        member = new Member(1L, "member", "password", "nickname", "foo@bar.com");
    }

    @Test
    void joinMember_thenCreateCafeMember() {
        // given
        given(cafeMemberRepository.existsByCafeMember(cafe, member))
                .willReturn(false);
        // when
        CafeMember cafeMember = service.joinMember(cafe, member);
        // then
        then(cafeMember)
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("member", member);
        verify(cafeMemberRepository).save(cafeMember);
    }

    @Test
    void joinMember_thenIncreaseCafeStatisticsCafeMemberCountBy1() {
        Long beforeCount = cafe.getData().getMemberCount();

        service.joinMember(cafe, member);

        then(cafe.getData().getMemberCount())
                .isEqualTo(beforeCount + 1);
        verify(cafeRepository).save(cafe);
    }

    @Test
    void joinMember_withExistsCafeMember_thenRaiseCafeMemberExistsException() {
        given(cafeMemberRepository.existsByCafeMember(cafe, member))
                .willReturn(true);
        Long beforeSize = cafe.getData().getMemberCount();

        thenThrownBy(() -> service.joinMember(cafe, member))
                .isInstanceOf(CafeMemberExistsException.class);
        then(cafe.getData().getMemberCount())
                .isEqualTo(beforeSize);
    }
}
