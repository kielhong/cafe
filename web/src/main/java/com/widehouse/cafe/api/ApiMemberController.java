package com.widehouse.cafe.api;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.domain.member.MemberRepository;
import com.widehouse.cafe.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RestController
public class ApiMemberController {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

    @GetMapping("/members/{username}/cafes")
    public List<Cafe> getCafesByMember(@PathVariable String username,
                                       @PageableDefault(sort = "cafe.createDateTime",
                                               direction = Sort.Direction.DESC) Pageable pageable) {
        Member member = memberRepository.findByUsername(username);

        return memberService.getCafesByMember(member, pageable);
    }
}
