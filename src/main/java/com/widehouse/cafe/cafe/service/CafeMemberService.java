package com.widehouse.cafe.cafe.service;

import static com.widehouse.cafe.cafe.entity.CafeMemberRole.MEMBER;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMember;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.cafe.entity.CafeRepository;
import com.widehouse.cafe.common.exception.CafeMemberExistsException;
import com.widehouse.cafe.user.entity.User;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Created by kiel on 2017. 2. 24..
 */
@RequiredArgsConstructor
@Service
public class CafeMemberService {
    private final CafeRepository cafeRepository;
    private final CafeMemberRepository cafeMemberRepository;

    /**
     * join member to cafe.
     * @param cafe cafe to join
     * @param member member who join
     * @return joined {@link CafeMember}
     */
    public CafeMember joinMember(Cafe cafe, User member) {
        if (isCafeMember(cafe, member)) {
            throw new CafeMemberExistsException();
        }

        CafeMember cafeMember = new CafeMember(cafe, member, MEMBER);
        cafe.getData().increaseCafeMemberCount();

        cafeMemberRepository.save(cafeMember);
        cafeRepository.save(cafe);

        return cafeMember;
    }

    public boolean isCafeMember(Cafe cafe, User member) {
        return cafeMemberRepository.existsByCafeMember(cafe, member);
    }
}
