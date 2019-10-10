package com.widehouse.cafe.cafe.service;

import static com.widehouse.cafe.cafe.entity.CafeMemberRole.MEMBER;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.cafe.entity.CafeMember;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.member.entity.Member;
import com.widehouse.cafe.common.exception.CafeMemberExistsException;
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

    /**
     * join member to cafe.
     * @param cafe cafe to join
     * @param member member who join
     * @return joined {@link CafeMember}
     */
    public CafeMember joinMember(Cafe cafe, Member member) {
        if (!cafeMemberRepository.existsByCafeMember(cafe, member)) {
            CafeMember cafeMember = new CafeMember(cafe, member, MEMBER);
            cafe.getData().increaseCafeMemberCount();

            cafeMemberRepository.save(cafeMember);
            cafeRepository.save(cafe);

            return cafeMember;
        } else {
            throw new CafeMemberExistsException();
        }
    }

    public boolean isCafeMember(Cafe cafe, Member member) {
        return cafeMemberRepository.existsByCafeMember(cafe, member);
    }
}
