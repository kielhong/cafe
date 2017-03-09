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
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by kiel on 2017. 3. 3..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CafeUserDetailsServiceTest {
    @MockBean
    private MemberRepository memberRepository;
    @Autowired
    private MemberDetailsService cafeUserDetailsService;

    @Test
    public void loadUserByUsername_Should_Return_User() {
        // given
        given(memberRepository.findByUsername("user"))
                .willReturn(new Member("user"));
        // when
        UserDetails userDetails = cafeUserDetailsService.loadUserByUsername("user");
        // then
        then(userDetails)
                .hasFieldOrPropertyWithValue("username", "user");
    }
}
