package com.widehouse.cafe.cafe.service;

import static com.widehouse.cafe.cafe.entity.CafeMemberRole.MANAGER;
import static com.widehouse.cafe.cafe.entity.CafeMemberRole.MEMBER;
import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMember;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.cafe.entity.CafeMemberRole;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.common.exception.CafeMemberExistsException;
import com.widehouse.cafe.common.exception.CafeMemberRoleException;
import com.widehouse.cafe.user.entity.User;

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
    private User user;

    @BeforeEach
    void setUp() {
        service = new CafeMemberService(cafeRepository, cafeMemberRepository);

        cafe = new Cafe("testurl", "testcafe");
        user = new User("user", "password");
    }

    @Test
    void joinMember_thenCreateCafeMember() {
        // given
        given(cafeMemberRepository.existsByCafeMember(cafe, user))
                .willReturn(false);
        // when
        CafeMember cafeMember = service.joinMember(cafe, user);
        // then
        then(cafeMember)
                .hasFieldOrPropertyWithValue("cafe", cafe)
                .hasFieldOrPropertyWithValue("member", user);
        verify(cafeMemberRepository).save(cafeMember);
    }

    @Test
    void joinMember_thenIncreaseCafeStatisticsCafeMemberCountBy1() {
        Long beforeCount = cafe.getData().getMemberCount();
        // when
        service.joinMember(cafe, user);
        // then
        then(cafe.getData().getMemberCount())
                .isEqualTo(beforeCount + 1);
        verify(cafeRepository).save(cafe);
    }

    @Test
    void joinMember_withExistsCafeMember_thenRaiseCafeMemberExistsException() {
        // given
        given(cafeMemberRepository.existsByCafeMember(cafe, user))
                .willReturn(true);
        // then
        thenThrownBy(() -> service.joinMember(cafe, user))
                .isInstanceOf(CafeMemberExistsException.class);
        verify(cafeRepository, never()).save(cafe);
    }

    @Test
    void withdrawMember_ThenWithdrawFromCafeAndDecreaseCafeMemberCount() {
        CafeMember cafeMember = CafeMember.builder().cafe(cafe).member(user).role(MEMBER).build();
        given(cafeMemberRepository.findByCafeAndMember(any(Cafe.class), any(User.class)))
                .willReturn(cafeMember);
        // when
        service.withdraw(cafe, user);
        // then
        verify(cafeMemberRepository).delete(cafeMember);
        verify(cafeRepository).save(cafe);
    }

    @Test
    void withdrawMember_GivenManagerRole_ThenWithdrawThrowCafeMemberRoleException() {
        CafeMember cafeMember = CafeMember.builder().cafe(cafe).member(user).role(MANAGER).build();
        given(cafeMemberRepository.findByCafeAndMember(any(Cafe.class), any(User.class)))
                .willReturn(cafeMember);
        // when
        thenThrownBy(() -> service.withdraw(cafe, user))
                .isInstanceOf(CafeMemberRoleException.class);
        // then
        verify(cafeMemberRepository, never()).delete(cafeMember);
        verify(cafeRepository, never()).save(cafe);
    }

}
