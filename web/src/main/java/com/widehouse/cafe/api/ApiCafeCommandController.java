package com.widehouse.cafe.api;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.CafeService;
import com.widehouse.cafe.service.MemberDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 3. 6..
 */
@RestController
@Slf4j
public class ApiCafeCommandController {
    @Autowired
    private CafeService cafeService;
    @Autowired
    private MemberDetailsService memberDetailsService;


    @PostMapping(value = "/api/cafes")
    public Cafe createCafe(@RequestBody Cafe cafeForm) {
        Member member = memberDetailsService.getCurrentMember();

        log.debug("member@controller : {}", member);
        Cafe cafe = cafeService.createCafe(member, cafeForm.getUrl(), cafeForm.getName(),
                cafeForm.getDescription(), cafeForm.getVisibility(), cafeForm.getCategory().getId());

        return cafe;
    }
}


