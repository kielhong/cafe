package com.widehouse.cafe.service;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeCategory;
import com.widehouse.cafe.domain.cafe.CafeMember;
import com.widehouse.cafe.domain.cafe.CafeMemberRole;
import com.widehouse.cafe.domain.cafe.CafeVisibility;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.CafeMemberAlreadyExistsException;
import org.springframework.stereotype.Service;

/**
 * Created by kiel on 2017. 2. 11..
 */
@Service
public class CafeService {

    public Cafe createCafe(Member member, String url, String name, String description,
                           CafeVisibility visibility, CafeCategory category) {
        Cafe cafe = new Cafe(url, name, description, visibility, category);

        CafeMember cafeMember = new CafeMember(cafe, member, CafeMemberRole.MANAGER);
        cafe.addCafeMember(cafeMember);

        return cafe;
    }

    public CafeMember joinMember(Cafe cafe, Member member, CafeMemberRole role) {
        if (cafe.getCafeMembers().stream().noneMatch((x -> x.getMember().equals(member)))) {
            CafeMember cafeMember = new CafeMember(cafe, member, role);
            cafe.addCafeMember(cafeMember);

            member.getCafes().add(cafe);

            return cafeMember;
        } else {
            throw new CafeMemberAlreadyExistsException();
        }
    }

    public CafeMember joinMember(Cafe cafe, Member member) {
        return joinMember(cafe, member, CafeMemberRole.MEMBER);
    }
}
