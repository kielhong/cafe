package com.widehouse.cafe.web;

import com.widehouse.cafe.annotation.CurrentMember;
import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.service.CafeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by kiel on 2017. 3. 6..
 */
@RestController
public class ApiCafeCommandController {
    @Autowired
    private CafeService cafeService;

    /**
     * POST /api/cafes.
     * @param cafeForm cafe request form
     * @param member current member
     * @return created {@link Cafe}
     */
    @PostMapping(value = "/api/cafes")
    public Cafe createCafe(@RequestBody Cafe cafeForm,
                           @CurrentMember Member member) {
        Cafe cafe = cafeService.createCafe(member, cafeForm.getUrl(), cafeForm.getName(),
                cafeForm.getDescription(), cafeForm.getVisibility(), cafeForm.getCategory().getId());

        return cafe;
    }
}


