package com.widehouse.cafe.service;

import static com.widehouse.cafe.domain.cafemember.CafeMemberRole.MEMBER;

import com.widehouse.cafe.domain.cafe.Cafe;
import com.widehouse.cafe.domain.cafe.CafeRepository;
import com.widehouse.cafe.domain.cafemember.CafeMember;
import com.widehouse.cafe.domain.cafemember.CafeMemberRepository;
import com.widehouse.cafe.domain.member.Member;
import com.widehouse.cafe.exception.CafeMemberAlreadyExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by kiel on 2017. 2. 24..
 */
@Service
public class CafeMemberService {
    @Autowired
    private CafeMemberRepository cafeMemberRepository;
    @Autowired
    private CafeRepository cafeRepository;

    public CafeMember joinMember(Cafe cafe, Member member) {
        if (!cafeMemberRepository.existsByCafeMember(cafe, member)) {
            CafeMember cafeMember = new CafeMember(cafe, member, MEMBER);
            cafe.getStatistics().increaseCafeMemberCount();

            cafeMemberRepository.save(cafeMember);
            cafeRepository.save(cafe);

            return cafeMember;
        } else {
            throw new CafeMemberAlreadyExistsException();
        }
    }

}
