package com.widehouse.cafe.web;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;
import com.widehouse.cafe.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RestController
public class MemberController {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @GetMapping("/members/{memberId}/cafes")
    public List<Cafe> getCafesByMember(@PathVariable Long memberId) {
        Member member = memberRepository.findOne(memberId);

        return memberService.getCafesByMember(member);
    }
}
