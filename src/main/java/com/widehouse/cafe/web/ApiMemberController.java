package com.widehouse.cafe.web;

import com.widehouse.cafe.annotation.CurrentMember;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.MemberService;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 2. 15..
 */
@RestController
@RequestMapping("api")
public class ApiMemberController {
    @Autowired
    private MemberService memberService;

    @GetMapping("/members/my/cafes")
    public List<Cafe> getCafesByMember(@PageableDefault(sort = "cafe.createDateTime", direction = Sort.Direction.DESC)
                                                   Pageable pageable,
                                       @CurrentMember Member member) {
        return  memberService.getCafesByMember(member, pageable);
    }
}
