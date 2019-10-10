package com.widehouse.cafe.member.service;

import com.widehouse.cafe.cafe.entity.Cafe;
import com.widehouse.cafe.cafe.entity.CafeMemberRepository;
import com.widehouse.cafe.member.entity.Member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Created by kiel on 2017. 2. 15..
 */
@Service
public class MemberService {
    @Autowired
    private CafeMemberRepository cafeMemberRepository;

    public List<Cafe> getCafesByMember(Member member, Pageable pageable) {
        return cafeMemberRepository.findCafeByMember(member, pageable);
    }
}
