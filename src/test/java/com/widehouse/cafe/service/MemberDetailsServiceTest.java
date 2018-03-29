package com.widehouse.cafe.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
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
