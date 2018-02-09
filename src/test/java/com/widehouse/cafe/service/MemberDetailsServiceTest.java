package com.widehouse.cafe.service;

import static org.assertj.core.api.BDDAssertions.thenThrownBy;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.domain.member.MemberRepository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MemberDetailsService.class)
public class MemberDetailsServiceTest {
    @Autowired
    private MemberDetailsService memberDetailsService;
    @MockBean
    private MemberRepository memberRepository;

    @Test
    public void loadUserByUsername_withNotExistsMember_thenRaiseUsernameNotFoundException() {
        given(memberRepository.findByUsername("user"))
                .willReturn(null);

        thenThrownBy(() -> memberDetailsService.loadUserByUsername("user"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
