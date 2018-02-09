package com.widehouse.cafe.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.BDDAssertions.then;
import static org.mockito.BDDMockito.given;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = MemberService.class)
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;
    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private CafeMemberRepository cafeMemberRepository;

    @Test
    public void getCafesByMember_thenListCafes() {
        Member member = new Member("member");
        Cafe cafe1 = new Cafe("url1", "name1");
        Cafe cafe2 = new Cafe("url2", "name2");
        Cafe cafe3 = new Cafe("url3", "name3");
        CafeMember cafeMember1 = new CafeMember(cafe1, member);
        CafeMember cafeMember2 = new CafeMember(cafe2, member);
        CafeMember cafeMember3 = new CafeMember(cafe3, member);
        given(cafeMemberRepository.findCafeByMember(member, PageRequest.of(0, 3)))
                .willReturn(Arrays.asList(cafe1, cafe2, cafe3));

        List<Cafe> result = memberService.getCafesByMember(member, PageRequest.of(0, 3));

        then(result)
                .contains(cafe1, cafe2, cafe3);
    }
}
