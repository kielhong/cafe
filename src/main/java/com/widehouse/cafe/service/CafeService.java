package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeCategory;
import com.widehouse.cafe.domain.cafe.CafeMemberRole;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.member.Member;
import org.springframework.stereotype.Service;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Service
public class CafeService {

    public Cafe createCafe(Member member, String url, String name, String description,
                           CafeVisibility visibility, CafeCategory category) {
        Cafe cafe = new Cafe(url, name, description, visibility, category);
        cafe.addMember(member, CafeMemberRole.MANAGER);
        return cafe;
    }
}
