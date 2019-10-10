package com.widehouse.cafe.member.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.member.entity.Member;
import com.widehouse.cafe.member.entity.MemberRepository;
import com.widehouse.cafe.member.service.MemberDetailsService;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = MemberDetailsService.class)
@Slf4j
public class MemberDetailsServiceTest {
    @Autowired
    private MemberDetailsService memberDetailsService;
    @MockBean
    private MemberRepository memberRepository;

    @Test
    public void loadUserByUsername_thenReturnUser() {
        Member member = new Member("user", "password", "nickname", "foo@bar.com");
        given(memberRepository.findByUsername("user"))
                .willReturn(member);

        UserDetails result = memberDetailsService.loadUserByUsername("user");

        then(result)
                .isEqualTo(member)
                .hasFieldOrPropertyWithValue("enabled", true);
    }

    @Test
    public void loadUserByUsername_withNotExistsMember_thenRaiseUsernameNotFoundException() {
        given(memberRepository.findByUsername("user"))
                .willReturn(null);

        thenThrownBy(() -> memberDetailsService.loadUserByUsername("user"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
