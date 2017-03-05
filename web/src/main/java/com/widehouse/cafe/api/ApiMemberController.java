package com.widehouse.cafe.api;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.MemberDetailsService;
import com.widehouse.cafe.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RestController
@RequestMapping("api")
public class ApiMemberController {
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberDetailsService memberDetailsService;

    @GetMapping("/members/my/cafes")
    public List<Cafe> getCafesByMember(@PageableDefault(sort = "cafe.createDateTime",
                                               direction = Sort.Direction.DESC) Pageable pageable) {
        Member member = memberDetailsService.getCurrentMember();

        return memberService.getCafesByMember(member, pageable);
    }
}
