package com.widehouse.cafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeMember;

import com.widehouse.cafe.domain.cafe.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MemberServiceTest {
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private CafeMemberRepository cafeMemberRepository;
    @Autowired
    private MemberService memberService;

    @Before
    public void setUp() {

    }


    @Test
    public void getCafesByMemeber_should_return_cafelist() {
        // given
        Member member = new Member("testmember");
        Cafe cafe1 = new Cafe("url1", "name1");
        Cafe cafe2 = new Cafe("url2", "name2");
        Cafe cafe3 = new Cafe("url3", "name3");
        Cafe cafe4 = new Cafe("url4", "name4");
        Cafe cafe5 = new Cafe("url5", "name5");
        CafeMember cafeMember1 = new CafeMember(cafe1, member);
        CafeMember cafeMember2 = new CafeMember(cafe2, member);
        CafeMember cafeMember3 = new CafeMember(cafe3, member);
        CafeMember cafeMember4 = new CafeMember(cafe4, member);
        CafeMember cafeMember5 = new CafeMember(cafe5, member);
        given(cafeMemberRepository.findCafeByMember(member))
                .willReturn(Arrays.asList(cafe1, cafe2, cafe3, cafe4, cafe5));
        // when
        List<Cafe> cafes = memberService.getCafesByMember(member);
        // then
        assertThat(cafes)
                .contains(cafe1, cafe2, cafe3, cafe4, cafe5);




    }
}
