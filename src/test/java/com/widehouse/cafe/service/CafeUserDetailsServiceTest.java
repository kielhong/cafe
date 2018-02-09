package com.widehouse.cafe.service;

import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 3. 3..
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MemberDetailsService.class)
public class CafeUserDetailsServiceTest {
    @Autowired
    private MemberDetailsService cafeUserDetailsService;
    @MockBean
    private MemberRepository memberRepository;

    @Test
    public void loadUserByUsername_thenReturnUser() {
        Member member = new Member("user");
        given(memberRepository.findByUsername("user"))
                .willReturn(member);

        UserDetails result = cafeUserDetailsService.loadUserByUsername("user");

        then(result)
                .isEqualTo(member)
                .hasFieldOrPropertyWithValue("enabled", true);
    }
}
